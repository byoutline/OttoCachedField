package com.byoutline.ottocachedfield.internal;

import com.byoutline.cachedfield.ErrorListenerWithArg;
import com.byoutline.ottocachedfield.events.ResponseEventWithArg;
import com.squareup.otto.Bus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoErrorListenerWithArg<ARG_TYPE> implements ErrorListenerWithArg<ARG_TYPE> {

    private final Bus bus;
    private final ResponseEventWithArg<Exception, ARG_TYPE> event;

    public OttoErrorListenerWithArg(@Nonnull Bus bus, @Nullable ResponseEventWithArg<Exception, ARG_TYPE> event) {
        this.bus = bus;
        this.event = event;
    }

    @Override
    public void valueLoadingFailed(Exception ex, ARG_TYPE arg) {
        if(event != null) {
            event.setResponse(ex, arg);
            bus.post(event);
        }
    }
}
