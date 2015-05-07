package com.navid.nifty.flow;


import de.lessvoid.nifty.screen.ScreenController;

public interface InstanceResolutor {

    ScreenGenerator resolveScreenGenerator(String interfaceConstructor);

    ScreenController resolveScreenControler(String controller);
}
