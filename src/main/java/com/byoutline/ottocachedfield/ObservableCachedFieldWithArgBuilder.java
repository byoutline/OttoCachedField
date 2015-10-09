package com.byoutline.ottocachedfield;

import com.byoutline.cachedfield.CachedFieldWithArg;
import com.byoutline.cachedfield.ProviderWithArg;
import com.byoutline.eventcallback.IBus;
import com.byoutline.ibuscachedfield.IBusCachedEndpointWithArgBuilder;
import com.byoutline.ibuscachedfield.IBusCachedFieldWithArgBuilder;
import com.byoutline.ibuscachedfield.builders.CachedFieldWithArgConstructorWrapper;
import com.byoutline.ibuscachedfield.events.ResponseEventWithArg;
import com.byoutline.ibuscachedfield.internal.IBusErrorListenerWithArg;
import com.byoutline.ibuscachedfield.internal.IBusSuccessListenerWithArg;
import com.byoutline.ottoeventcallback.OttoIBus;
import com.squareup.otto.Bus;

import javax.inject.Provider;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Fluent interface builder of {@link ObservableCachedFieldWithArg}.
 *
 * @param <RETURN_TYPE> Type of object to be cached.
 * @param <ARG_TYPE>    Type of argument that needs to be passed to calculate value.
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class ObservableCachedFieldWithArgBuilder<RETURN_TYPE, ARG_TYPE>
        extends IBusCachedFieldWithArgBuilder<RETURN_TYPE, ARG_TYPE, Bus> {

    protected ObservableCachedFieldWithArgBuilder() {
        super(new ConstructorWrapper<RETURN_TYPE, ARG_TYPE>(),
                OttoCachedField.defaultBus,
                OttoCachedField.defaultSessionIdProvider,
                OttoCachedField.defaultValueGetterExecutor,
                OttoCachedField.defaultStateListenerExecutor);
    }

    private static class ConstructorWrapper<RETURN_TYPE, ARG_TYPE> implements CachedFieldWithArgConstructorWrapper<RETURN_TYPE, ARG_TYPE, Bus> {
        @Override
        public CachedFieldWithArg<RETURN_TYPE, ARG_TYPE> build(Provider<String> sessionIdProvider,
                                                               ProviderWithArg<RETURN_TYPE, ARG_TYPE> valueGetter,
                                                               ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> successEvent,
                                                               ResponseEventWithArg<Exception, ARG_TYPE> errorEvent,
                                                               Bus bus,
                                                               ExecutorService valueGetterExecutor,
                                                               Executor stateListenerExecutor) {
            IBus iBus = new OttoIBus(bus);
            return new ObservableCachedFieldWithArg<RETURN_TYPE, ARG_TYPE>(
                    sessionIdProvider, valueGetter,
                    new IBusSuccessListenerWithArg<RETURN_TYPE, ARG_TYPE>(iBus, successEvent),
                    new IBusErrorListenerWithArg<ARG_TYPE>(iBus, errorEvent),
                    valueGetterExecutor, stateListenerExecutor);
        }
    }
}
