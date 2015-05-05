package com.navid.nifty.flow;

public final class ScreenConfiguration {
    private String screenName;
    private String controller;
    private String interfaceConstructor;

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getInterfaceConstructor() {
        return interfaceConstructor;
    }

    public void setInterfaceConstructor(String interfaceConstructor) {
        this.interfaceConstructor = interfaceConstructor;
    }
}
