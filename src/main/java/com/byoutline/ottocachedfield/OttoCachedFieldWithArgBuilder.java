package com.byoutline.ottocachedfield;

import com.byoutline.cachedfield.ProviderWithArg;
import com.byoutline.cachedfield.dbcache.DbCacheArg;
import com.byoutline.cachedfield.dbcache.DbCachedValueProviderWithArg;
import com.byoutline.cachedfield.dbcache.DbWriterWithArg;
import com.byoutline.ibuscachedfield.events.ResponseEventWithArg;
import com.byoutline.ottoeventcallback.OttoIBus;
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

    public <API_RETURN_TYPE> DbCacheBuilderReader<API_RETURN_TYPE, RETURN_TYPE, ARG_TYPE> withApiFetcher(ProviderWithArg<API_RETURN_TYPE, ARG_TYPE> apiValueProvider) {
        return new DbCacheBuilderReader<API_RETURN_TYPE, RETURN_TYPE, ARG_TYPE>(apiValueProvider);
    }

    public static class DbCacheBuilderReader<API_RETURN_TYPE, RETURN_TYPE, ARG_TYPE> {
        private final ProviderWithArg<API_RETURN_TYPE, ARG_TYPE> apiValueProvider;

        public DbCacheBuilderReader(ProviderWithArg<API_RETURN_TYPE, ARG_TYPE> apiValueProvider) {
            this.apiValueProvider = apiValueProvider;
        }

        public <API_RETURN_TYPE> DbCacheBuilderWriter<API_RETURN_TYPE, RETURN_TYPE, ARG_TYPE> withDbWriter(DbWriterWithArg<API_RETURN_TYPE, ARG_TYPE> dbSaver) {
            return new DbCacheBuilderWriter(apiValueProvider, dbSaver);
        }
    }

    public static class DbCacheBuilderWriter<API_RETURN_TYPE, RETURN_TYPE, ARG_TYPE> {
        private final ProviderWithArg<API_RETURN_TYPE, ARG_TYPE> apiValueProvider;
        private final DbWriterWithArg<API_RETURN_TYPE, ARG_TYPE> dbSaver;

        public DbCacheBuilderWriter(ProviderWithArg<API_RETURN_TYPE, ARG_TYPE> apiValueProvider, DbWriterWithArg<API_RETURN_TYPE, ARG_TYPE> dbSaver) {
            this.apiValueProvider = apiValueProvider;
            this.dbSaver = dbSaver;
        }

        public OttoCachedFieldWithArgBuilder.SuccessEvent withDbReader(ProviderWithArg<RETURN_TYPE, ARG_TYPE> dbValueProvider) {
            ProviderWithArg<RETURN_TYPE, DbCacheArg<ARG_TYPE>> valueProvider = new DbCachedValueProviderWithArg<API_RETURN_TYPE, RETURN_TYPE, ARG_TYPE>(apiValueProvider, dbSaver, dbValueProvider);
            return new OttoCachedFieldWithArgBuilder<RETURN_TYPE, DbCacheArg<ARG_TYPE>>()
                    .withValueProvider(valueProvider);
        }
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
        return new OttoCachedFieldWithArg<RETURN_TYPE, ARG_TYPE>(sessionIdProvider, valueGetter, successEvent, errorEvent, new OttoIBus(bus));
    }
}
