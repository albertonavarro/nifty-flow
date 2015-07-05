package com.navid.nifty.flow.domain;

import com.navid.nifty.flow.ScreenGenerator;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Created by alberto on 12/05/15.
 */
public class Screen {

    private Flow parent;

    private String name;

    private final ScreenGenerator interfaceConstructor;

    private final ScreenController controller;

    public Screen(ScreenGenerator interfaceConstructor, ScreenController controller, Flow parent, String name) {
        this.interfaceConstructor = interfaceConstructor;
        this.controller = controller;
        this.parent = parent;
        this.name = name;
    }

    public Flow getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public String getUniqueScreenName() {
        return new ScreenId(parent.getName(), name).getUniqueName();
    }


    public ScreenGenerator getInterfaceConstructor() {
        return interfaceConstructor;
    }

    public ScreenController getController() {
        return controller;
    }
}
