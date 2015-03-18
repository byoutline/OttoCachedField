package com.byoutline.ottocachedfield;

import com.byoutline.cachedfield.ProviderWithArg;
import com.byoutline.ottocachedfield.events.ResponseEventWithArg;
import com.squareup.otto.Bus;

import javax.annotation.Nullable;
import javax.inject.Provider;

/**
 * Fluent interface builder of {@link OttoCachedField}. If you do not like
 * fluent interface create {@link OttoCachedField} by one of its constructors.
 *
 * @param <RETURN_TYPE> Type of object to be cached.
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoCachedFieldWithArgBuilder<RETURN_TYPE, ARG_TYPE> {

    private ProviderWithArg<RETURN_TYPE, ARG_TYPE> valueGetter;
    private ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> successEvent;
    private ResponseEventWithArg<Exception, ARG_TYPE> errorEvent;
    private Provider<String> sessionIdProvider;
    private Bus bus;

    public OttoCachedFieldWithArgBuilder() {
        bus = OttoCachedField.defaultBus;
        sessionIdProvider = OttoCachedField.defaultSessionIdProvider;
    }

    public SuccessEvent withValueProvider(ProviderWithArg<RETURN_TYPE, ARG_TYPE> valueProvider) {
        this.valueGetter = valueProvider;
        return new SuccessEvent();
    }

    public class SuccessEvent {

        private SuccessEvent() {
        }

        public ErrorEventSetter withSuccessEvent(ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> successEvent) {
            OttoCachedFieldWithArgBuilder.this.successEvent = successEvent;
            return new ErrorEventSetter();
        }
    }

    public class ErrorEventSetter {

        private ErrorEventSetter() {
        }

        public CustomSessionIdProvider withResponseErrorEvent(@Nullable ResponseEventWithArg<Exception, ARG_TYPE> errorEvent) {
            OttoCachedFieldWithArgBuilder.this.errorEvent = errorEvent;
            return new CustomSessionIdProvider();
        }

        public OttoCachedFieldWithArg<RETURN_TYPE, ARG_TYPE> build() {
            return OttoCachedFieldWithArgBuilder.this.build();
        }
    }

    public class CustomSessionIdProvider {

        private CustomSessionIdProvider() {
        }

        public CustomBus withCustomSessionIdProvider(Provider<String> sessionIdProvider) {
            OttoCachedFieldWithArgBuilder.this.sessionIdProvider = sessionIdProvider;
            return new CustomBus();
        }

        public OttoCachedFieldWithArg<RETURN_TYPE, ARG_TYPE> build() {
            return OttoCachedFieldWithArgBuilder.this.build();
        }
    }

    public class CustomBus {

        private CustomBus() {
        }

        public Builder withCustomBus(Bus bus) {
            OttoCachedFieldWithArgBuilder.this.bus = bus;
            return new Builder();
        }

        public OttoCachedFieldWithArg<RETURN_TYPE, ARG_TYPE> build() {
            return OttoCachedFieldWithArgBuilder.this.build();
        }
    }

    public class Builder {

        private Builder() {
        }

        public OttoCachedFieldWithArg<RETURN_TYPE, ARG_TYPE> build() {
            return OttoCachedFieldWithArgBuilder.this.build();
        }
    }

    private OttoCachedFieldWithArg<RETURN_TYPE, ARG_TYPE> build() {
        return new OttoCachedFieldWithArg<RETURN_TYPE, ARG_TYPE>(sessionIdProvider, valueGetter, successEvent, errorEvent, bus);
    }
}
