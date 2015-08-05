package com.byoutline.ottocachedfield;

import com.byoutline.cachedfield.CachedField;
import com.byoutline.eventcallback.ResponseEvent;
import com.byoutline.ibuscachedfield.IBusCachedFieldBuilder;
import com.byoutline.ibuscachedfield.builders.CachedFieldConstructorWrapper;
import com.byoutline.ibuscachedfield.internal.ErrorEvent;
import com.byoutline.ottoeventcallback.OttoIBus;
import com.squareup.otto.Bus;

import javax.inject.Provider;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Fluent interface builder of {@link OttoCachedField}. If you do not like
 * fluent interface create {@link OttoCachedField} by one of its constructors.
 *
 * @param <RETURN_TYPE> Type of object to be cached.
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoCachedFieldBuilder<RETURN_TYPE> extends IBusCachedFieldBuilder<RETURN_TYPE, Bus> {

    public OttoCachedFieldBuilder() {
        super(new ConstructorWrapper<RETURN_TYPE>(),
                OttoCachedField.defaultBus,
                OttoCachedField.defaultSessionIdProvider,
                OttoCachedField.defaultValueGetterExecutor,
                OttoCachedField.defaultStateListenerExecutor);
    }

    private static class ConstructorWrapper<RETURN_TYPE> implements CachedFieldConstructorWrapper<RETURN_TYPE, Bus> {
        @Override
        public CachedField<RETURN_TYPE> build(Provider<String> sessionIdProvider,
                                              Provider<RETURN_TYPE> valueGetter,
                                              ResponseEvent<RETURN_TYPE> successEvent, ErrorEvent errorEvent,
                                              Bus bus,
                                              ExecutorService valueGetterExecutor, Executor stateListenerExecutor) {
            return new OttoCachedField<RETURN_TYPE>(sessionIdProvider, valueGetter,
                    successEvent, errorEvent,
                    new OttoIBus(bus),
                    valueGetterExecutor, stateListenerExecutor);
        }
    }
}
