package com.byoutline.ottocachedfield.internal;

import com.byoutline.eventcallback.IBus;
import com.byoutline.eventcallback.ResponseEvent;
import com.squareup.otto.Bus;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class ErrorEvent {

    private final ResponseEvent<Exception> responseEvent;
    private final Object genericEvent;

    public ErrorEvent(@Nullable ResponseEvent<Exception> responseEvent,
            @Nullable Object genericEvent) {
        this.responseEvent = responseEvent;
        this.genericEvent = genericEvent;
    }

    public static ErrorEvent responseEvent(@Nonnull ResponseEvent<Exception> event) {
        return new ErrorEvent(event, null);
    }

    public static ErrorEvent genericEvent(@Nonnull Object event) {
        return new ErrorEvent(null, event);
    }

    public void post(@Nonnull IBus bus, Exception ex) {
        if (responseEvent != null) {
            responseEvent.setResponse(ex);
            bus.post(responseEvent);
        }
        if (genericEvent != null) {
            bus.post(genericEvent);
        }
    }
}
