package com.navid.nifty.flow;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import de.lessvoid.nifty.screen.ScreenController;
import org.jgrapht.graph.DirectedMultigraph;

import java.util.*;

public final class ScreenFlowManager {

    public static final String NEXT = "next";
    public static final String PREV = "prev";
    public static final String POP = "pop";

    private final Map<String, ScreenConfiguration> configs = new HashMap<String, ScreenConfiguration>();
    private final Map<String, ScreenFlowUnit> units = new HashMap<String, ScreenFlowUnit>();
    private final Map<String, FlowDefinition> flows = new HashMap<String, FlowDefinition>();

    private final InstanceResolutor instanceResolutor;

    private final DirectedMultigraph<ScreenFlowUnit, ScreenLink> grapht = new DirectedMultigraph(ScreenLink.class);

    private ScreenFlowUnit currentScreen;
    private String nextScreenHint;

    public ScreenFlowManager( InstanceResolutor instanceResolutor ) {
        this.instanceResolutor = instanceResolutor;
    }

    public synchronized void addScreenConfiguration(ScreenConfiguration screenConfiguration) {
        Preconditions.checkArgument(!configs.containsKey(screenConfiguration.getScreenName()),
                screenConfiguration.getScreenName() + " already declared.");

        configs.put(screenConfiguration.getScreenName(), screenConfiguration);
    }

    public synchronized void addFlowDefinition(FlowDefinition flowDefinition) {
        Preconditions.checkArgument(!flowDefinition.getScreenNames().isEmpty());
        Preconditions.checkArgument(!flows.containsKey(flowDefinition.getName()),
                "Flow with name " + flowDefinition.getName() + " already defined");

        if(flowDefinition.getName().equals("root")){
            ScreenFlowUnit previousNode = null;
            for(String screenName : flowDefinition.getScreenNames()){
                Preconditions.checkArgument(configs.containsKey(screenName));
                ScreenFlowUnit instance = instantiateConfiguration(flowDefinition.getName(), configs.get(screenName));
                units.put(instance.getScreenName(), instance);
                grapht.addVertex(instance);

                if(previousNode != null) {
                    grapht.addEdge(previousNode, instance, new ScreenLink(NEXT));
                    grapht.addEdge(instance, previousNode, new ScreenLink(PREV));
                }

                previousNode = instance;
                if(currentScreen==null){
                    currentScreen = instance;
                }
            }

        } else {
            Preconditions.checkState(flows.containsKey("root"), "No root flow has been defined yet.");

        }
        flows.put(flowDefinition.getName(), flowDefinition);
    }

    private ScreenFlowUnit instantiateConfiguration(String prefix, ScreenConfiguration screenConfiguration) {
        ScreenGenerator sg = instanceResolutor.resolveScreenGenerator(screenConfiguration.getInterfaceConstructor());
        ScreenController sc = instanceResolutor.resolveScreenControler(screenConfiguration.getController());
        ScreenFlowUnit sfu = new ScreenFlowUnit(prefix + ":" + screenConfiguration.getScreenName(), sg, sc);
        return sfu;
    }

    public String nextScreen() {
        Preconditions.checkState(flows.containsKey("root"), "No root flow has been defined yet.");
        Set<ScreenLink> links = grapht.outgoingEdgesOf(currentScreen);

        Iterable<ScreenLink> result;
        if(NEXT.equals(nextScreenHint)) {
            result = Iterables.filter(links, new FilterByEdgeName(NEXT));
        } else if (PREV.equals(nextScreenHint)) {
            result = Iterables.filter(links, new FilterByEdgeName(PREV));
        } else if (nextScreenHint != null) {
            result = Iterables.filter(links, new FilterByEdgeName(nextScreenHint));
        } else {
            return currentScreen.getScreenName();
        }

        if(result.iterator().hasNext()){
            currentScreen = grapht.getEdgeTarget(result.iterator().next());
            return currentScreen.getScreenName();
        } else {
            return currentScreen.getScreenName();
        }

    }

    public void setNextScreenHint(String nextScreenHint) {
        this.nextScreenHint = nextScreenHint;
    }

    private static class FilterByEdgeName implements Predicate<ScreenLink>{
        private final String name;

        public FilterByEdgeName(String name) {
            this.name=name;
        }

        @Override
        public boolean apply(ScreenLink input) {
            return input.getLinkName().equals(name);
        }
    }
}
