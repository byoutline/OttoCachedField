package com.byoutline.ottocachedfield.internal;

import com.byoutline.cachedfield.SuccessListenerWithArg;
import com.byoutline.ottocachedfield.ResponseEventWithArg;
import com.squareup.otto.Bus;

/**
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public final class OttoSuccessListenerWithArg<RETURN_TYPE, ARG_TYPE> implements SuccessListenerWithArg<RETURN_TYPE, ARG_TYPE> {

    private final Bus bus;
    private final ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> responseEvent;

    public OttoSuccessListenerWithArg(Bus bus, ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> responseEvent) {
        this.bus = bus;
        this.responseEvent = responseEvent;
    }

    @Override
    public void valueLoaded(RETURN_TYPE value, ARG_TYPE arg) {
        responseEvent.setResponse(value, arg);
        bus.post(responseEvent);
    }
    
}
