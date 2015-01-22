package com.byoutline.ottocachedfield;

import com.byoutline.cachedfield.CachedField;
import com.byoutline.cachedfield.CachedFieldImpl;
import com.byoutline.cachedfield.ErrorListener;
import com.byoutline.cachedfield.FieldStateListener;
import com.byoutline.cachedfield.SuccessListener;
import com.byoutline.cachedfield.internal.StubFieldStateListener;
import com.byoutline.eventcallback.ResponseEvent;
import com.byoutline.ottocachedfield.internal.ErrorEvent;
import com.byoutline.ottocachedfield.internal.OttoErrorListener;
import com.byoutline.ottocachedfield.internal.OttoSuccessListener;
import com.squareup.otto.Bus;

import javax.inject.Provider;

/**
 * {@link CachedField} implementation that posts calculated result on Otto bus.
 *
 * @param <T> Type of cached value.
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoCachedField<T> extends CachedFieldImpl<T> {

    public static Provider<String> defaultSessionIdProvider;
    public static Bus defaultBus;

    public OttoCachedField(Provider<T> valueGetter, ResponseEvent<T> successEvent) {
        this(valueGetter, successEvent, null);
    }

    public OttoCachedField(Provider<T> valueGetter, ResponseEvent<T> successEvent, ResponseEvent<Exception> errorEvent) {
        this(defaultSessionIdProvider, valueGetter, successEvent, errorEvent, defaultBus);
    }

    public OttoCachedField(Provider<T> valueGetter, ResponseEvent<T> successEvent, Object errorEvent) {
        this(defaultSessionIdProvider, valueGetter, successEvent, errorEvent, defaultBus);
    }

    public OttoCachedField(Provider<String> sessionIdProvider, Provider<T> valueGetter, ResponseEvent<T> successEvent, ResponseEvent<Exception> errorEvent, Bus bus) {
        this(sessionIdProvider, valueGetter, successEvent, ErrorEvent.responseEvent(errorEvent), new StubFieldStateListener(), bus);
    }

    public OttoCachedField(Provider<String> sessionIdProvider, Provider<T> valueGetter, ResponseEvent<T> successEvent, Object errorEvent, Bus bus) {
        this(sessionIdProvider, valueGetter, successEvent, ErrorEvent.genericEvent(errorEvent), new StubFieldStateListener(), bus);
    }

    public OttoCachedField(Provider<String> sessionIdProvider, Provider<T> valueGetter, ResponseEvent<T> successEvent, ResponseEvent<Exception> errorEvent, FieldStateListener fieldStateListener, Bus bus) {
        this(sessionIdProvider, valueGetter, successEvent, ErrorEvent.responseEvent(errorEvent), fieldStateListener, bus);
    }

    public OttoCachedField(Provider<String> sessionIdProvider, Provider<T> valueGetter, ResponseEvent<T> successEvent, Object errorEvent, FieldStateListener fieldStateListener, Bus bus) {
        this(sessionIdProvider, valueGetter, successEvent, ErrorEvent.genericEvent(errorEvent), fieldStateListener, bus);
    }

    OttoCachedField(Provider<String> sessionIdProvider, Provider<T> valueGetter, ResponseEvent<T> successEvent, ErrorEvent errorEvent, FieldStateListener fieldStateListener, Bus bus) {
        this(sessionIdProvider,
                valueGetter,
                new OttoSuccessListener<>(bus, successEvent),
                new OttoErrorListener(bus, errorEvent),
                fieldStateListener,
                bus);
    }

    private OttoCachedField(Provider<String> sessionProvider,
                            Provider<T> valueGetter,
                            SuccessListener<T> successHandler, ErrorListener errorHandler,
                            FieldStateListener fieldStateListener, Bus bus) {
        super(sessionProvider, valueGetter, successHandler, errorHandler, fieldStateListener);
        bus.register(valueGetter);
    }

    public static <T> OttoCachedFieldBuilder<T> builder() {
        return new OttoCachedFieldBuilder<>();
    }

    public static void init(Provider<String> defaultSessionIdProvider, Bus defaultBus) {
        OttoCachedField.defaultSessionIdProvider = defaultSessionIdProvider;
        OttoCachedField.defaultBus = defaultBus;
    }
}
