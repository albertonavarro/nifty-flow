import com.navid.nifty.flow.InstanceResolutor;
import com.navid.nifty.flow.ScreenGenerator;
import com.navid.nifty.flow.resolutors.InstanceResolutionException;
import de.lessvoid.nifty.screen.ScreenController;

/**
 * Created by alberto on 08/07/15.
 */
public class ClassInstanceResolutor implements InstanceResolutor {
    @Override
    public ScreenGenerator resolveScreenGenerator(String interfaceConstructor) throws InstanceResolutionException {
        try {
            return (ScreenGenerator) Class.forName(interfaceConstructor).newInstance();
        } catch (InstantiationException e) {
            throw new InstanceResolutionException(e);
        } catch (IllegalAccessException e) {
            throw new InstanceResolutionException(e);
        } catch (ClassNotFoundException e) {
            throw new InstanceResolutionException(e);
        }
    }

    @Override
    public ScreenController resolveScreenControler(String controller) throws InstanceResolutionException {
        try {
            return (ScreenController) Class.forName(controller).newInstance();
        } catch (InstantiationException e) {
            throw new InstanceResolutionException(e);
        } catch (IllegalAccessException e) {
            throw new InstanceResolutionException(e);
        } catch (ClassNotFoundException e) {
            throw new InstanceResolutionException(e);
        }
    }
}
