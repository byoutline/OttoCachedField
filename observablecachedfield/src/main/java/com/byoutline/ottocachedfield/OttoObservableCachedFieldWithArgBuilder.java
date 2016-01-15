package com.byoutline.ottocachedfield;

import com.byoutline.eventcallback.IBus;
import com.byoutline.observablecachedfield.ObservableCachedFieldWithArg;
import com.byoutline.observablecachedfield.ObservableCachedFieldWithArgBuilder;
import com.byoutline.ottoeventcallback.OttoIBus;
import com.squareup.otto.Bus;

/**
 * Fluent interface builder of {@link ObservableCachedFieldWithArg}.
 *
 * @param <RETURN_TYPE> Type of object to be cached.
 * @param <ARG_TYPE>    Type of argument that needs to be passed to calculate value.
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoObservableCachedFieldWithArgBuilder<RETURN_TYPE, ARG_TYPE>
        extends ObservableCachedFieldWithArgBuilder<RETURN_TYPE, ARG_TYPE, Bus> {
    public OttoObservableCachedFieldWithArgBuilder() {
        super(OttoCachedField.defaultSessionIdProvider,
                OttoCachedField.defaultBus, new OttoConverter(),
                OttoCachedField.defaultValueGetterExecutor,
                OttoCachedField.defaultStateListenerExecutor);
    }

    private static class OttoConverter implements BusConverter<Bus> {
        @Override
        public IBus convert(Bus bus) {
            return new OttoIBus(bus);
        }
    }
}
