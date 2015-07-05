package com.navid.nifty.flow;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.navid.nifty.flow.domain.Flow;
import com.navid.nifty.flow.domain.Screen;
import com.navid.nifty.flow.dto.ScreenDefinition;
import com.navid.nifty.flow.domain.ScreenId;
import com.navid.nifty.flow.jgrapht.ExitModuleGraphLink;
import com.navid.nifty.flow.jgrapht.GraphLink;
import com.navid.nifty.flow.jgrapht.ImplicitGraphLink;
import com.navid.nifty.flow.jgrapht.ModuleGraphLink;
import com.navid.nifty.flow.resolutors.InstanceResolutionException;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.ScreenController;
import org.jgrapht.graph.DirectedMultigraph;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.navid.nifty.flow.domain.ScreenId.fromString;

/**
 * Main class to control what's next screen based on current, hint and graph
 */
public class ScreenFlowManagerImpl implements ScreenFlowManager{

    private final Map<String, ScreenDefinition> screenDefinitions = new HashMap<String, ScreenDefinition>();
    private final Map<String, Flow> flows = new HashMap<String, Flow>();

    private final InstanceResolutor instanceResolutor;

    private final DirectedMultigraph<Screen, GraphLink> graph
            = new DirectedMultigraph<Screen, GraphLink>(GraphLink.class);

    private Flow rootFlowDefinition;
    private Screen currentScreen;
    private String nextScreenHint;
    private Nifty nifty;

    public ScreenFlowManagerImpl(Nifty nifty, InstanceResolutor instanceResolutor) {
        this.instanceResolutor = instanceResolutor;
        this.nifty = nifty;
    }

    @Override
    public void addScreenDefinition(ScreenDefinition screenDefinition) throws InstanceResolutionException {
        Preconditions.checkArgument(!screenDefinitions.containsKey(screenDefinition.getScreenName()),
                screenDefinition.getScreenName() + " already declared.");

        screenDefinitions.put(screenDefinition.getScreenName(), screenDefinition);
        nifty.registerScreenController(instanceResolutor.resolveScreenControler(screenDefinition.getController()));
    }

    @Override
    public void addFlowDefinition(String flowName, final Optional<String> screenName, List<String> flowDefinition) {
        if(screenName.isPresent()){
            Optional<ScreenId> optionalScreenInstanceFrom = fromString(screenName.get());
            if(!optionalScreenInstanceFrom.isPresent()){
                throw new IllegalStateException("Parent flow must be formatted like flow:screen: " + screenName.get());
            }

            ScreenId screenInstanceFrom = optionalScreenInstanceFrom.get();
            //CHILD FLOW
            if(!flows.containsKey(screenInstanceFrom.getFlowName())){
                throw new IllegalStateException("Parent flow declared not found: " + screenInstanceFrom.getFlowName());
            }

            Flow parentFlow = flows.get(screenInstanceFrom.getFlowName());
            Screen parentScreen = parentFlow.getScreen(screenInstanceFrom.getScreenName());

            Flow flow = createFlow(flowName, flowDefinition);
            flows.put(flowName, flow);

            addToGraph(flow, parentScreen);
        } else {
            //ROOT FLOW
            if(rootFlowDefinition != null) {
                throw new IllegalStateException("Root flow already declared with name " + rootFlowDefinition.getName() + " while declaring root flow " + flowName);
            }

            rootFlowDefinition = createFlow(flowName, flowDefinition);
            flows.put(flowName, rootFlowDefinition);
            currentScreen = rootFlowDefinition.getScreens().get(0);

            addToGraph(rootFlowDefinition, null);
        }

    }

    private Flow createFlow(String flowName, List<String> flowDefinition) {
        Flow flow = new Flow(flowName);
        List<Screen> listScreens = newArrayList( Lists.transform(flowDefinition, new STRING_TO_SCREEN(flow)));
        flow.setScreens(listScreens);
        return flow;
    }

