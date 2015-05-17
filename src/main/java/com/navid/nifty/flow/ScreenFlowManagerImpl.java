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
import com.navid.nifty.flow.jgrapht.GraphLink;
import de.lessvoid.nifty.screen.ScreenController;
import org.jgrapht.graph.DirectedMultigraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public ScreenFlowManagerImpl(InstanceResolutor instanceResolutor) {
        this.instanceResolutor = instanceResolutor;
    }

    @Override
    public void addScreenDefinition(ScreenDefinition screenDefinition) {
        Preconditions.checkArgument(!screenDefinitions.containsKey(screenDefinition.getScreenName()),
                screenDefinition.getScreenName() + " already declared.");

        screenDefinitions.put(screenDefinition.getScreenName(), screenDefinition);
    }

    @Override
    public void addFlowDefinition(String flowName, final Optional<String> screenName, List<String> flowDefinition) {
        if(screenName.isPresent()){
            ScreenId screenInstanceFrom = fromString(screenName.get());
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
                graph.addEdge(previous, current, new GraphLink(NEXT));
                graph.addEdge(current, previous, new GraphLink(PREV));
            } else if (parentScreen != null) {
                graph.addEdge(parentScreen, current, new GraphLink(rootFlowDefinition.getName()));
                graph.addEdge(current, parentScreen, new GraphLink(PREV));
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
                    = instanceResolutor.resolveScreenGenerator(screenDefinition.getInterfaceConstructor());
            ScreenController controller
                    = instanceResolutor.resolveScreenControler(screenDefinition.getController());

            Screen screen = new Screen(interfaceConstructor, controller, flow, screenName);
            return screen;
        }
    }

    @Override
    public String nextScreen() {
        if(currentScreen == null) {
            currentScreen = rootFlowDefinition.getScreens().get(0);
        } else {
            if(nextScreenHint != null) {
                Set<GraphLink> edges = graph.outgoingEdgesOf(currentScreen);
                Optional<GraphLink> edge = Iterables.tryFind(edges, new FilterByEdgeName(nextScreenHint));
                if(edge.isPresent()){
                    currentScreen = graph.getEdgeTarget(edge.get());
                }
            }
        }
        nextScreenHint = null;
        return currentScreen.getUniqueScreenName();
    }

    @Override
    public void setNextScreenHint(String nextScreenHint) {
        this.nextScreenHint = nextScreenHint;
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
}
