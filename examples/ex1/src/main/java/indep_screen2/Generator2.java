package indep_screen2;

import com.navid.nifty.flow.ScreenGenerator;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;

/**
 * Created by alberto on 08/07/15.
 */
public class Generator2 implements ScreenGenerator {

    private final Nifty nifty;

    private final ScreenController screenController;

    private int counter = 0;

    public Generator2(Nifty nifty, ScreenController screenController) {
        this.nifty = nifty;
        this.screenController = screenController;
    }

    @Override
    public void buildScreen(String uniqueScreenId, String controllerClassName) {
        if (nifty.getScreen(uniqueScreenId) != null) {
            nifty.removeScreen(uniqueScreenId);
        }
        buildScreenNow(uniqueScreenId);
    }

    public void buildScreenNow(String uniqueScreenId) {

        new ScreenBuilder(uniqueScreenId) {
            {
                controller(screenController); // Screen properties

                // <layer>
                layer(new LayerBuilder("Layer_ID") {
                    {
                        childLayoutVertical(); // layer properties, add more...

                        text(new TextBuilder("text") {
                            {
                                text("Screen made " + ++counter + " times from Generator2 with name " + uniqueScreenId);
                                style("nifty-label");
                                alignCenter();
                                valignCenter();
                                height("20%");
                            }
                        });

                        panel(new PanelBuilder("Panel_ID") {
                            {
                                height("20%");
                                childLayoutHorizontal();

                                control(new ButtonBuilder("PreviousButton", "Back") {
                                    {
                                        alignCenter();
                                        valignCenter();
                                        interactOnClick("back()");
                                    }
                                });

                                control(new ButtonBuilder("NextButton", "Next") {
                                    {
                                        alignCenter();
                                        valignCenter();
                                        interactOnClick("next()");
                                    }
                                });



                            }
                        });

                    }
                });
                // </layer>
            }
        }.build(nifty);
    }
}
