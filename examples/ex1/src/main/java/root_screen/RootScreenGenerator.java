package root_screen;

import com.navid.nifty.flow.ScreenFlowManager;
import com.navid.nifty.flow.ScreenGenerator;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.tools.Color;

public final class RootScreenGenerator implements ScreenGenerator {

    private final Nifty nifty;
    private final RootScreenController startScreenController;
    private final ScreenFlowManager screenFlowManager;

    public RootScreenGenerator(Nifty nifty, RootScreenController startScreenController, ScreenFlowManager screenFlowManager) {
        this.nifty = nifty;
        this.startScreenController = startScreenController;
        this.screenFlowManager = screenFlowManager;
    }

    @Override
    public void buildScreen(String uniqueScreenId, String controllerClassName) {

        final PanelBuilder panelModules = new PanelBuilder("Panel_Modules") {
            {
                childLayoutVertical();
                alignCenter();
                height("80%");
            }
        };

        for (final String moduleName : screenFlowManager.getChildren()) {
            panelModules.control(new ButtonBuilder(moduleName + "ButtonLabel", moduleName) {
                {
                    alignCenter();
                    valignCenter();
                    height("11%");
                    width("20%");
                    interactOnClick("executeModule(" + moduleName + ")");
                }
            });
        }

        final PanelBuilder panelMessage = new PanelBuilder("Panel_Message") {
            {
                childLayoutVertical();
                alignCenter();
                height("10%");
            }
        };

        final PanelBuilder panelQuit = new PanelBuilder("Panel_Quit") {
            {
                childLayoutVertical();
                height("10%");
            }
        };

        panelQuit.control(new ButtonBuilder("QuitButton", "Quit") {
            {
                alignRight();
                valignCenter();
                height("100%");
                width("20%");
                interactOnClick("quit()");
            }
        });

        new ScreenBuilder(uniqueScreenId) {
            {
                controller(startScreenController);
                layer(new LayerBuilder("Layer_Main") {
                    {
                        backgroundColor(Color.BLACK);
                        childLayoutVertical();
                        panel(panelModules);
                        panel(panelMessage);
                        panel(panelQuit);
                    }
                });
            }
        }.build(nifty);

    }

}
