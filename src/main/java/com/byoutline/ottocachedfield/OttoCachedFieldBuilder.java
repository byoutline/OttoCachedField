package com.byoutline.ottocachedfield;

import com.byoutline.eventcallback.ResponseEvent;
import com.byoutline.ottocachedfield.internal.ErrorEvent;
import com.squareup.otto.Bus;

import javax.inject.Provider;

/**
 * Fluent interface builder of {@link OttoCachedField}. If you do not like
 * fluent interface create {@link OttoCachedField} by one of its constructors.
 *
 * @param <T> Type of object to be cached.
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoCachedFieldBuilder<T> {

    private Provider<T> valueGetter;
    private ResponseEvent<T> successEvent;
    private ErrorEvent errorEvent;
    private Provider<String> sessionIdProvider;
    private Bus bus;

    public OttoCachedFieldBuilder() {
        bus = OttoCachedField.defaultBus;
        sessionIdProvider = OttoCachedField.defaultSessionIdProvider;
    }

    public SuccessEvent withValueProvider(Provider<T> valueProvider) {
        this.valueGetter = valueProvider;
        return new SuccessEvent();
    }

    public class SuccessEvent {

        private SuccessEvent() {
        }

        public ErrorEventSetter withSuccessEvent(ResponseEvent<T> successEvent) {
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

        public OttoCachedField<T> build() {
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

        public OttoCachedField<T> build() {
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

        public OttoCachedField<T> build() {
            return OttoCachedFieldBuilder.this.build();
        }
    }

    public class Builder {

        private Builder() {
        }

        public OttoCachedField<T> build() {
            return OttoCachedFieldBuilder.this.build();
        }
    }

    private OttoCachedField<T> build() {
        return new OttoCachedField<>(sessionIdProvider, valueGetter, successEvent, errorEvent, bus);
    }
}
