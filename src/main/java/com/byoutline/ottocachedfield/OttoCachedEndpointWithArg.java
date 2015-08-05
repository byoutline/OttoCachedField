package com.byoutline.ottocachedfield;

import com.byoutline.cachedfield.ProviderWithArg;
import com.byoutline.cachedfield.cachedendpoint.CachedEndpointWithArgImpl;
import com.byoutline.cachedfield.cachedendpoint.StateAndValue;
import com.byoutline.eventcallback.IBus;
import com.byoutline.ibuscachedfield.events.ResponseEventWithArg;
import com.byoutline.ottocachedfield.internal.BusCallEndListener;

import javax.annotation.Nonnull;
import javax.inject.Provider;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Apache License 2.0
 *
 * @param <RETURN_TYPE>
 * @param <ARG_TYPE>
 * @author Sebastian Kacprzak <nait at naitbit.com>
 */
public class OttoCachedEndpointWithArg<RETURN_TYPE, ARG_TYPE> extends CachedEndpointWithArgImpl<RETURN_TYPE, ARG_TYPE> {

    OttoCachedEndpointWithArg(@Nonnull Provider<String> sessionProvider,
                              @Nonnull ProviderWithArg<RETURN_TYPE, ARG_TYPE> valueGetter,
                              @Nonnull ResponseEventWithArg<StateAndValue<RETURN_TYPE, ARG_TYPE>, ARG_TYPE> event,
                              @Nonnull IBus bus,
                              @Nonnull ExecutorService valueGetterExecutor,
                              @Nonnull Executor stateListenerExecutor) {
        super(sessionProvider, valueGetter,
                new BusCallEndListener<RETURN_TYPE, ARG_TYPE>(bus, event),
                valueGetterExecutor, stateListenerExecutor);
    }

}