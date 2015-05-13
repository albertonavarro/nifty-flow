package com.navid.nifty.flow.dto;

/**
 * This class represents the static configuration to be loaded and instantiated
 */
public final class ScreenDefinition {
    private final String screenName;
    private final String controller;
    private final String interfaceConstructor;

    public ScreenDefinition(String screenName, String controller, String interfaceConstructor) {
        this.screenName = screenName;
        this.controller = controller;
        this.interfaceConstructor = interfaceConstructor;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getController() {
        return controller;
    }

    public String getInterfaceConstructor() {
        return interfaceConstructor;
    }
}
