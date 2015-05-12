package com.navid.nifty.flow.domain;

import java.util.List;

/**
 * Created by alberto on 12/05/15.
 */
public class Flow {

    private final String name;

    private List<Screen> screens;

    public Flow(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Screen> getScreens() {
        return screens;
    }

    public void setScreens(List<Screen> screens) {
        this.screens = screens;
    }
}
