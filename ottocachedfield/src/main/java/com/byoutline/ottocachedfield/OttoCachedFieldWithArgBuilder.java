package com.byoutline.ottocachedfield;

import com.byoutline.cachedfield.CachedFieldWithArg;
import com.byoutline.cachedfield.ProviderWithArg;
import com.byoutline.cachedfield.dbcache.DbCacheArg;
import com.byoutline.cachedfield.dbcache.DbCachedValueProviderWithArg;
import com.byoutline.cachedfield.dbcache.DbWriterWithArg;
import com.byoutline.ibuscachedfield.IBusCachedFieldWithArgBuilder;
import com.byoutline.ibuscachedfield.builders.CachedFieldWithArgConstructorWrapper;
import com.byoutline.ibuscachedfield.events.ResponseEventWithArg;
import com.byoutline.ibuscachedfield.internal.NullArgumentException;
import com.byoutline.ottoeventcallback.OttoIBus;
import com.squareup.otto.Bus;

import javax.inject.Provider;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Fluent interface builder of {@link OttoCachedFieldWithArg}.
 *
 * @param <RETURN_TYPE> Type of object to be cached.
 * @param <ARG_TYPE>    Type of argument that needs to be passed to calculate value.
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoCachedFieldWithArgBuilder<RETURN_TYPE, ARG_TYPE>
        extends IBusCachedFieldWithArgBuilder<RETURN_TYPE, ARG_TYPE, Bus, CachedFieldWithArg<RETURN_TYPE, ARG_TYPE>> {

    public OttoCachedFieldWithArgBuilder() {
        super(new ConstructorWrapper<RETURN_TYPE, ARG_TYPE>(),
                OttoCachedField.defaultBus,
                OttoCachedField.defaultSessionIdProvider,
                OttoCachedField.defaultValueGetterExecutor,
                OttoCachedField.defaultStateListenerExecutor);
    }

    public <API_RETURN_TYPE> DbCacheBuilderReader<API_RETURN_TYPE, RETURN_TYPE, ARG_TYPE> withApiFetcher(ProviderWithArg<API_RETURN_TYPE, ARG_TYPE> apiValueProvider) {
        return new DbCacheBuilderReader<API_RETURN_TYPE, RETURN_TYPE, ARG_TYPE>(apiValueProvider);
    }

    public static class DbCacheBuilderReader<API_RETURN_TYPE, RETURN_TYPE, ARG_TYPE> {
        private final ProviderWithArg<API_RETURN_TYPE, ARG_TYPE> apiValueProvider;

        public DbCacheBuilderReader(ProviderWithArg<API_RETURN_TYPE, ARG_TYPE> apiValueProvider) {
            this.apiValueProvider = apiValueProvider;
        }

        public DbCacheBuilderWriter<API_RETURN_TYPE, RETURN_TYPE, ARG_TYPE> withDbWriter(DbWriterWithArg<API_RETURN_TYPE, ARG_TYPE> dbSaver) {
            return new DbCacheBuilderWriter<API_RETURN_TYPE, RETURN_TYPE, ARG_TYPE>(apiValueProvider, dbSaver);
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

    private static class ConstructorWrapper<RETURN_TYPE, ARG_TYPE>
            implements CachedFieldWithArgConstructorWrapper<RETURN_TYPE, ARG_TYPE, Bus, CachedFieldWithArg<RETURN_TYPE, ARG_TYPE>> {

        @Override
        public CachedFieldWithArg<RETURN_TYPE, ARG_TYPE> build(Provider<String> sessionIdProvider, ProviderWithArg<RETURN_TYPE, ARG_TYPE> valueGetter, ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> successEvent, ResponseEventWithArg<Exception, ARG_TYPE> errorEvent, Bus bus, ExecutorService valueGetterExecutor, Executor stateListenerExecutor) {
            if (stateListenerExecutor == null || valueGetter == null) {
                throw new NullArgumentException();
            }
            return new OttoCachedFieldWithArg<RETURN_TYPE, ARG_TYPE>(sessionIdProvider, valueGetter,
                    successEvent, errorEvent,
                    new OttoIBus(bus),
                    valueGetterExecutor, stateListenerExecutor);
        }
    }
}
