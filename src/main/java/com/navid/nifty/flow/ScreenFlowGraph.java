package com.navid.nifty.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ScreenFlowGraph {

    private ScreenFlowGraph parent = null;
    private final List<ScreenFlowUnit> modScreenConfigurations = new ArrayList<ScreenFlowUnit>();
    private final Map<String, ScreenFlowUnit> mapModScreenConfigurations = new HashMap<String, ScreenFlowUnit>();

    public void addScreen(ScreenFlowUnit currentScreenConfig) {
        modScreenConfigurations.add(currentScreenConfig);
        mapModScreenConfigurations.put(currentScreenConfig.getScreenName(), currentScreenConfig);

    }

    public String getStartScreenName() {
        return modScreenConfigurations.get(0).getScreenName();
    }

    public ScreenFlowUnit getScreenConfiguration(String currentScreen) {
        return mapModScreenConfigurations.get(currentScreen);
    }

    public String getNextScreenName(ScreenFlowState screenFlowState) {

        if ("next".equals(screenFlowState.getScreenCommand())) {
            if (screenFlowState.getCurrentScreen() == null) {
                return modScreenConfigurations.get(0).getScreenName();
            } else {
                return modScreenConfigurations.get(
                        modScreenConfigurations.indexOf(
                                mapModScreenConfigurations.get(screenFlowState.getCurrentScreen())) + 1).getScreenName();
            }
        } else if ("back".equals(screenFlowState.getScreenCommand())) {
            if (screenFlowState.getCurrentScreen() == null) {
                return modScreenConfigurations.get(0).getScreenName();
            } else if (modScreenConfigurations.indexOf(
                    mapModScreenConfigurations.get(screenFlowState.getCurrentScreen())) == 0) {
                screenFlowState.setCurrentFlow("root");
                return parent.getStartScreenName();
            } else {
                return modScreenConfigurations.get(
                        modScreenConfigurations.indexOf(
                                mapModScreenConfigurations.get(screenFlowState.getCurrentScreen())) - 1).getScreenName();
            }
        } else if (screenFlowState.getScreenCommand() == null) {
            return modScreenConfigurations.get(
                    modScreenConfigurations.indexOf(
                            mapModScreenConfigurations.get(screenFlowState.getScreenCommand())) + 1).getScreenName();

        } else {
            return modScreenConfigurations.get(
                    modScreenConfigurations.indexOf(
                            mapModScreenConfigurations.get(screenFlowState.getScreenCommand()))).getScreenName();

        }
    }

    void addParentFlow(ScreenFlowGraph parent) {
        this.parent = parent;
    }

}
