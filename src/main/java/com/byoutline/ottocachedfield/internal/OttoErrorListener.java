package com.byoutline.ottocachedfield.internal;

import com.byoutline.cachedfield.ErrorListener;
import com.squareup.otto.Bus;

import javax.annotation.Nonnull;

/**
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoErrorListener implements ErrorListener {

    private final Bus bus;
    private final ErrorEvent event;

    public OttoErrorListener(@Nonnull Bus bus, @Nonnull ErrorEvent event) {
        this.bus = bus;
        this.event = event;
    }

    @Override
    public void valueLoadingFailed(Exception excptn) {
        event.post(bus, excptn);
    }
}