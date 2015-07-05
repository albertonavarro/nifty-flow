package com.navid.nifty.flow;


import com.navid.nifty.flow.resolutors.InstanceResolutionException;
import de.lessvoid.nifty.screen.ScreenController;

public interface InstanceResolutor {

    ScreenGenerator resolveScreenGenerator(String interfaceConstructor) throws InstanceResolutionException;

    ScreenController resolveScreenControler(String controller) throws InstanceResolutionException;
}
