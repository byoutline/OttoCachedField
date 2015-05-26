package com.byoutline.ottocachedfield;

import com.byoutline.cachedfield.*;
import com.byoutline.ibuscachedfield.events.ResponseEventWithArg;
import com.byoutline.ibuscachedfield.internal.IBusErrorListenerWithArg;
import com.byoutline.ibuscachedfield.internal.IBusSuccessListenerWithArg;
import com.byoutline.ottoeventcallback.OttoIBus;

import javax.inject.Provider;

/**
 * {@link CachedField} implementation that posts calculated result on Otto bus.
 *
 * @param <RETURN_TYPE> Type of cached value.
 * @param <ARG_TYPE> Type of argument that needs to be passed to calculate value.
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoCachedFieldWithArg<RETURN_TYPE, ARG_TYPE> extends CachedFieldWithArgImpl<RETURN_TYPE, ARG_TYPE> {

    OttoCachedFieldWithArg(Provider<String> sessionIdProvider, 
            ProviderWithArg<RETURN_TYPE, ARG_TYPE> valueGetter, 
            ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> successEvent,
            ResponseEventWithArg<Exception, ARG_TYPE> errorEvent, OttoIBus bus) {
        this(sessionIdProvider,
                valueGetter,
                new IBusSuccessListenerWithArg<RETURN_TYPE, ARG_TYPE>(bus, successEvent),
                new IBusErrorListenerWithArg<ARG_TYPE>(bus, errorEvent),
                bus);
    }

    private OttoCachedFieldWithArg(Provider<String> sessionProvider,
                            ProviderWithArg<RETURN_TYPE, ARG_TYPE> valueGetter,
                            SuccessListenerWithArg<RETURN_TYPE, ARG_TYPE> successHandler, 
                            ErrorListenerWithArg<ARG_TYPE> errorHandler, OttoIBus bus) {
        super(sessionProvider, valueGetter, successHandler, errorHandler);
        bus.register(valueGetter);
    }

    public static <RETURN_TYPE, ARG_TYPE> OttoCachedFieldWithArgBuilder<RETURN_TYPE, ARG_TYPE> builder() {
        return new OttoCachedFieldWithArgBuilder<RETURN_TYPE, ARG_TYPE>();
    }
}
