package com.navid.nifty.flow;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public final class RedirectorScreenController implements ScreenController {

    private ScreenFlowManager screenFlowManager;

    private Nifty nifty;

    private Screen screen;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        nifty.gotoScreen(screenFlowManager.nextScreen());
    }

    @Override
    public void onEndScreen() {
    }

    /**
     * @param screenFlowManager the screenFlowManager to set
     */
    public void setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
    }

}
