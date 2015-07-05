package com.navid.nifty.flow.template.ftl;

import com.navid.nifty.flow.InstanceResolutor;
import com.navid.nifty.flow.ScreenGenerator;
import com.navid.nifty.flow.resolutors.InstanceResolutionException;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.ScreenController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by alberto on 6/7/15.
 */
public class StaticScreenGeneratorResolutor implements InstanceResolutor {

    private final Nifty nifty;

    public StaticScreenGeneratorResolutor(Nifty nifty) {
        this.nifty = nifty;
    }

    @Override
    public ScreenGenerator resolveScreenGenerator(String interfaceConstructor) throws InstanceResolutionException {
        try {
            URI uri = new URI(interfaceConstructor);
            return new StaticScreenGenerator(nifty, uri.getSchemeSpecificPart());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Wrong format, it should be spring:beanName, found :" + interfaceConstructor, e);
        } catch (IOException e) {
            throw new InstanceResolutionException("Error finding screen generator"+interfaceConstructor, e);
        }
    }

    @Override
    public ScreenController resolveScreenControler(String controller) throws InstanceResolutionException {
        throw new InstanceResolutionException("ScreenController with this schema is not supported");
    }
}
