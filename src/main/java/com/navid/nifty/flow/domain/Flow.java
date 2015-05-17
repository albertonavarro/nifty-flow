package com.navid.nifty.flow.domain;

import java.util.HashMap;
import java.util.List;

/**
 * Created by alberto on 12/05/15.
 */
public class Flow {

    private final String name;

    private final HashMap<String, Screen> screenNames = new HashMap<String, Screen>();

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

    public Screen getScreen(String screenName) {
        return screenNames.get(screenName);
    }

    public void setScreens(List<Screen> screens) {
        this.screens = screens;
        for(Screen screen : screens) {
            if (screenNames.put(screen.getName(), screen) != null) {
                throw new IllegalArgumentException("Screen name repeated within the same flow");
            }
        }
    }
}
