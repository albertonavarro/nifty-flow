package com.navid.nifty.flow.domain;

/**
 * This class represents a screen identity uniquely among the graph.
 */
public class ScreenId {

    private final String flowName;

    private final String screenName;

    public ScreenId(String flowName, String screenName) {
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

    public static ScreenId fromString(String uniqueId) {
        return new ScreenId(uniqueId.split(":")[0], uniqueId.split(":")[1]);
    }

    @Override
    public int hashCode() {
        return getUniqueName().hashCode();
    }

    @Override
    public boolean equals( Object o ) {
        if (! (o instanceof ScreenId)) {
            return false;
        }

        ScreenId other = (ScreenId) o;

        return other.getUniqueName().equals(getUniqueName());
    }
}
