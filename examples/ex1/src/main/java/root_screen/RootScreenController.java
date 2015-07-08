/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package root_screen;

import com.jme3.app.Application;
import com.navid.nifty.flow.ScreenFlowManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

public final class RootScreenController implements ScreenController {

    private Application application;

    private ScreenFlowManager screenFlowManager;

    /*
     From Bind
     */
    private Nifty nifty;
    /*
     From Bind
     */
    private Screen screen;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
    }

    @Override
    public void onEndScreen() {
    }

    public void executeModule(String moduleName) {
        screenFlowManager.setNextScreenHint(moduleName);
        nifty.gotoScreen("redirector");
    }

    public void quit() {
        application.stop(false);
        System.exit(0);
    }

    /**
     * @param screenFlowManager the screenFlowManager to set
     */
    public RootScreenController setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
        return this;
    }

    /**
     * @param application the application to set
     */
    public RootScreenController setApplication(Application application) {
        this.application = application;
        return this;
    }

}
