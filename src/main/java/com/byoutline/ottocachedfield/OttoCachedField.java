package com.byoutline.ottocachedfield;

import com.byoutline.cachedfield.CachedFieldImpl;
import com.byoutline.cachedfield.ErrorListener;
import com.byoutline.cachedfield.SuccessListener;
import com.byoutline.eventcallback.ResponseEvent;
import com.byoutline.ottocachedfield.internal.OttoErrorListener;
import com.byoutline.ottocachedfield.internal.OttoSuccessListener;
import com.byoutline.ottocachedfield.internal.RetofitValueProvider;
import com.byoutline.ottoeventcallback.PostFromAnyThreadIBus;
import com.squareup.otto.Bus;
import javax.inject.Provider;

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoCachedField<T> extends CachedFieldImpl<T> {

    public static Provider<String> defaultSessionIdProvider;
    public static Bus defaultBus;
    public static long MAX_WAIT_TIME_IN_S = 60;

    public OttoCachedField(RetrofitCall<T> valueGetter, ResponseEvent<T> successEvent) {
        this(valueGetter, successEvent, null);
    }

    public OttoCachedField(RetrofitCall<T> valueGetter, ResponseEvent<T> successEvent, Object errorEvent) {
        this(defaultSessionIdProvider, valueGetter, successEvent, errorEvent, defaultBus);
    }

    public OttoCachedField(Provider<String> sessionIdProvider, RetrofitCall<T> valueGetter, ResponseEvent<T> successEvent, Object errorEvent, Bus bus) {
        this(sessionIdProvider, valueGetter, successEvent, errorEvent, new PostFromAnyThreadIBus(bus));
    }

    private OttoCachedField(Provider<String> sessionIdProvider, RetrofitCall<T> valueGetter, ResponseEvent<T> successEvent, Object errorEvent, PostFromAnyThreadIBus bus) {
        this(sessionIdProvider,
                new RetofitValueProvider(valueGetter, bus, sessionIdProvider),
                new OttoSuccessListener(bus, successEvent),
                new OttoErrorListener(bus, errorEvent),
                bus);
    }

    private OttoCachedField(Provider<String> sessionProvider,
            RetofitValueProvider<T> valueGetter,
            SuccessListener<T> successHandler, ErrorListener errorHandler,
            PostFromAnyThreadIBus bus) {
        super(sessionProvider, valueGetter, successHandler, errorHandler);
        bus.register(valueGetter);
    }

    public static void init(Provider<String> defaultSessionIdProvider, Bus defaultBus) {
        OttoCachedField.defaultSessionIdProvider = defaultSessionIdProvider;
        OttoCachedField.defaultBus = defaultBus;
    }
}