    private void addToGraph(Flow rootFlowDefinition, Screen parentScreen) {
        Screen previous = null;
        for(Screen current : rootFlowDefinition.getScreens()) {
            graph.addVertex(current);
            if(previous!=null){
                graph.addEdge(previous, current, new ImplicitGraphLink(NEXT));
                graph.addEdge(current, previous, new ImplicitGraphLink(PREV));
            } else if (parentScreen != null) {
                graph.addEdge(parentScreen, current, new ModuleGraphLink(rootFlowDefinition.getName()));
                graph.addEdge(current, parentScreen, new ExitModuleGraphLink(PREV));
            }
            previous = current;
        }
    }

    private class STRING_TO_SCREEN implements Function<String, Screen> {
        private Flow flow;

        public STRING_TO_SCREEN(Flow flow) {
            this.flow = flow;
        }

        @Override
        public Screen apply(String screenName) {
            ScreenDefinition screenDefinition = screenDefinitions.get(screenName);
            ScreenGenerator interfaceConstructor
                    = null;
            try {
                interfaceConstructor = instanceResolutor.resolveScreenGenerator(screenDefinition.getInterfaceConstructor());
                ScreenController controller
                        = instanceResolutor.resolveScreenControler(screenDefinition.getController());

                Screen screen = new Screen(interfaceConstructor, controller, flow, screenName);
                return screen;
            } catch (InstanceResolutionException e) {
                e.printStackTrace();
            }
            throw new RuntimeException();
        }
    }

    @Override
    public String nextScreen() {
        Screen candidateScreen = null;

        //First we try with the direct hint
        if(nextScreenHint != null) {
            Set<GraphLink> edges = graph.outgoingEdgesOf(currentScreen);
            Optional<GraphLink> edge = Iterables.tryFind(edges, new FilterByEdgeName(nextScreenHint));
            if(edge.isPresent()){
                candidateScreen = graph.getEdgeTarget(edge.get());
            }

            //then the hint as screen name
            if(candidateScreen == null) {
                Optional<ScreenId> optScreenId = ScreenId.fromString(nextScreenHint);
                if(optScreenId.isPresent()){
                    ScreenId screenId = optScreenId.get();
                    //Reference to a global positioned screen.
                    if(flows.containsKey(screenId.getFlowName())){
                        candidateScreen = flows.get(screenId.getFlowName()).getScreen(screenId.getScreenName());
                    }
                } else {
                    //Reference to a screen local to this flow.
                    candidateScreen = currentScreen.getParent().getScreen(nextScreenHint);

                }
            }

            if(candidateScreen != null){
                currentScreen = candidateScreen;
            }

            nextScreenHint = null;
        }

        currentScreen.getInterfaceConstructor().buildScreen(currentScreen.getUniqueScreenName());
        return currentScreen.getUniqueScreenName();
    }

    @Override
    public void setNextScreenHint(String nextScreenHint) {
        this.nextScreenHint = nextScreenHint;
    }

    @Override
    public Collection<String> getChildren() {
        Iterable<GraphLink> result = Iterables.filter(graph.outgoingEdgesOf(currentScreen), new FilterByEdgeType(ModuleGraphLink.class));
        return newArrayList(Iterables.transform(result, new Function<GraphLink, String>() {
            @Override
            public String apply(GraphLink input) {
                return input.getLinkName();
            }
        }));
    }

    private static class FilterByEdgeName implements Predicate<GraphLink> {
        private final String name;

        public FilterByEdgeName(String name) {
            this.name=name;
        }

        @Override
        public boolean apply(GraphLink input) {
            return input.getLinkName().equals(name);
        }
    }

    private static class FilterByEdgeType implements Predicate<GraphLink> {
        private final Class<? extends GraphLink> type;

        private FilterByEdgeType(Class<? extends GraphLink> type) {
            this.type = type;
        }

        @Override
        public boolean apply(GraphLink input) {
            return (type.isInstance(input));
        }
    }
}
