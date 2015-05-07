package com.navid.nifty.flow;

import de.lessvoid.nifty.screen.ScreenController;

final class ScreenFlowUnit {

    private final String screenName;
    private final ScreenGenerator interfaceConstructor;
    private final ScreenController controller;

    ScreenFlowUnit(String screenName, ScreenGenerator interfaceConstructor, ScreenController controller) {
        this.screenName = screenName;
        this.interfaceConstructor = interfaceConstructor;
        this.controller = controller;
    }

    /**
     * @return the screenName
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * @return the interfaceConstructor
     */
    public ScreenGenerator getInterfaceConstructor() {
        return interfaceConstructor;
    }

    /**
     * @return the controller
     */
    public ScreenController getController() {
        return controller;
    }

}
