package com.navid.nifty.flow;

final class ScreenFlowState {

    private String currentFlow;

    private String currentScreen;

    private String screenCommand;

    /**
     * @return the currentScreen
     */
    public String getCurrentScreen() {
        return currentScreen;
    }

    /**
     * @param currentScreen the currentScreen to set
     */
    public void setCurrentScreen(String currentScreen) {
        this.currentScreen = currentScreen;
    }

    /**
     * @return the screenCommand
     */
    public String getScreenCommand() {
        return screenCommand;
    }

    /**
     * @param screenCommand the screenCommand to set
     */
    public void setScreenCommand(String screenCommand) {
        this.screenCommand = screenCommand;
    }

    /**
     * @return the currentFlow
     */
    public String getCurrentFlow() {
        return currentFlow;
    }

    /**
     * @param currentFlow the currentFlow to set
     */
    public void setCurrentFlow(String currentFlow) {
        this.currentFlow = currentFlow;
    }

}
