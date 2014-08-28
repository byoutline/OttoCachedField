package com.byoutline.ottocachedfield;

import com.byoutline.cachedfield.CachedFieldImpl;
import com.byoutline.eventcallback.ResponseEvent;
import com.byoutline.ottocachedfield.internal.OttoErrorListener;
import com.byoutline.ottocachedfield.internal.OttoSuccessListener;
import com.byoutline.ottocachedfield.internal.RetofitValueProvider;
import com.squareup.otto.Bus;
import javax.inject.Provider;

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoCachedField<T> extends CachedFieldImpl<T> {

    public static Provider<String> defaultSessionIdProvider;
    public static Bus defaultBus;
    public static long MAX_WAIT_TIME_IN_S = 300;

    public OttoCachedField(RetrofitCall<T> valueGetter, ResponseEvent<T> successEvent) {
        this(valueGetter, successEvent, null);
    }

    public OttoCachedField(RetrofitCall<T> valueGetter, ResponseEvent<T> successEvent, Object errorEvent) {
        this(defaultSessionIdProvider, valueGetter, successEvent, errorEvent, defaultBus);
    }

    public OttoCachedField(Provider<String> sessionIdProvider, RetrofitCall<T> valueGetter, ResponseEvent<T> successEvent, Object errorEvent, Bus bus) {
        super(sessionIdProvider,
                new RetofitValueProvider(valueGetter, bus, sessionIdProvider),
                new OttoSuccessListener<T>(bus, successEvent),
                new OttoErrorListener(bus, errorEvent));
    }

    public static void init(Provider<String> defaultSessionIdProvider, Bus defaultBus) {
        OttoCachedField.defaultSessionIdProvider = defaultSessionIdProvider;
        OttoCachedField.defaultBus = defaultBus;
    }
}
