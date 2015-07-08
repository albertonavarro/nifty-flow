import com.google.common.base.Optional;
import com.jme3.app.SimpleApplication;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.navid.nifty.flow.*;
import com.navid.nifty.flow.dto.ScreenDefinition;
import com.navid.nifty.flow.resolutors.DefaultInstanceResolutor;
import com.navid.nifty.flow.resolutors.InstanceResolutionException;
import com.navid.nifty.flow.template.ftl.StaticScreenGeneratorResolutor;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.screen.ScreenController;
import indep_screen1.Controller1;
import indep_screen2.Controller2;
import indep_screen2.Generator2;
import indep_screen3.Controller3;
import indep_screen4.Controller4;
import indep_screen4.Generator4;
import root_screen.RootScreenGenerator;
import root_screen.RootScreenController;

import static com.google.common.base.Optional.of;
import static com.google.common.collect.Lists.newArrayList;

/**
 * @author anavarro
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);
        flyCam.setDragToRotate(true);

        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");

        /* ------------------ NEW CODE ----------------- */
        DefaultInstanceResolutor defaultInstanceResolutor = new DefaultInstanceResolutor();
        ScreenFlowManager screenFlowManager = new ScreenFlowManagerImpl(nifty, defaultInstanceResolutor);

        //Main structures, made up resolutors depending on what you prefer to use
        LiveInstanceResolutor liveInstanceResolutor = new LiveInstanceResolutor();
        defaultInstanceResolutor.addResolutor("static", new StaticScreenGeneratorResolutor(nifty));
        defaultInstanceResolutor.addResolutor("live", liveInstanceResolutor);

        //Some examples of live instances, in case you prefer them
        RootScreenController screenController = new RootScreenController().setScreenFlowManager(screenFlowManager).setApplication(this);
        ScreenController screenController2 = new Controller2(screenFlowManager);
        ScreenController screenController4 = new Controller4(screenFlowManager);
        liveInstanceResolutor.addController("root", screenController);
        liveInstanceResolutor.addGenerator("root", new RootScreenGenerator(nifty, screenController, screenFlowManager));
        liveInstanceResolutor.addController("controller1", new Controller1(screenFlowManager));
        liveInstanceResolutor.addController("controller2", screenController2);
        liveInstanceResolutor.addController("controller3", new Controller3(screenFlowManager));
        liveInstanceResolutor.addController("controller4", new Controller4(screenFlowManager));

        liveInstanceResolutor.addGenerator("generator2", new Generator2(nifty, screenController2));
        liveInstanceResolutor.addGenerator("generator4", new Generator4(nifty, screenController4));

        try {
            //My first flow definition
            screenFlowManager.addScreenDefinition(new ScreenDefinition("root", "live:root", "live:root"));
            screenFlowManager.addScreenDefinition(new ScreenDefinition("screen1", "live:controller1", "static:/screen.xml"));
            screenFlowManager.addScreenDefinition(new ScreenDefinition("screen2", "live:controller2", "live:generator2"));
            screenFlowManager.addScreenDefinition(new ScreenDefinition("screen3", "live:controller1", "static:/screen.xml"));
            screenFlowManager.addScreenDefinition(new ScreenDefinition("screen4", "live:controller4", "live:generator4"));
            screenFlowManager.addScreenDefinition(new ScreenDefinition("screen5", "live:controller1", "static:/screen.xml"));
            screenFlowManager.addScreenDefinition(new ScreenDefinition("screen6", "live:controller4", "live:generator4"));

            screenFlowManager.addFlowDefinition("root", Optional.<String>absent(), newArrayList("root")); //parent is absent, this is root.
            screenFlowManager.addFlowDefinition("screenFlow1", of("root:root"), newArrayList("screen1", "screen2", "screen3", "screen4"));
            screenFlowManager.addFlowDefinition("screenFlow2", of("root:root"), newArrayList("screen5", "screen6"));

            //Once I finished to load flows, I'm ready to start, mandatory name, mandatory implementation
            nifty.addScreen("redirector", new ScreenBuilder("start", new RedirectorScreenController().setScreenFlowManager(screenFlowManager)).build(nifty));
            nifty.gotoScreen("redirector");

        } catch (InstanceResolutionException e) {
            e.printStackTrace();
        }


    }
}