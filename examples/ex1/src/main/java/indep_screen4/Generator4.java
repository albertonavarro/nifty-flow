package indep_screen4;

import com.navid.nifty.flow.ScreenGenerator;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.nifty.tools.Color;

/**
 * Created by alberto on 08/07/15.
 */
public class Generator4 implements ScreenGenerator {

    private final Nifty nifty;

    private final ScreenController screenController;

    public Generator4(Nifty nifty, ScreenController screenController) {
        this.nifty = nifty;
        this.screenController = screenController;
    }

    @Override
    public void buildScreen(String uniqueScreenId) {
        if (nifty.getScreen(uniqueScreenId) == null) {
            buildScreenNow(uniqueScreenId);
        }
    }

    public void buildScreenNow(String uniqueScreenId) {

        new ScreenBuilder(uniqueScreenId) {
            {
                controller(screenController); // Screen properties

                // <layer>
                layer(new LayerBuilder("Layer_ID") {
                    {
                        childLayoutVertical(); // layer properties, add more...

                        panel(new PanelBuilder("Panel_ID") {
                            {
                                height("20%");
                                childLayoutHorizontal();

                                text(new TextBuilder("text") {
                                    {
                                        text("Screen made one time only from Generator4 with name " + uniqueScreenId);
                                        style("nifty-label");
                                        alignCenter();
                                        valignCenter();
                                        height("20%");
                                    }});

                                control(new ButtonBuilder("PreviousButton", "Back") {
                                    {
                                        alignCenter();
                                        valignCenter();
                                        interactOnClick("back()");
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
