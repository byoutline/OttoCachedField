package com.byoutline.ottocachedfield;

import com.byoutline.cachedfield.ProviderWithArg;
import com.byoutline.cachedfield.cachedendpoint.CachedEndpointWithArg;
import com.byoutline.cachedfield.cachedendpoint.StateAndValue;
import com.byoutline.ibuscachedfield.IBusCachedEndpointWithArgBuilder;
import com.byoutline.ibuscachedfield.builders.CachedEndpointWithArgConstructorWrapper;
import com.byoutline.ibuscachedfield.events.ResponseEventWithArg;
import com.byoutline.ottoeventcallback.OttoIBus;
import com.squareup.otto.Bus;

import javax.inject.Provider;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Fluent interface for building instances of {@link OttoCachedEndpointWithArg}.
 */
public class OttoCachedEndpointWithArgBuilder<RETURN_TYPE, ARG_TYPE>
        extends IBusCachedEndpointWithArgBuilder<RETURN_TYPE, ARG_TYPE, Bus, CachedEndpointWithArg<RETURN_TYPE, ARG_TYPE>> {

    protected OttoCachedEndpointWithArgBuilder() {
        super(new ConstructorWrapper<RETURN_TYPE, ARG_TYPE>(), OttoCachedField.defaultBus,
                OttoCachedField.defaultSessionIdProvider,
                OttoCachedField.defaultValueGetterExecutor,
                OttoCachedField.defaultStateListenerExecutor);
    }

    private static class ConstructorWrapper<RETURN_TYPE, ARG_TYPE>
            implements CachedEndpointWithArgConstructorWrapper<RETURN_TYPE, ARG_TYPE, Bus, CachedEndpointWithArg<RETURN_TYPE, ARG_TYPE>> {
        @Override
        public CachedEndpointWithArg<RETURN_TYPE, ARG_TYPE> build(Provider<String> sessionIdProvider,
                                                                  ProviderWithArg<RETURN_TYPE, ARG_TYPE> valueGetter,
                                                                  ResponseEventWithArg<StateAndValue<RETURN_TYPE, ARG_TYPE>, ARG_TYPE> resultEvent,
                                                                  Bus bus,
                                                                  ExecutorService valueGetterExecutor,
                                                                  Executor stateListenerExecutor) {
            return new OttoCachedEndpointWithArg<RETURN_TYPE, ARG_TYPE>(sessionIdProvider, valueGetter,
                    resultEvent, new OttoIBus(bus),
                    valueGetterExecutor, stateListenerExecutor);
        }
    }
}
