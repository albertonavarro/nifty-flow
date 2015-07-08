package indep_screen3;

import com.navid.nifty.flow.ScreenFlowManager;
import com.navid.nifty.flow.ScreenFlowManagerImpl;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Created by alberto on 08/07/15.
 */
public class Controller3 implements ScreenController {

    private final ScreenFlowManager screenFlowManager;

    private Nifty nifty;

    public Controller3(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
    }

    @Override
    public void onStartScreen() {

    }

    @Override
    public void onEndScreen() {

    }

    public void back() {
        screenFlowManager.setNextScreenHint(ScreenFlowManagerImpl.PREV);
        nifty.gotoScreen("redirector");
    }

    public void next() {
        screenFlowManager.setNextScreenHint(ScreenFlowManagerImpl.NEXT);
        nifty.gotoScreen("redirector");
    }
}
