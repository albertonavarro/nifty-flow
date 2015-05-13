package com.navid.nifty.flow.jgrapht;

/**
 * This class helps to make the links unique across the graph, as hashcode and equals aren't
 * delegated to String's ones.
 */
public class GraphLink {

    private String linkName;

    public GraphLink(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkName(){
        return linkName;
    }
}
