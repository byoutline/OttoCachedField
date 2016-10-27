package com.byoutline.ottocachedfield;

import com.byoutline.cachedfield.*;
import com.byoutline.cachedfield.cachedendpoint.StateAndValue;
import com.byoutline.eventcallback.ResponseEvent;
import com.byoutline.ibuscachedfield.events.ResponseEventWithArg;
import com.byoutline.ibuscachedfield.internal.*;
import com.byoutline.observablecachedfield.ObservableCachedField;
import com.byoutline.observablecachedfield.ObservableCachedFieldWithArg;
import com.byoutline.ottoeventcallback.OttoIBus;
import com.squareup.otto.Bus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Provider;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class CachedFieldBuilder {
    public <RETURN_TYPE> PostValueProvNoArg<RETURN_TYPE> withValueProvider(@Nonnull Provider<RETURN_TYPE> provider) {
        return new PostValueProvNoArg<>(provider);
    }

    public <RETURN_TYPE, ARG_TYPE> PostValueProvWithArg<RETURN_TYPE, ARG_TYPE> withValueProviderWithArg(@Nonnull ProviderWithArg<RETURN_TYPE, ARG_TYPE> provider) {
        return new PostValueProvWithArg<>(provider);
    }

    public static class PostValueProvNoArg<RETURN_TYPE> {
        final Provider<RETURN_TYPE> provider;

        protected PostValueProvNoArg(Provider<RETURN_TYPE> provider) {
            this.provider = provider;
        }

        public WithSuccessEventNoArg<RETURN_TYPE, OttoCachedField<RETURN_TYPE>> withSuccessEvent(@Nonnull ResponseEvent<RETURN_TYPE> event) {
            BusConstructorWrapperNoArg<RETURN_TYPE, OttoCachedField<RETURN_TYPE>> constructorWrapper = new BusConstructorWrapperNoArg<RETURN_TYPE, OttoCachedField<RETURN_TYPE>>() {
                @Override
                public OttoCachedField<RETURN_TYPE> build(Provider<String> sessionIdProvider,
                                                          ResponseEvent<RETURN_TYPE> successEvent, ResponseEvent<Exception> errorEvent, Bus customBus,
                                                          ExecutorService valueGetterExecutor, Executor stateListenerExecutor) {
                    ErrorEvent onErrorEvent = new ErrorEvent(errorEvent, null);
                    OttoIBus iBus = new OttoIBus(customBus);
                    return new OttoCachedField<>(sessionIdProvider,
                            provider, successEvent, onErrorEvent, iBus,
                            valueGetterExecutor, stateListenerExecutor);
                }
            };
            return new WithSuccessEventNoArg<>(event, constructorWrapper);
        }

        public AsObservableNoArg<RETURN_TYPE> asObservable() {
            return new AsObservableNoArg<>(provider);
        }
    }

    public static class PostValueProvWithArg<RETURN_TYPE, ARG_TYPE> {
        final ProviderWithArg<RETURN_TYPE, ARG_TYPE> provider;

        protected PostValueProvWithArg(ProviderWithArg<RETURN_TYPE, ARG_TYPE> provider) {
            this.provider = provider;
        }

        public WithSuccessEventWithArg<RETURN_TYPE, ARG_TYPE, OttoCachedFieldWithArg<RETURN_TYPE, ARG_TYPE>> withSuccessEvent(@Nonnull final ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> event) {
            BusConstructorWrapperWithArg<RETURN_TYPE, ARG_TYPE, OttoCachedFieldWithArg<RETURN_TYPE, ARG_TYPE>> builder = new BusConstructorWrapperWithArg<RETURN_TYPE, ARG_TYPE, OttoCachedFieldWithArg<RETURN_TYPE, ARG_TYPE>>() {
                @Override
                public OttoCachedFieldWithArg<RETURN_TYPE, ARG_TYPE> build(Provider<String> sessionIdProvider, ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> successEvent, ResponseEventWithArg<Exception, ARG_TYPE> errorEvent, Bus customBus, ExecutorService valueGetterExecutor, Executor stateListenerExecutor) {
                    OttoIBus iBus = new OttoIBus(customBus);
                    return new OttoCachedFieldWithArg<>(
                            sessionIdProvider, provider,
                            event, errorEvent, iBus,
                            valueGetterExecutor, stateListenerExecutor
                    );
                }
            };
            return new WithSuccessEventWithArg<>(event, builder);
        }

        public AsEndpointWithArg<RETURN_TYPE, ARG_TYPE> asEndpoint(@Nonnull ResponseEventWithArg<StateAndValue<RETURN_TYPE, ARG_TYPE>, ARG_TYPE> resultEvent) {
            return new AsEndpointWithArg<>(provider, resultEvent);
        }

        public AsObservableWithArg<RETURN_TYPE, ARG_TYPE> asObservable() {
            return new AsObservableWithArg<>(provider);
        }
    }

    public static class AsEndpointWithArg<RETURN_TYPE, ARG_TYPE> {
        final ProviderWithArg<RETURN_TYPE, ARG_TYPE> provider;
        final ResponseEventWithArg<StateAndValue<RETURN_TYPE, ARG_TYPE>, ARG_TYPE> resultEvent;

        public AsEndpointWithArg(ProviderWithArg<RETURN_TYPE, ARG_TYPE> provider, ResponseEventWithArg<StateAndValue<RETURN_TYPE, ARG_TYPE>, ARG_TYPE> resultEvent) {
            this.provider = provider;
            this.resultEvent = resultEvent;
        }

        public OverrideDefaultsSetter<OttoCachedEndpointWithArg<RETURN_TYPE, ARG_TYPE>> withCustomBus(@Nonnull Bus customBus) {
            return builder(customBus);
        }

        private OverrideDefaultsSetter<OttoCachedEndpointWithArg<RETURN_TYPE, ARG_TYPE>> withDefaultBus() {
            return withCustomBus(OttoCachedField.defaultBus);
        }

        public OverrideDefaultsSetter<OttoCachedEndpointWithArg<RETURN_TYPE, ARG_TYPE>> withCustomSessionIdProvider(@Nonnull Provider<String> customSessionIdProvider) {
            return withDefaultBus().withCustomSessionIdProvider(customSessionIdProvider);
        }

        public OverrideDefaultsSetter<OttoCachedEndpointWithArg<RETURN_TYPE, ARG_TYPE>> withCustomValueGetterExecutor(@Nonnull ExecutorService valueGetterExecutor) {
            return withDefaultBus().withCustomValueGetterExecutor(valueGetterExecutor);
        }

        public OverrideDefaultsSetter<OttoCachedEndpointWithArg<RETURN_TYPE, ARG_TYPE>> withCustomStateListenerExecutor(@Nonnull Executor stateListenerExecutor) {
            return withDefaultBus().withCustomStateListenerExecutor(stateListenerExecutor);
        }

        private OverrideDefaultsSetter<OttoCachedEndpointWithArg<RETURN_TYPE, ARG_TYPE>> builder(final Bus bus) {
            return new OverrideDefaultsSetter<>(new ConstructorWrapper<OttoCachedEndpointWithArg<RETURN_TYPE, ARG_TYPE>>() {
                @Override
                public OttoCachedEndpointWithArg<RETURN_TYPE, ARG_TYPE> build(Provider<String> sessionIdProvider, ExecutorService valueGetterExecutor, Executor stateListenerExecutor) {
                    OttoIBus iBus = new OttoIBus(bus);
                    return new OttoCachedEndpointWithArg<>(sessionIdProvider, provider,
                            resultEvent, iBus,
                            valueGetterExecutor, stateListenerExecutor
                    );
                }
            });
        }

        public OttoCachedEndpointWithArg<RETURN_TYPE, ARG_TYPE> build() {
            return withDefaultBus().build();
        }
    }

    public static class AsObservableNoArg<RETURN_TYPE> {
        final Provider<RETURN_TYPE> provider;

        protected AsObservableNoArg(Provider<RETURN_TYPE> provider) {
            this.provider = provider;
        }

        public OverrideDefaultsSetter<ObservableCachedField<RETURN_TYPE>> withCustomSessionIdProvider(@Nonnull Provider<String> customSessionIdProvider) {
            return builder().withCustomSessionIdProvider(customSessionIdProvider);
        }

        public OverrideDefaultsSetter<ObservableCachedField<RETURN_TYPE>> withCustomValueGetterExecutor(@Nonnull ExecutorService valueGetterExecutor) {
            return builder().withCustomValueGetterExecutor(valueGetterExecutor);
        }

        public OverrideDefaultsSetter<ObservableCachedField<RETURN_TYPE>> withCustomStateListenerExecutor(@Nonnull Executor stateListenerExecutor) {
            return builder().withCustomStateListenerExecutor(stateListenerExecutor);
        }

        public WithSuccessEventNoArg<RETURN_TYPE, ObservableCachedField<RETURN_TYPE>> withSuccessEvent(@Nonnull ResponseEvent<RETURN_TYPE> event) {
            BusConstructorWrapperNoArg<RETURN_TYPE, ObservableCachedField<RETURN_TYPE>> builder = new BusConstructorWrapperNoArg<RETURN_TYPE, ObservableCachedField<RETURN_TYPE>>() {
                @Override
                public ObservableCachedField<RETURN_TYPE> build(Provider<String> sessionIdProvider, ResponseEvent<RETURN_TYPE> successEvent, ResponseEvent<Exception> errorEvent, Bus customBus, ExecutorService valueGetterExecutor, Executor stateListenerExecutor) {
                    OttoIBus iBus = new OttoIBus(customBus);
                    ErrorEvent onErrorEvent = new ErrorEvent(errorEvent, null);
                    SuccessListener<RETURN_TYPE> additionalSuccessListener = new IBusSuccessListener<>(iBus, successEvent);
                    ErrorListener additionalErrorListener = new IBusErrorListener(iBus, onErrorEvent);
                    return new ObservableCachedField<>(
                            sessionIdProvider,
                            provider,
                            additionalSuccessListener,
                            additionalErrorListener,
                            valueGetterExecutor,
                            stateListenerExecutor
                    );
                }
            };
            return new WithSuccessEventNoArg<>(event, builder);
        }

        private OverrideDefaultsSetter<ObservableCachedField<RETURN_TYPE>> builder() {
            return new OverrideDefaultsSetter<>(new ConstructorWrapper<ObservableCachedField<RETURN_TYPE>>() {
                @Override
                public ObservableCachedField<RETURN_TYPE> build(Provider<String> sessionIdProvider, ExecutorService valueGetterExecutor, Executor stateListenerExecutor) {
                    return new ObservableCachedField<>(
                            sessionIdProvider,
                            provider,
                            new StubSuccessListener<RETURN_TYPE>(),
                            new StubErrorListener(),
                            valueGetterExecutor,
                            stateListenerExecutor
                    );
                }
            });
        }

        public ObservableCachedField<RETURN_TYPE> build() {
            return builder().build();
        }
    }

    public static class AsObservableWithArg<RETURN_TYPE, ARG_TYPE> {
        final ProviderWithArg<RETURN_TYPE, ARG_TYPE> provider;

        public AsObservableWithArg(ProviderWithArg<RETURN_TYPE, ARG_TYPE> provider) {
            this.provider = provider;
        }

        public OverrideDefaultsSetter<ObservableCachedFieldWithArg<RETURN_TYPE, ARG_TYPE>> withCustomSessionIdProvider(@Nonnull Provider<String> customSessionIdProvider) {
            return builder().withCustomSessionIdProvider(customSessionIdProvider);
        }

        public OverrideDefaultsSetter<ObservableCachedFieldWithArg<RETURN_TYPE, ARG_TYPE>> withCustomValueGetterExecutor(@Nonnull ExecutorService valueGetterExecutor) {
            return builder().withCustomValueGetterExecutor(valueGetterExecutor);
        }

        public OverrideDefaultsSetter<ObservableCachedFieldWithArg<RETURN_TYPE, ARG_TYPE>> withCustomStateListenerExecutor(@Nonnull Executor stateListenerExecutor) {
            return builder().withCustomStateListenerExecutor(stateListenerExecutor);
        }

        public WithSuccessEventWithArg<RETURN_TYPE, ARG_TYPE, ObservableCachedFieldWithArg<RETURN_TYPE, ARG_TYPE>> withSuccessEvent(@Nonnull ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> event) {
            BusConstructorWrapperWithArg<RETURN_TYPE, ARG_TYPE, ObservableCachedFieldWithArg<RETURN_TYPE, ARG_TYPE>> builder = new BusConstructorWrapperWithArg<RETURN_TYPE, ARG_TYPE, ObservableCachedFieldWithArg<RETURN_TYPE, ARG_TYPE>>() {
                @Override
                public ObservableCachedFieldWithArg<RETURN_TYPE, ARG_TYPE> build(Provider<String> sessionIdProvider,
                                                                                 ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> successEvent, ResponseEventWithArg<Exception, ARG_TYPE> errorEvent, Bus customBus,
                                                                                 ExecutorService valueGetterExecutor, Executor stateListenerExecutor) {
                    OttoIBus iBus = new OttoIBus(customBus);
                    SuccessListenerWithArg<RETURN_TYPE, ARG_TYPE> additionalSuccessListener = new IBusSuccessListenerWithArg<>(iBus, successEvent);
                    ErrorListenerWithArg<ARG_TYPE> additionalErrorListener = new IBusErrorListenerWithArg<>(iBus, errorEvent);
                    return new ObservableCachedFieldWithArg<>(
                            sessionIdProvider,
                            provider,
                            additionalSuccessListener,
                            additionalErrorListener,
                            valueGetterExecutor,
                            stateListenerExecutor
                    );
                }
            };
            return new WithSuccessEventWithArg<>(event, builder);
        }

        private OverrideDefaultsSetter<ObservableCachedFieldWithArg<RETURN_TYPE, ARG_TYPE>> builder() {
            return new OverrideDefaultsSetter<>(new ConstructorWrapper<ObservableCachedFieldWithArg<RETURN_TYPE, ARG_TYPE>>() {
                @Override
                public ObservableCachedFieldWithArg<RETURN_TYPE, ARG_TYPE> build(Provider<String> sessionIdProvider, ExecutorService valueGetterExecutor, Executor stateListenerExecutor) {
                    return new ObservableCachedFieldWithArg<>(
                            sessionIdProvider,
                            provider,
                            new StubSuccessListenerWithArg<RETURN_TYPE, ARG_TYPE>(),
                            new StubErrorListenerWithArg<ARG_TYPE>(),
                            valueGetterExecutor,
                            stateListenerExecutor
                    );
                }
            });
        }

        public ObservableCachedFieldWithArg<RETURN_TYPE, ARG_TYPE> build() {
            return builder().build();
        }
    }

    public static class WithSuccessEventNoArg<RETURN_TYPE, FIELD_BUILD_TYPE> {
        final ResponseEvent<RETURN_TYPE> event;
        final BusConstructorWrapperNoArg<RETURN_TYPE, FIELD_BUILD_TYPE> constructorWrapper;

        protected WithSuccessEventNoArg(ResponseEvent<RETURN_TYPE> event,
                                        BusConstructorWrapperNoArg<RETURN_TYPE, FIELD_BUILD_TYPE> constructorWrapper) {
            this.event = event;
            this.constructorWrapper = constructorWrapper;
        }

        public WithErrorEventNoArg<RETURN_TYPE, FIELD_BUILD_TYPE> withErrorEvent(@Nullable ResponseEvent<Exception> errorEvent) {
            return new WithErrorEventNoArg<>(event, errorEvent, constructorWrapper);
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomBus(@Nonnull Bus customBus) {
            return new WithErrorEventNoArg<>(event, null, constructorWrapper).withCustomBus(customBus);
        }

        private OverrideDefaultsSetter<FIELD_BUILD_TYPE> withDefaultBus() {
            return withCustomBus(OttoCachedField.defaultBus);
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomSessionIdProvider(@Nonnull Provider<String> customSessionIdProvider) {
            return withDefaultBus().withCustomSessionIdProvider(customSessionIdProvider);
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomValueGetterExecutor(@Nonnull ExecutorService valueGetterExecutor) {
            return withDefaultBus().withCustomValueGetterExecutor(valueGetterExecutor);
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomStateListenerExecutor(@Nonnull Executor stateListenerExecutor) {
            return withDefaultBus().withCustomStateListenerExecutor(stateListenerExecutor);
        }

        public FIELD_BUILD_TYPE build() {
            return withDefaultBus().build();
        }
    }

    public static class WithSuccessEventWithArg<RETURN_TYPE, ARG_TYPE, FIELD_BUILD_TYPE> {
        final ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> event;
        final BusConstructorWrapperWithArg<RETURN_TYPE, ARG_TYPE, FIELD_BUILD_TYPE> constructorWrapper;

        protected WithSuccessEventWithArg(ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> event, BusConstructorWrapperWithArg<RETURN_TYPE, ARG_TYPE, FIELD_BUILD_TYPE> constructorWrapper) {
            this.event = event;
            this.constructorWrapper = constructorWrapper;
        }

        public WithErrorEventWithArg<RETURN_TYPE, ARG_TYPE, FIELD_BUILD_TYPE> withErrorEvent(ResponseEventWithArg<Exception, ARG_TYPE> errorEvent) {
            return new WithErrorEventWithArg<>(event, errorEvent, constructorWrapper);
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomBus(Bus customBus) {
            return new WithErrorEventWithArg<>(event, null, constructorWrapper).withCustomBus(customBus);
        }

        private OverrideDefaultsSetter<FIELD_BUILD_TYPE> withDefaultBus() {
            return withCustomBus(OttoCachedField.defaultBus);
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomSessionIdProvider(Provider<String> customSessionIdProvider) {
            return withDefaultBus().withCustomSessionIdProvider(customSessionIdProvider);
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomValueGetterExecutor(ExecutorService valueGetterExecutor) {
            return withDefaultBus().withCustomValueGetterExecutor(valueGetterExecutor);
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomStateListenerExecutor(Executor stateListenerExecutor) {
            return withDefaultBus().withCustomStateListenerExecutor(stateListenerExecutor);
        }

        public FIELD_BUILD_TYPE build() {
            return withDefaultBus().build();
        }
    }

    public static class WithErrorEventNoArg<RETURN_TYPE, FIELD_BUILD_TYPE> {
        final ResponseEvent<RETURN_TYPE> event;
        @Nullable
        final ResponseEvent<Exception> errorEvent;
        final BusConstructorWrapperNoArg<RETURN_TYPE, FIELD_BUILD_TYPE> constructorWrapper;

        public WithErrorEventNoArg(ResponseEvent<RETURN_TYPE> event, @Nullable ResponseEvent<Exception> errorEvent,
                                   BusConstructorWrapperNoArg<RETURN_TYPE, FIELD_BUILD_TYPE> constructorWrapper) {
            this.event = event;
            this.errorEvent = errorEvent;
            this.constructorWrapper = constructorWrapper;
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomBus(final Bus customBus) {
            return new OverrideDefaultsSetter<>(new ConstructorWrapper<FIELD_BUILD_TYPE>() {
                @Override
                public FIELD_BUILD_TYPE build(Provider<String> sessionIdProvider, ExecutorService valueGetterExecutor, Executor stateListenerExecutor) {
                    validateNotNull(valueGetterExecutor, stateListenerExecutor);
                    return constructorWrapper.build(sessionIdProvider, event, errorEvent, customBus, valueGetterExecutor, stateListenerExecutor);
                }
            });
        }

        private OverrideDefaultsSetter<FIELD_BUILD_TYPE> withDefaultBus() {
            return withCustomBus(OttoCachedField.defaultBus);
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomSessionIdProvider(Provider<String> customSessionIdProvider) {
            return withDefaultBus().withCustomSessionIdProvider(customSessionIdProvider);
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomValueGetterExecutor(ExecutorService valueGetterExecutor) {
            return withDefaultBus().withCustomValueGetterExecutor(valueGetterExecutor);
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomStateListenerExecutor(Executor stateListenerExecutor) {
            return withDefaultBus().withCustomStateListenerExecutor(stateListenerExecutor);
        }

        public FIELD_BUILD_TYPE build() {
            return withCustomBus(OttoCachedField.defaultBus).build();
        }
    }

    public static class WithErrorEventWithArg<RETURN_TYPE, ARG_TYPE, FIELD_BUILD_TYPE> {
        final ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> event;
        final ResponseEventWithArg<Exception, ARG_TYPE> errorEvent;
        final BusConstructorWrapperWithArg<RETURN_TYPE, ARG_TYPE, FIELD_BUILD_TYPE> constructorWrapper;

        public WithErrorEventWithArg(ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> event, ResponseEventWithArg<Exception, ARG_TYPE> errorEvent, BusConstructorWrapperWithArg<RETURN_TYPE, ARG_TYPE, FIELD_BUILD_TYPE> constructorWrapper) {
            this.event = event;
            this.errorEvent = errorEvent;
            this.constructorWrapper = constructorWrapper;
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomBus(@Nonnull final Bus customBus) {
            return new OverrideDefaultsSetter<>(new ConstructorWrapper<FIELD_BUILD_TYPE>() {
                @Override
                public FIELD_BUILD_TYPE build(Provider<String> sessionIdProvider, ExecutorService valueGetterExecutor, Executor stateListenerExecutor) {
                    validateNotNull(valueGetterExecutor, stateListenerExecutor);
                    return constructorWrapper.build(sessionIdProvider, event, errorEvent, customBus, valueGetterExecutor, stateListenerExecutor);
                }
            });
        }


        private OverrideDefaultsSetter<FIELD_BUILD_TYPE> withDefaultBus() {
            return withCustomBus(OttoCachedField.defaultBus);
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomSessionIdProvider(Provider<String> customSessionIdProvider) {
            return withDefaultBus().withCustomSessionIdProvider(customSessionIdProvider);
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomValueGetterExecutor(ExecutorService valueGetterExecutor) {
            return withDefaultBus().withCustomValueGetterExecutor(valueGetterExecutor);
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomStateListenerExecutor(Executor stateListenerExecutor) {
            return withDefaultBus().withCustomStateListenerExecutor(stateListenerExecutor);
        }

        public FIELD_BUILD_TYPE build() {
            return withCustomBus(OttoCachedField.defaultBus).build();
        }
    }

    public static class OverrideDefaultsSetter<FIELD_BUILD_TYPE> {
        private final ConstructorWrapper<FIELD_BUILD_TYPE> constructorWrapper;
        private Provider<String> sessionIdProvider;
        private ExecutorService valueGetterExecutor;
        private Executor stateListenerExecutor;

        protected OverrideDefaultsSetter(ConstructorWrapper<FIELD_BUILD_TYPE> constructorWrapper) {
            this.constructorWrapper = constructorWrapper;
            sessionIdProvider = OttoCachedField.defaultSessionIdProvider;
            valueGetterExecutor = OttoCachedField.defaultValueGetterExecutor;
            stateListenerExecutor = OttoCachedField.defaultStateListenerExecutor;
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomSessionIdProvider(@Nonnull Provider<String> sessionIdProvider) {
            OverrideDefaultsSetter.this.sessionIdProvider = sessionIdProvider;
            return this;
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomValueGetterExecutor(@Nonnull ExecutorService valueGetterExecutor) {
            OverrideDefaultsSetter.this.valueGetterExecutor = valueGetterExecutor;
            return this;
        }

        public OverrideDefaultsSetter<FIELD_BUILD_TYPE> withCustomStateListenerExecutor(@Nonnull Executor stateListenerExecutor) {
            OverrideDefaultsSetter.this.stateListenerExecutor = stateListenerExecutor;
            return this;
        }

        public FIELD_BUILD_TYPE build() {
            validateNotNull(valueGetterExecutor, stateListenerExecutor);
            return constructorWrapper.build(sessionIdProvider, valueGetterExecutor, stateListenerExecutor);
        }
    }

    private static void validateNotNull(Object... values) throws NullArgumentException {
        for (Object value : values) {
            if (value == null) {
                throw new NullArgumentException();
            }
        }
    }

    protected interface ConstructorWrapper<FIELD_BUILD_TYPE> {
        @Nonnull
        FIELD_BUILD_TYPE build(Provider<String> sessionIdProvider, ExecutorService valueGetterExecutor, Executor stateListenerExecutor);
    }

    protected interface BusConstructorWrapperNoArg<RETURN_TYPE, FIELD_BUILD_TYPE> {
        @Nonnull
        FIELD_BUILD_TYPE build(Provider<String> sessionIdProvider,
                               ResponseEvent<RETURN_TYPE> successEvent, ResponseEvent<Exception> errorEvent, Bus customBus,
                               ExecutorService valueGetterExecutor, Executor stateListenerExecutor);
    }

    protected interface BusConstructorWrapperWithArg<RETURN_TYPE, ARG_TYPE, FIELD_BUILD_TYPE> {
        @Nonnull
        FIELD_BUILD_TYPE build(Provider<String> sessionIdProvider,
                               ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> successEvent, ResponseEventWithArg<Exception, ARG_TYPE> errorEvent, Bus customBus,
                               ExecutorService valueGetterExecutor, Executor stateListenerExecutor);
    }
}
