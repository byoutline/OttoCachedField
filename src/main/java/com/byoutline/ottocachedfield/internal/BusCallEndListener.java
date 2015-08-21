package com.byoutline.ottocachedfield.internal;

import com.byoutline.cachedfield.cachedendpoint.CallEndListener;
import com.byoutline.cachedfield.cachedendpoint.StateAndValue;
import com.byoutline.eventcallback.IBus;
import com.byoutline.ibuscachedfield.events.ResponseEventWithArg;

/**
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class BusCallEndListener<RETURN_TYPE, ARG_TYPE> implements CallEndListener<RETURN_TYPE, ARG_TYPE> {
    private final IBus bus;
    private final ResponseEventWithArg<StateAndValue<RETURN_TYPE, ARG_TYPE>, ARG_TYPE> event;

    public BusCallEndListener(IBus bus, ResponseEventWithArg<StateAndValue<RETURN_TYPE, ARG_TYPE>, ARG_TYPE> event) {
        this.bus = bus;
        this.event = event;
    }

    @Override
    public void callEnded(StateAndValue<RETURN_TYPE, ARG_TYPE> stateAndValue) {
        event.setResponse(stateAndValue, stateAndValue.getArg());
        bus.post(event);
    }
}
