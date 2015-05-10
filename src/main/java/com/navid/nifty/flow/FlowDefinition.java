package com.navid.nifty.flow;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import java.util.List;

/**
 * Created by alberto on 5/6/15.
 */
public class FlowDefinition {

    private final String name;

    private final Optional<String> parentScreen;

    private final List<String> screenNames;

    public FlowDefinition(String name, Optional<String> parentScreen, List<String> screenNames) {
        this.name = Preconditions.checkNotNull(name);
        this.screenNames = Preconditions.checkNotNull(screenNames);
        this.parentScreen = parentScreen;
    }

    public String getName() {
        return name;
    }

    public List<String> getScreenNames() {
        return screenNames;
    }

    public Optional<String> getParentScreen() {
        return parentScreen;
    }
}
