package com.byoutline.ottocachedfield.internal;

import com.byoutline.eventcallback.ResponseEventImpl;
import retrofit.RetrofitError;

/**
 * Indicates that loading of the value failed.
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class ErrorEvent extends ResponseEventImpl<RetrofitError> implements CFResultEvent {

    private final long eventId;

    public ErrorEvent(long eventId) {
        this.eventId = eventId;
    }

    @Override
    public long getEventId() {
        return eventId;
    }
}
