package com.byoutline.ottocachedfield.internal;

import com.byoutline.cachedfield.ErrorListener;
import com.squareup.otto.Bus;

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoErrorListener implements ErrorListener {

    private final Bus bus;
    private final Object event;

    public OttoErrorListener(Bus bus, Object event) {
        this.bus = bus;
        this.event = event;
    }

    @Override
    public void valueLoadingFailed(Exception excptn) {
        if (event != null) {
            bus.post(event);
        }
    }
}
