package com.byoutline.ottocachedfield;

import com.byoutline.cachedfield.CachedField;
import com.byoutline.cachedfield.CachedFieldWithArgImpl;
import com.byoutline.cachedfield.ErrorListenerWithArg;
import com.byoutline.cachedfield.ProviderWithArg;
import com.byoutline.cachedfield.SuccessListenerWithArg;
import com.byoutline.ottocachedfield.internal.OttoErrorListenerWithArg;
import com.byoutline.ottocachedfield.internal.OttoSuccessListenerWithArg;
import com.squareup.otto.Bus;
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
            ResponseEventWithArg<Exception, ARG_TYPE> errorEvent, Bus bus) {
        this(sessionIdProvider,
                valueGetter,
                new OttoSuccessListenerWithArg<RETURN_TYPE, ARG_TYPE>(bus, successEvent),
                new OttoErrorListenerWithArg<ARG_TYPE>(bus, errorEvent),
                bus);
    }

    private OttoCachedFieldWithArg(Provider<String> sessionProvider,
                            ProviderWithArg<RETURN_TYPE, ARG_TYPE> valueGetter,
                            SuccessListenerWithArg<RETURN_TYPE, ARG_TYPE> successHandler, 
                            ErrorListenerWithArg<ARG_TYPE> errorHandler, Bus bus) {
        super(sessionProvider, valueGetter, successHandler, errorHandler);
        bus.register(valueGetter);
    }

    public static <RETURN_TYPE, ARG_TYPE> OttoCachedFieldWithArgBuilder<RETURN_TYPE, ARG_TYPE> builder() {
        return new OttoCachedFieldWithArgBuilder<RETURN_TYPE, ARG_TYPE>();
    }
}
