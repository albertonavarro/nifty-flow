package com.navid.nifty.flow.resolutors;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.navid.nifty.flow.InstanceResolutor;
import com.navid.nifty.flow.ScreenGenerator;
import com.navid.nifty.flow.template.ftl.StaticScreenGenerator;
import de.lessvoid.nifty.screen.ScreenController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

/**
 * Created by alberto on 6/6/15.
 */
public class DefaultInstanceResolutor implements InstanceResolutor {

    private static final Logger log = LoggerFactory.getLogger(DefaultInstanceResolutor.class);

    private Multimap<String, InstanceResolutor> resolutorMultimap = HashMultimap.create();

    public void addResolutor(String schema, InstanceResolutor resolutor) {
        resolutorMultimap.put(schema, resolutor);
    }

    @Override
    public ScreenGenerator resolveScreenGenerator(String interfaceConstructor) throws InstanceResolutionException {
        try {
            URI uri = new URI(interfaceConstructor);
            Collection<InstanceResolutor> instanceResolutors = resolutorMultimap.get(uri.getScheme());

            for(InstanceResolutor instanceResolutor : instanceResolutors) {
                try {
                    ScreenGenerator constructor = instanceResolutor.resolveScreenGenerator(interfaceConstructor);
                    if ( constructor != null) {
                        log.info("ScreenGenerator {} found in instanceResolutor {}", interfaceConstructor, instanceResolutor);
                        return constructor;
                    }
                } catch(InstanceResolutionException e){
                    log.debug("ScreenController {} not found in instanceResolutor {}", interfaceConstructor, instanceResolutor);
                }
            }
            throw new IllegalArgumentException("Unknown constructor " + interfaceConstructor);
        } catch (URISyntaxException e) {
            throw new InstanceResolutionException("Error solving instance as format scheme:value", e);
        }
    }

    @Override
    public ScreenController resolveScreenControler(String controller) throws InstanceResolutionException {
        try {
            URI uri = new URI(controller);
            Collection<InstanceResolutor> instanceResolutors = resolutorMultimap.get(uri.getScheme());

            for(InstanceResolutor instanceResolutor : instanceResolutors) {
                try {
                    ScreenController constructor = instanceResolutor.resolveScreenControler(controller);
                    if ( constructor != null) {
                        log.info("ScreenController {} found in instanceResolutor {}", controller, instanceResolutor);
                        return constructor;
                    }
                } catch(InstanceResolutionException e){
                    log.debug("ScreenController {} not found in instanceResolutor {}", controller, instanceResolutor);
                }
            }
            throw new IllegalArgumentException("Unknown controller " + controller);
        } catch (URISyntaxException e) {
            throw new InstanceResolutionException("Error solving instance as format scheme:value", e);
        }
    }

    public static DefaultInstanceResolutor createDefaultInstanceResolutor() {
        DefaultInstanceResolutor defaultInstanceResolutor = new DefaultInstanceResolutor();

        return defaultInstanceResolutor;
    }
}
