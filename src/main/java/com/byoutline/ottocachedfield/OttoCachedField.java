package com.byoutline.ottocachedfield;

import com.byoutline.cachedfield.CachedFieldImpl;
import com.byoutline.eventcallback.ResponseEvent;
import com.byoutline.ottocachedfield.internal.OttoErrorListener;
import com.byoutline.ottocachedfield.internal.OttoSuccessListener;
import com.squareup.otto.Bus;
import javax.inject.Provider;

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoCachedField<T> extends CachedFieldImpl<T> {

    public static Provider<String> sessionProvider;
    public static Bus bus;

    public OttoCachedField(Provider<T> valueGetter, ResponseEvent<T> successEvent) {
        this(valueGetter, successEvent, null);
    }

    public OttoCachedField(Provider<T> valueGetter, ResponseEvent<T> successEvent, Object errorEvent) {
        this(sessionProvider, valueGetter, successEvent, errorEvent, bus);
    }

    public OttoCachedField(Provider<String> sessionProvider, Provider<T> valueGetter, ResponseEvent<T> successEvent, Object errorEvent, Bus bus) {
        super(sessionProvider, valueGetter, new OttoSuccessListener<T>(bus, successEvent), new OttoErrorListener(bus, errorEvent));
    }
}
