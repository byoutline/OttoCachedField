package com.byoutline.ottocachedfield;

import com.byoutline.cachedfield.ProviderWithArg;
import com.byoutline.cachedfield.cachedendpoint.CachedEndpointWithArg;
import com.byoutline.cachedfield.cachedendpoint.StateAndValue;
import com.byoutline.ibuscachedfield.IBusCachedEndpointWithArgBuilder;
import com.byoutline.ibuscachedfield.builders.CachedEndpointWithArgConstructorWrapperBuilder;
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
        extends IBusCachedEndpointWithArgBuilder<RETURN_TYPE, ARG_TYPE, Bus> {

    protected OttoCachedEndpointWithArgBuilder() {
        super(new ConstructorWrapper<RETURN_TYPE, ARG_TYPE>(), OttoCachedField.defaultBus,
                OttoCachedField.defaultSessionIdProvider,
                OttoCachedField.defaultValueGetterExecutor,
                OttoCachedField.defaultStateListenerExecutor);
    }

    private static class ConstructorWrapper<RETURN_TYPE, ARG_TYPE> implements CachedEndpointWithArgConstructorWrapperBuilder<RETURN_TYPE, ARG_TYPE, Bus> {
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
