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
import com.navid.nifty.flow.dto.ScreenInstance;
import de.lessvoid.nifty.screen.ScreenController;
import org.jgrapht.graph.DirectedMultigraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by alberto on 12/05/15.
 */
public class ScreenFlowManagerImpl implements ScreenFlowManager{

    private final Map<String, ScreenDefinition> screenDefinitions = new HashMap<String, ScreenDefinition>();
    private final Map<String, ScreenFlowUnit> units = new HashMap<String, ScreenFlowUnit>();
    private final Map<String, Flow> flows = new HashMap<String, Flow>();

    private final InstanceResolutor instanceResolutor;

    private final DirectedMultigraph<Screen, String> graph
            = new DirectedMultigraph<Screen, String>(String.class);

    private Flow rootFlowDefinition;
    private Screen currentScreen;
    private String nextScreenHint;

    public ScreenFlowManagerImpl(InstanceResolutor instanceResolutor) {
        this.instanceResolutor = instanceResolutor;
    }

    @Override
    public void addScreenConfiguration(ScreenDefinition screenDefinition) {
        Preconditions.checkArgument(!screenDefinitions.containsKey(screenDefinition.getScreenName()),
                screenDefinition.getScreenName() + " already declared.");

        screenDefinitions.put(screenDefinition.getScreenName(), screenDefinition);
    }

    @Override
    public void addFlowDefinition(String flowName, final Optional<ScreenInstance> screenNameFrom, List<String> flowDefinition) {
        if(screenNameFrom.isPresent()){
            //CHILD FLOW
            if(!flows.containsKey(screenNameFrom.get().getFlowName())){
                throw new RuntimeException("Parent flow declared not found: " + screenNameFrom.get().getFlowName());
            }



        } else {
            //ROOT FLOW
            if(rootFlowDefinition != null) {
                throw new RuntimeException("Root flow already declared with name " + rootFlowDefinition.getName() + " while declaring root flow " + flowName);
            }

            rootFlowDefinition = new Flow(flowName);
            List<Screen> listScreens = newArrayList( Lists.transform(flowDefinition, new STRING_TO_SCREEN(rootFlowDefinition)));
            rootFlowDefinition.setScreens(listScreens);

            addToGraph(rootFlowDefinition);
        }

    }

    private void addToGraph(Flow rootFlowDefinition) {
        Screen previous = null;
        for(Screen current : rootFlowDefinition.getScreens()) {
            graph.addVertex(current);
            if(previous!=null){
                graph.addEdge(previous, current, new String(NEXT));
                graph.addEdge(current, previous, new String(PREV));
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
            return currentScreen.getUniqueScreenName();
        } else {
            if(nextScreenHint != null) {
                Set<String> edges = graph.outgoingEdgesOf(currentScreen);
                String edge = Iterables.find(edges, new FilterByEdgeName(nextScreenHint));
                currentScreen = graph.getEdgeTarget(edge);
                return currentScreen.getUniqueScreenName();
            } else {
                return currentScreen.getUniqueScreenName();
            }
        }
    }

    @Override
    public void setNextScreenHint(String nextScreenHint) {
        this.nextScreenHint = nextScreenHint;
    }

    private static class FilterByEdgeName implements Predicate<String> {
        private final String name;

        public FilterByEdgeName(String name) {
            this.name=name;
        }

        @Override
        public boolean apply(String input) {
            return input.equals(name);
        }
    }
}
