package com.byoutline.ottocachedfield.internal;

import com.byoutline.eventcallback.ResponseEvent;
import com.byoutline.eventcallback.ResponseEventImpl;

/**
 * Indicates that value loaded correctly. This {@link ResponseEvent} is reused
 * for all successful value loads.
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class SuccessEvent<T> extends ResponseEventImpl<T> implements CFResultEvent {

    private final long eventId;

    public SuccessEvent(long eventId) {
        this.eventId = eventId;
    }

    @Override
    public long getEventId() {
        return eventId;
    }
}
