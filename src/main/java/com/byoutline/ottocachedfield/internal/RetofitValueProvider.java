package com.byoutline.ottocachedfield.internal;

import com.byoutline.eventcallback.CallbackConfig;
import com.byoutline.eventcallback.EventCallback;
import com.byoutline.eventcallback.IBus;
import com.byoutline.ottocachedfield.OttoCachedField;
import com.byoutline.ottocachedfield.RetrofitCall;
import com.byoutline.ottoeventcallback.OttoIBus;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import javax.inject.Provider;
import retrofit.RetrofitError;

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class RetofitValueProvider<T> implements Provider<T> {

    private final RetrofitCall call;
    private final AtomicLong eventsId = new AtomicLong(Long.MIN_VALUE);
    private final CallbackConfig config;

    private CountDownLatch doneSignal;
    private T currentResult;
    private RetrofitError currentError;

    public RetofitValueProvider(RetrofitCall<T> call, IBus bus, Provider<String> sessionIdProvider) {
        this.call = call;
        config = getCallbackConfig(bus, sessionIdProvider);
    }

    @Override
    public T get() throws RuntimeException {
        doneSignal = new CountDownLatch(1);
        EventCallback<T, RetrofitError> cb = getCallback();
        call.get(cb);
        try {
            doneSignal.await(OttoCachedField.MAX_WAIT_TIME_IN_S, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            throw new RuntimeException("Timeout reached " + OttoCachedField.MAX_WAIT_TIME_IN_S, ex);
        }
        if (currentError != null) {
            throw new RuntimeException("Failed to load value", currentError);
        }
        return currentResult;
    }

    @Subscribe
    public void onSuccess(SuccessEvent<T> successEvent) {
        currentResult = successEvent.getResponse();
        currentError = null;
        doneSignal.countDown();
    }

    @Subscribe
    public void onError(ErrorEvent errorEvent) {
        currentResult = null;
        currentError = errorEvent.getResponse();
        doneSignal.countDown();
    }

    private EventCallback<T, RetrofitError> getCallback() {
        long id = eventsId.getAndIncrement();
        return CFEventCallback.<T>builder(config).
                onSuccess().postResponseEvents(new SuccessEvent<T>(id)).validThisSessionOnly().
                onError().postResponseEvents(new ErrorEvent(id)).validThisSessionOnly()
                .build();
    }

    private CallbackConfig getCallbackConfig(IBus bus, Provider<String> sessionIdProvider) {
        return new CallbackConfig(false, bus, sessionIdProvider);
    }
}
