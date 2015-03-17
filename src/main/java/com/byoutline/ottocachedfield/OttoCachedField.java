package com.byoutline.ottocachedfield;

import com.byoutline.cachedfield.CachedField;
import com.byoutline.cachedfield.CachedFieldImpl;
import com.byoutline.cachedfield.ErrorListener;
import com.byoutline.cachedfield.FieldStateListener;
import com.byoutline.cachedfield.SuccessListener;
import com.byoutline.eventcallback.ResponseEvent;
import com.byoutline.ottocachedfield.internal.ErrorEvent;
import com.byoutline.ottocachedfield.internal.OttoErrorListener;
import com.byoutline.ottocachedfield.internal.OttoSuccessListener;
import com.squareup.otto.Bus;
import javax.inject.Provider;

/**
 * {@link CachedField} implementation that posts calculated result on Otto bus.
 *
 * @param <RETURN_TYPE> Type of cached value.
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoCachedField<RETURN_TYPE> extends CachedFieldImpl<RETURN_TYPE> {

    public static Provider<String> defaultSessionIdProvider;
    public static Bus defaultBus;

    public OttoCachedField(Provider<RETURN_TYPE> valueGetter, ResponseEvent<RETURN_TYPE> successEvent) {
        this(valueGetter, successEvent, null);
    }

    public OttoCachedField(Provider<RETURN_TYPE> valueGetter, ResponseEvent<RETURN_TYPE> successEvent, ResponseEvent<Exception> errorEvent) {
        this(defaultSessionIdProvider, valueGetter, successEvent, errorEvent, defaultBus);
    }

    public OttoCachedField(Provider<RETURN_TYPE> valueGetter, ResponseEvent<RETURN_TYPE> successEvent, Object errorEvent) {
        this(defaultSessionIdProvider, valueGetter, successEvent, errorEvent, defaultBus);
    }

    public OttoCachedField(Provider<String> sessionIdProvider, Provider<RETURN_TYPE> valueGetter, ResponseEvent<RETURN_TYPE> successEvent, ResponseEvent<Exception> errorEvent, FieldStateListener fieldStateListener, Bus bus) {
        this(sessionIdProvider, valueGetter, successEvent, ErrorEvent.responseEvent(errorEvent), bus);
    }

    public OttoCachedField(Provider<String> sessionIdProvider, Provider<RETURN_TYPE> valueGetter, ResponseEvent<RETURN_TYPE> successEvent, Object errorEvent, Bus bus) {
        this(sessionIdProvider, valueGetter, successEvent, ErrorEvent.genericEvent(errorEvent), bus);
    }

    OttoCachedField(Provider<String> sessionIdProvider, Provider<RETURN_TYPE> valueGetter, ResponseEvent<RETURN_TYPE> successEvent, ErrorEvent errorEvent, Bus bus) {
        this(sessionIdProvider,
                valueGetter,
                new OttoSuccessListener<RETURN_TYPE>(bus, successEvent),
                new OttoErrorListener(bus, errorEvent),
                bus);
    }

    private OttoCachedField(Provider<String> sessionProvider,
                            Provider<RETURN_TYPE> valueGetter,
                            SuccessListener<RETURN_TYPE> successHandler, ErrorListener errorHandler, Bus bus) {
        super(sessionProvider, valueGetter, successHandler, errorHandler);
        bus.register(valueGetter);
    }

    public static <RETURN_TYPE> OttoCachedFieldBuilder<RETURN_TYPE> builder() {
        return new OttoCachedFieldBuilder<RETURN_TYPE>();
    }

    public static void init(Provider<String> defaultSessionIdProvider, Bus defaultBus) {
        OttoCachedField.defaultSessionIdProvider = defaultSessionIdProvider;
        OttoCachedField.defaultBus = defaultBus;
    }
}
