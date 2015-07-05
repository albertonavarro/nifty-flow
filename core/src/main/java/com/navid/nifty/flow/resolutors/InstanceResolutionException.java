package com.navid.nifty.flow.resolutors;

import java.net.URISyntaxException;

/**
 * Created by alberto on 6/6/15.
 */
public class InstanceResolutionException extends Exception {
    public InstanceResolutionException(String s, Exception e) {
        super(s,e);
    }

    public InstanceResolutionException(String s) {
        super(s);
    }
}
