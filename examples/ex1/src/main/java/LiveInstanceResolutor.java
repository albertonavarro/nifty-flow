import com.navid.nifty.flow.InstanceResolutor;
import com.navid.nifty.flow.ScreenGenerator;
import com.navid.nifty.flow.resolutors.InstanceResolutionException;
import de.lessvoid.nifty.screen.ScreenController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alberto on 08/07/15.
 */
public class LiveInstanceResolutor implements InstanceResolutor {

    private final Map<String, ScreenGenerator> generators = new HashMap<>();
    private final Map<String, ScreenController> controllers = new HashMap<>();

    public void addGenerator(String name, ScreenGenerator sg) {
        generators.put(name, sg);
    }

    public void addController(String name, ScreenController sc) {
        controllers.put(name, sc);
    }

    @Override
    public ScreenGenerator resolveScreenGenerator(String interfaceConstructor) throws InstanceResolutionException {
        return generators.get(interfaceConstructor);
    }

    @Override
    public ScreenController resolveScreenControler(String controller) throws InstanceResolutionException {
        return controllers.get(controller);
    }
}
