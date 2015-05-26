package com.byoutline.ottocachedfield;

import com.byoutline.eventcallback.ResponseEvent;
import com.byoutline.ibuscachedfield.internal.ErrorEvent;
import com.byoutline.ottoeventcallback.OttoIBus;
import com.squareup.otto.Bus;

import javax.inject.Provider;

/**
 * Fluent interface builder of {@link OttoCachedField}. If you do not like
 * fluent interface create {@link OttoCachedField} by one of its constructors.
 *
 * @param <RETURN_TYPE> Type of object to be cached.
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoCachedFieldBuilder<RETURN_TYPE> {

    private Provider<RETURN_TYPE> valueGetter;
    private ResponseEvent<RETURN_TYPE> successEvent;
    private ErrorEvent errorEvent;
    private Provider<String> sessionIdProvider;
    private Bus bus;

    public OttoCachedFieldBuilder() {
        bus = OttoCachedField.defaultBus;
        sessionIdProvider = OttoCachedField.defaultSessionIdProvider;
    }

    public SuccessEvent withValueProvider(Provider<RETURN_TYPE> valueProvider) {
        this.valueGetter = valueProvider;
        return new SuccessEvent();
    }

    public class SuccessEvent {

        private SuccessEvent() {
        }

        public ErrorEventSetter withSuccessEvent(ResponseEvent<RETURN_TYPE> successEvent) {
            OttoCachedFieldBuilder.this.successEvent = successEvent;
            return new ErrorEventSetter();
        }
    }

    public class ErrorEventSetter {

        private ErrorEventSetter() {
        }

        public CustomSessionIdProvider withGenericErrorEvent(Object errorEvent) {
            OttoCachedFieldBuilder.this.errorEvent = ErrorEvent.genericEvent(errorEvent);
            return new CustomSessionIdProvider();
        }

        public CustomSessionIdProvider withResponseErrorEvent(ResponseEvent<Exception> errorEvent) {
            OttoCachedFieldBuilder.this.errorEvent = ErrorEvent.responseEvent(errorEvent);
            return new CustomSessionIdProvider();
        }

        public OttoCachedField<RETURN_TYPE> build() {
            OttoCachedFieldBuilder.this.errorEvent = new ErrorEvent(null, null);
            return OttoCachedFieldBuilder.this.build();
        }
    }

    public class CustomSessionIdProvider {

        private CustomSessionIdProvider() {
        }

        public CustomBus withCustomSessionIdProvider(Provider<String> sessionIdProvider) {
            OttoCachedFieldBuilder.this.sessionIdProvider = sessionIdProvider;
            return new CustomBus();
        }

        public OttoCachedField<RETURN_TYPE> build() {
            return OttoCachedFieldBuilder.this.build();
        }
    }

    public class CustomBus {

        private CustomBus() {
        }

        public Builder withCustomBus(Bus bus) {
            OttoCachedFieldBuilder.this.bus = bus;
            return new Builder();
        }

        public OttoCachedField<RETURN_TYPE> build() {
            return OttoCachedFieldBuilder.this.build();
        }
    }

    public class Builder {

        private Builder() {
        }

        public OttoCachedField<RETURN_TYPE> build() {
            return OttoCachedFieldBuilder.this.build();
        }
    }

    private OttoCachedField<RETURN_TYPE> build() {
        return new OttoCachedField<RETURN_TYPE>(sessionIdProvider, valueGetter, successEvent, errorEvent, new OttoIBus(bus));
    }
}
