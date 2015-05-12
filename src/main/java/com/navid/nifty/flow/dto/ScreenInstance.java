package com.navid.nifty.flow.dto;

/**
 * Created by alberto on 12/05/15.
 */
public class ScreenInstance {

    private final String flowName;

    private final String screenName;

    public ScreenInstance(String flowName, String screenName) {
        this.flowName = flowName;
        this.screenName = screenName;
    }

    public String getFlowName() {
        return flowName;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getUniqueName() {
        return flowName + ":" + screenName;
    }
}
