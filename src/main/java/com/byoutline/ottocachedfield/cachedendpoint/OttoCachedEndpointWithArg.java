package com.byoutline.ottocachedfield.cachedendpoint;

import com.byoutline.cachedfield.ProviderWithArg;
import com.byoutline.cachedfield.cachedendpoint.CachedEndpointWithArgImpl;
import com.byoutline.cachedfield.cachedendpoint.CallEndListener;
import com.byoutline.cachedfield.cachedendpoint.StateAndValue;
import com.byoutline.ibuscachedfield.events.ResponseEventWithArg;
import com.byoutline.ottocachedfield.OttoCachedField;

import javax.annotation.Nonnull;

/**
 * Apache License 2.0
 *
 * @param <RETURN_TYPE>
 * @param <ARG_TYPE>
 * @author Sebastian Kacprzak <nait at naitbit.com>
 */
public class OttoCachedEndpointWithArg<RETURN_TYPE, ARG_TYPE> extends CachedEndpointWithArgImpl<RETURN_TYPE, ARG_TYPE> {

    public OttoCachedEndpointWithArg(
            @Nonnull ProviderWithArg<RETURN_TYPE, ARG_TYPE> valueGetter,
            @Nonnull final ResponseEventWithArg<StateAndValue<RETURN_TYPE, ARG_TYPE>, ARG_TYPE> event) {
        // TODO use instance values instead of defaults.
        super(OttoCachedField.defaultSessionIdProvider, valueGetter,
                new CallEndListener<RETURN_TYPE, ARG_TYPE>() {
                    @Override
                    public void callEnded(StateAndValue<RETURN_TYPE, ARG_TYPE> stateAndValue) {
                        event.setResponse(stateAndValue, stateAndValue.getArg());
                        OttoCachedField.defaultBus.post(event);
                    }
                },
                OttoCachedField.defaultValueGetterExecutor, OttoCachedField.defaultStateListenerExecutor);
    }
}