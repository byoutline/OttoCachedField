package com.byoutline.ottocachedfield;

import com.byoutline.cachedfield.CachedField;
import com.byoutline.cachedfield.CachedFieldImpl;
import com.byoutline.cachedfield.ErrorListener;
import com.byoutline.cachedfield.SuccessListener;
import com.byoutline.eventcallback.ResponseEvent;
import com.byoutline.ibuscachedfield.internal.ErrorEvent;
import com.byoutline.ibuscachedfield.internal.IBusErrorListener;
import com.byoutline.ibuscachedfield.internal.IBusSuccessListener;
import com.byoutline.ottoeventcallback.OttoIBus;
import com.squareup.otto.Bus;

import javax.inject.Provider;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import static com.byoutline.cachedfield.internal.DefaultExecutors.createDefaultStateListenerExecutor;
import static com.byoutline.cachedfield.internal.DefaultExecutors.createDefaultValueGetterExecutor;

/**
 * {@link CachedField} implementation that posts calculated result on Otto bus.
 *
 * @param <RETURN_TYPE> Type of cached value.
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoCachedField<RETURN_TYPE> extends CachedFieldImpl<RETURN_TYPE> {

    public static Provider<String> defaultSessionIdProvider;
    public static Bus defaultBus;
    static ExecutorService defaultValueGetterExecutor;
    static Executor defaultStateListenerExecutor;

    public OttoCachedField(Provider<RETURN_TYPE> valueGetter, ResponseEvent<RETURN_TYPE> successEvent) {
        this(valueGetter, successEvent, null);
    }

    public OttoCachedField(Provider<RETURN_TYPE> valueGetter, ResponseEvent<RETURN_TYPE> successEvent, ResponseEvent<Exception> errorEvent) {
        this(defaultSessionIdProvider, valueGetter, successEvent, errorEvent, defaultBus);
    }

    public OttoCachedField(Provider<RETURN_TYPE> valueGetter, ResponseEvent<RETURN_TYPE> successEvent, Object errorEvent) {
        this(defaultSessionIdProvider, valueGetter, successEvent, errorEvent, defaultBus);
    }

    public OttoCachedField(Provider<String> sessionIdProvider, Provider<RETURN_TYPE> valueGetter, ResponseEvent<RETURN_TYPE> successEvent, ResponseEvent<Exception> errorEvent, Bus bus) {
        this(sessionIdProvider, valueGetter, successEvent, ErrorEvent.responseEvent(errorEvent), bus);
    }

    public OttoCachedField(Provider<String> sessionIdProvider, Provider<RETURN_TYPE> valueGetter, ResponseEvent<RETURN_TYPE> successEvent, Object errorEvent, Bus bus) {
        this(sessionIdProvider, valueGetter, successEvent, ErrorEvent.genericEvent(errorEvent),
                new OttoIBus(bus), createDefaultValueGetterExecutor(), createDefaultStateListenerExecutor());
    }

    OttoCachedField(Provider<String> sessionIdProvider, Provider<RETURN_TYPE> valueGetter,
                    ResponseEvent<RETURN_TYPE> successEvent, ErrorEvent errorEvent, OttoIBus bus,
                    ExecutorService valueGetterExecutor, Executor stateListenerExecutor) {
        this(sessionIdProvider,
                valueGetter,
                new IBusSuccessListener<RETURN_TYPE>(bus, successEvent),
                new IBusErrorListener(bus, errorEvent),
                valueGetterExecutor,
                stateListenerExecutor);
    }

    private OttoCachedField(Provider<String> sessionProvider,
                            Provider<RETURN_TYPE> valueGetter,
                            SuccessListener<RETURN_TYPE> successHandler, ErrorListener errorHandler,
                            ExecutorService valueGetterExecutor, Executor stateListenerExecutor) {
        super(sessionProvider, valueGetter, successHandler, errorHandler, valueGetterExecutor, stateListenerExecutor);
    }

    public static <RETURN_TYPE> OttoCachedFieldBuilder<RETURN_TYPE> builder() {
        return new OttoCachedFieldBuilder<RETURN_TYPE>();
    }

    public static void init(Provider<String> defaultSessionIdProvider, Bus defaultBus) {
        init(defaultSessionIdProvider, defaultBus,
                createDefaultValueGetterExecutor(),
                createDefaultStateListenerExecutor());
    }

    public static void init(Provider<String> defaultSessionIdProvider, Bus defaultBus,
                            ExecutorService defaultValueGetterExecutor, Executor defaultStateListenerExecutor) {
        OttoCachedField.defaultSessionIdProvider = defaultSessionIdProvider;
        OttoCachedField.defaultBus = defaultBus;
        OttoCachedField.defaultValueGetterExecutor = defaultValueGetterExecutor;
        OttoCachedField.defaultStateListenerExecutor = defaultStateListenerExecutor;
    }
}
