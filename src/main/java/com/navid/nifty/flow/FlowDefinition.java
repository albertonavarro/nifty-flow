package com.navid.nifty.flow;

import java.util.List;

/**
 * Created by alberto on 5/6/15.
 */
public class FlowDefinition {

    private final String name;

    private final List<String> screenNames;

    public FlowDefinition(String name, List<String> screenNames) {
        this.name = name;
        this.screenNames = screenNames;
    }

    public String getName() {
        return name;
    }

    public List<String> getScreenNames() {
        return screenNames;
    }
}
