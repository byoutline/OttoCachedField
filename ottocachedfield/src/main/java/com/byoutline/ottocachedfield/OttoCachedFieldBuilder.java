package com.byoutline.ottocachedfield;

import com.byoutline.cachedfield.CachedField;
import com.byoutline.cachedfield.ProviderWithArg;
import com.byoutline.cachedfield.dbcache.DbCachedValueProvider;
import com.byoutline.cachedfield.dbcache.DbWriter;
import com.byoutline.cachedfield.dbcache.FetchType;
import com.byoutline.eventcallback.ResponseEvent;
import com.byoutline.ibuscachedfield.IBusCachedFieldBuilder;
import com.byoutline.ibuscachedfield.builders.CachedFieldConstructorWrapper;
import com.byoutline.ibuscachedfield.internal.ErrorEvent;
import com.byoutline.ibuscachedfield.internal.NullArgumentException;
import com.byoutline.ottoeventcallback.OttoIBus;
import com.squareup.otto.Bus;

import javax.inject.Provider;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Fluent interface builder of {@link OttoCachedField}. If you do not like
 * fluent interface create {@link OttoCachedField} by one of its constructors.
 *
 * @param <RETURN_TYPE> Type of object to be cached.
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoCachedFieldBuilder<RETURN_TYPE> extends IBusCachedFieldBuilder<RETURN_TYPE, Bus, CachedField<RETURN_TYPE>> {

    public OttoCachedFieldBuilder() {
        super(new ConstructorWrapper<RETURN_TYPE>(),
                OttoCachedField.defaultBus,
                OttoCachedField.defaultSessionIdProvider,
                OttoCachedField.defaultValueGetterExecutor,
                OttoCachedField.defaultStateListenerExecutor);
    }

    public <API_RETURN_TYPE> DbCacheBuilderReader<API_RETURN_TYPE, RETURN_TYPE> withApiFetcher(Provider<API_RETURN_TYPE> apiValueProvider) {
        return new DbCacheBuilderReader<API_RETURN_TYPE, RETURN_TYPE>(apiValueProvider);
    }

    public static class DbCacheBuilderReader<API_RETURN_TYPE, RETURN_TYPE> {
        private final Provider<API_RETURN_TYPE> apiValueProvider;

        public DbCacheBuilderReader(Provider<API_RETURN_TYPE> apiValueProvider) {
            this.apiValueProvider = apiValueProvider;
        }

        public DbCacheBuilderWriter<API_RETURN_TYPE, RETURN_TYPE> withDbWriter(DbWriter<API_RETURN_TYPE> dbSaver) {
            return new DbCacheBuilderWriter<API_RETURN_TYPE, RETURN_TYPE>(apiValueProvider, dbSaver);
        }
    }

    public static class DbCacheBuilderWriter<API_RETURN_TYPE, RETURN_TYPE> {
        private final Provider<API_RETURN_TYPE> apiValueProvider;
        private final DbWriter<API_RETURN_TYPE> dbSaver;

        public DbCacheBuilderWriter(Provider<API_RETURN_TYPE> apiValueProvider, DbWriter<API_RETURN_TYPE> dbSaver) {
            this.apiValueProvider = apiValueProvider;
            this.dbSaver = dbSaver;
        }

        public OttoCachedFieldWithArgBuilder.SuccessEvent withDbReader(Provider<RETURN_TYPE> dbValueProvider) {
            ProviderWithArg<RETURN_TYPE, FetchType> valueProvider =
                    new DbCachedValueProvider<API_RETURN_TYPE, RETURN_TYPE>(apiValueProvider, dbSaver, dbValueProvider);
            return new OttoCachedFieldWithArgBuilder<RETURN_TYPE, FetchType>()
                    .withValueProvider(valueProvider);
        }
    }

    private static class ConstructorWrapper<RETURN_TYPE> implements CachedFieldConstructorWrapper<RETURN_TYPE, Bus, CachedField<RETURN_TYPE>> {
        @Override
        public CachedField<RETURN_TYPE> build(Provider<String> sessionIdProvider,
                                              Provider<RETURN_TYPE> valueGetter,
                                              ResponseEvent<RETURN_TYPE> successEvent, ErrorEvent errorEvent,
                                              Bus bus,
                                              ExecutorService valueGetterExecutor, Executor stateListenerExecutor) {
            if (stateListenerExecutor == null || valueGetter == null) {
                throw new NullArgumentException();
            }
            return new OttoCachedField<RETURN_TYPE>(sessionIdProvider, valueGetter,
                    successEvent, errorEvent,
                    new OttoIBus(bus),
                    valueGetterExecutor, stateListenerExecutor);
        }
    }
}
