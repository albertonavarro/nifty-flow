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
        String[] parts = uniqueId.split(":");
        if(parts.length != 2) {
            throw new IllegalArgumentException("Wrong format for screen unique id, it should be flow:screenId, and it was " + uniqueId);
        }
        return new ScreenId(parts[0], parts[1]);
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
