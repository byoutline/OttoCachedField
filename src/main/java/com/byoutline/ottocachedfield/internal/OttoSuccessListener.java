package com.byoutline.ottocachedfield.internal;

import com.byoutline.cachedfield.SuccessListener;
import com.byoutline.eventcallback.ResponseEvent;
import com.squareup.otto.Bus;

/**
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public final class OttoSuccessListener<RETURN_TYPE> implements SuccessListener<RETURN_TYPE> {

    private final Bus bus;
    private final ResponseEvent<RETURN_TYPE> responseEvent;

    public OttoSuccessListener(Bus bus, ResponseEvent<RETURN_TYPE> responseEvent) {
        this.bus = bus;
        this.responseEvent = responseEvent;
    }

    @Override
    public void valueLoaded(RETURN_TYPE t) {
        responseEvent.setResponse(t);
        bus.post(responseEvent);
    }
}
