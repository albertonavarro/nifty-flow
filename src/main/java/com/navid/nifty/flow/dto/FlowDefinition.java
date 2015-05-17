package com.navid.nifty.flow.dto;

import com.google.common.base.Preconditions;

import java.util.List;

/**
 *
 */
public class FlowDefinition {

    private final String name;

    private final List<String> screenNames;

    public FlowDefinition(String name, List<String> screenNames) {
        this.name = name;
        this.screenNames = Preconditions.checkNotNull(screenNames);
    }

    public String getName() {
        return name;
    }

    public List<String> getScreenNames() {
        return screenNames;
    }
}
