package com.byoutline.ottocachedfield.internal;

import com.byoutline.cachedfield.SuccessListener;
import com.byoutline.eventcallback.ResponseEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Produce;

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoSuccessListener<T> implements SuccessListener<T> {

    private final Bus bus;
    private final ResponseEvent<T> responseEvent;

    public OttoSuccessListener(Bus bus, ResponseEvent<T> responseEvent) {
        this.bus = bus;
        this.responseEvent = responseEvent;
        bus.register(this);
    }

    @Override
    public void valueLoaded(T t) {
        responseEvent.setResponse(t);
        bus.post(responseEvent);
    }

    @Produce
    ResponseEvent<T> produceResponseEvent() {
        return responseEvent;
    }
}
