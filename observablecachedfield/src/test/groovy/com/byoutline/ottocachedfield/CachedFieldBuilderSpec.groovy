package com.byoutline.ottocachedfield

import com.byoutline.cachedfield.cachedendpoint.CachedEndpointWithArg
import com.byoutline.cachedfield.cachedendpoint.StateAndValue
import com.byoutline.cachedfield.internal.DefaultExecutors
import com.byoutline.eventcallback.ResponseEvent
import com.byoutline.eventcallback.ResponseEventImpl
import com.byoutline.ibuscachedfield.events.ResponseEventWithArg
import com.byoutline.observablecachedfield.ObservableCachedField
import com.byoutline.observablecachedfield.ObservableCachedFieldWithArg
import com.byoutline.ottocachedfield.events.ResponseEventWithArgImpl
import com.google.common.util.concurrent.MoreExecutors
import com.squareup.otto.Bus
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import javax.inject.Provider
import java.util.concurrent.Executor

import static com.byoutline.ottocachedfield.MockFactory.getStringGetter
import static com.byoutline.ottocachedfield.MockFactory.sameSessionIdProvider

/**
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class CachedFieldBuilderSpec extends Specification {

    @Shared
    String value = "val"
    @Shared
    Map<Integer, String> argToValueMap = [1: 'a', 2: 'b']
    @Shared
    Bus bus

    @Shared
    ResponseEvent<String> successEvent = new ResponseEventImpl<>()
    @Shared
    ResponseEventWithArg<String, Integer> successEventWithArg = new ResponseEventWithArgImpl<>()
    @Shared
    ResponseEventWithArg<StateAndValue<String, Integer>, Integer> responseEventWithArg = new ResponseEventWithArgImpl<>()
    @Shared
    ResponseEvent<Exception> errorEvent = new ResponseEventImpl<>()
    @Shared
    ResponseEventWithArg<Exception, Integer> errorEventWithArg = new ResponseEventWithArgImpl<>()

    def setupSpec() {
        bus = Mock()
        OttoCachedField.init(getSameSessionIdProvider(), bus)
    }

    @Unroll
    def "builder #name should create instance of OttoCachedField"() {
        when:
        def result = builder.build()

        then:
        result instanceof OttoCachedField

        where:
        name                          | builder
        'successEvent'                | inst()
                .withValueProvider(getStringGetter(value))
                .withSuccessEvent(successEvent)
        'errorEvent'                  | inst()
                .withValueProvider(getStringGetter(value))
                .withSuccessEvent(successEvent)
                .withErrorEvent(errorEvent)
        'customSessionIdProv'         | inst()
                .withValueProvider(getStringGetter(value))
                .withSuccessEvent(successEvent)
                .withCustomSessionIdProvider(getSameSessionIdProvider())
        'customBus'                   | inst()
                .withValueProvider(getStringGetter(value))
                .withSuccessEvent(successEvent)
                .withCustomBus(bus)
        'customStateListenerExecutor' | inst()
                .withValueProvider(getStringGetter(value))
                .withSuccessEvent(successEvent)
                .withCustomStateListenerExecutor(DefaultExecutors.createDefaultStateListenerExecutor())
        'customValueGetterExecutor'   | inst()
                .withValueProvider(getStringGetter(value))
                .withSuccessEvent(successEvent)
                .withCustomValueGetterExecutor(DefaultExecutors.createDefaultValueGetterExecutor())
    }

    @Unroll
    def "builder #name should create instance of OttoCachedFieldWithArg"() {
        when:
        def result = builder.build()

        then:
        result instanceof OttoCachedFieldWithArg

        where:
        name                          | builder
        'successEvent'                | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .withSuccessEvent(successEventWithArg)
        'errorEvent'                  | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .withSuccessEvent(successEventWithArg)
                .withErrorEvent(errorEventWithArg)
        'customSessionIdProv'         | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .withSuccessEvent(successEventWithArg)
                .withCustomSessionIdProvider(getSameSessionIdProvider())
        'customBus'                   | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .withSuccessEvent(successEventWithArg)
                .withCustomBus(bus)
        'customStateListenerExecutor' | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .withSuccessEvent(successEventWithArg)
                .withCustomStateListenerExecutor(DefaultExecutors.createDefaultStateListenerExecutor())
        'customValueGetterExecutor'   | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .withSuccessEvent(successEventWithArg)
                .withCustomValueGetterExecutor(DefaultExecutors.createDefaultValueGetterExecutor())
    }

    @Unroll
    def "builder #name should create instance of ObservableCachedField"() {
        when:
        def result = builder.build()

        then:
        result instanceof ObservableCachedField

        where:
        name                          | builder
        'successEvent'                | inst()
                .withValueProvider(getStringGetter(value))
                .asObservable()
        'customSessionIdProv'         | inst()
                .withValueProvider(getStringGetter(value))
                .asObservable()
                .withCustomSessionIdProvider(getSameSessionIdProvider())
        'customStateListenerExecutor' | inst()
                .withValueProvider(getStringGetter(value))
                .asObservable()
                .withCustomStateListenerExecutor(DefaultExecutors.createDefaultStateListenerExecutor())
        'customValueGetterExecutor'   | inst()
                .withValueProvider(getStringGetter(value))
                .asObservable()
                .withCustomValueGetterExecutor(DefaultExecutors.createDefaultValueGetterExecutor())
        'successEvent'                | inst()
                .withValueProvider(getStringGetter(value))
                .asObservable()
                .withSuccessEvent(successEvent)
        'errorEvent'                  | inst()
                .withValueProvider(getStringGetter(value))
                .asObservable()
                .withSuccessEvent(successEvent)
                .withErrorEvent(errorEvent)
    }

    @Unroll
    def "builder #name should create instance of ObservableCachedFieldWithArg"() {
        when:
        def result = builder.build()

        then:
        result instanceof ObservableCachedFieldWithArg

        where:
        name                          | builder
        'successEvent'                | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asObservable()
        'customSessionIdProv'         | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asObservable()
                .withCustomSessionIdProvider(getSameSessionIdProvider())
        'customStateListenerExecutor' | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asObservable()
                .withCustomStateListenerExecutor(DefaultExecutors.createDefaultStateListenerExecutor())
        'customValueGetterExecutor'   | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asObservable()
                .withCustomValueGetterExecutor(DefaultExecutors.createDefaultValueGetterExecutor())
        'successEvent'                | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asObservable()
                .withSuccessEvent(successEventWithArg)
        'errorEvent'                  | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asObservable()
                .withSuccessEvent(successEventWithArg)
                .withErrorEvent(errorEventWithArg)
    }

    @Unroll
    def "builder #name should create instance of OttoCachedEndpointWithArg"() {
        when:
        def result = builder.build()

        then:
        result instanceof OttoCachedEndpointWithArg

        where:
        name                          | builder
        'responseEvent'               | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asEndpoint(responseEventWithArg)
        'customSessionIdProv'         | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asEndpoint(responseEventWithArg)
                .withCustomSessionIdProvider(getSameSessionIdProvider())
        'customBus'                   | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asEndpoint()
                .withCustomBus(bus)
        'customStateListenerExecutor' | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asEndpoint()
                .withCustomStateListenerExecutor(DefaultExecutors.createDefaultStateListenerExecutor())
        'customValueGetterExecutor'   | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asEndpoint()
                .withCustomValueGetterExecutor(DefaultExecutors.createDefaultValueGetterExecutor())
    }

    def "#name no arg builder should set CustomBus"() {
        given:
        def differentBus = Mock(Bus)
        when:
        def field = builder
                .withSuccessEvent(successEvent)
                .withCustomBus(differentBus)
                .withCustomValueGetterExecutor(MoreExecutors.newDirectExecutorService())
                .build()
        field.postValue()
        then:
        1 * differentBus.post(successEvent)
        where:
        name   | builder
        'cF'   | inst()
                .withValueProvider(getStringGetter(value))
        'obsF' | inst()
                .withValueProvider(getStringGetter(value))
                .asObservable()
    }

    def "#name with arg builder should set CustomBus"() {
        given:
        def differentBus = Mock(Bus)
        when:
        def field = builder
                .withCustomBus(differentBus)
                .withCustomValueGetterExecutor(MoreExecutors.newDirectExecutorService())
                .build()
        if (field instanceof CachedEndpointWithArg) {
            field.call(1)
        } else {
            field.postValue(1)
        }
        then:
        1 * differentBus.post(_)
        where:
        name       | builder
        'cF'       | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .withSuccessEvent(successEventWithArg)
        'obsF'     | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asObservable()
                .withSuccessEvent(successEventWithArg)
        'endpoint' | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asEndpoint(responseEventWithArg)
    }

    def "#name no arg builder should set CustomSessionIdProvider"() {
        given:
        boolean customProviderCalled = false
        Provider<String> prov = new Provider<String>() {

            @Override
            String get() {
                customProviderCalled = true
                return 'a'
            }
        }
        when:
        def field = builder
                .withCustomSessionIdProvider(prov)
                .withCustomValueGetterExecutor(MoreExecutors.newDirectExecutorService())
                .build()
        field.postValue()
        then:
        customProviderCalled
        where:
        name   | builder
        'cF'   | inst()
                .withValueProvider(getStringGetter(value))
                .withSuccessEvent(successEvent)
        'obsF no Bus' | inst()
                .withValueProvider(getStringGetter(value))
                .asObservable()
        'obsF with Bus' | inst()
                .withValueProvider(getStringGetter(value))
                .asObservable()
                .withSuccessEvent(successEvent)
    }

    def "#name with arg builder should set CustomSessionIdProvider"() {
        given:
        boolean customProviderCalled = false
        Provider<String> prov = new Provider<String>() {

            @Override
            String get() {
                customProviderCalled = true
                return 'a'
            }
        }
        when:
        def field = builder
                .withCustomSessionIdProvider(prov)
                .withCustomValueGetterExecutor(MoreExecutors.newDirectExecutorService())
                .build()
        if (field instanceof CachedEndpointWithArg) {
            field.call(1)
        } else {
            field.postValue(1)
        }
        then:
        customProviderCalled
        where:
        name            | builder
        'cF'            | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .withSuccessEvent(successEventWithArg)
        'obsF no Bus'   | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asObservable()
        'obsF with Bus' | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asObservable()
                .withSuccessEvent(successEventWithArg)
        'endpoint'      | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asEndpoint(responseEventWithArg)
    }

    def "#name no arg builder should set CustomStateListenerExecutor"() {
        given:
        boolean customExecutorCalled = false
        Executor stateListenerExecutor = new Executor() {
            @Override
            void execute(Runnable command) {
                customExecutorCalled = true
                command.run()
            }
        }
        when:
        def field = builder
                .withCustomValueGetterExecutor(MoreExecutors.newDirectExecutorService())
                .withCustomStateListenerExecutor(stateListenerExecutor)
                .build()
        field.postValue()
        then:
        customExecutorCalled
        where:
        name   | builder
        'cF'   | inst()
                .withValueProvider(getStringGetter(value))
                .withSuccessEvent(successEvent)
        'obsF no Bus' | inst()
                .withValueProvider(getStringGetter(value))
                .asObservable()
        'obsF with Bus' | inst()
                .withValueProvider(getStringGetter(value))
                .asObservable()
                .withSuccessEvent(successEvent)
    }

    def "#name with arg builder should set CustomStateListenerExecutor"() {
        given:
        boolean customExecutorCalled = false
        Executor stateListenerExecutor = new Executor() {
            @Override
            void execute(Runnable command) {
                customExecutorCalled = true
                command.run()
            }
        }
        when:
        def field = builder
                .withCustomValueGetterExecutor(MoreExecutors.newDirectExecutorService())
                .withCustomStateListenerExecutor(stateListenerExecutor)
                .build()
        then:
        if (field instanceof CachedEndpointWithArg) {
            field.call(1)
        } else {
            field.postValue(1)
        }
        then:
        customExecutorCalled
        where:
        name            | builder
        'cF'            | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .withSuccessEvent(successEventWithArg)
        'obsF no Bus'   | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asObservable()
        'obsF with Bus' | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asObservable()
                .withSuccessEvent(successEventWithArg)
        'endpoint'      | inst()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asEndpoint(responseEventWithArg)
    }

    private CachedFieldBuilder inst() {
        new CachedFieldBuilder()
    }
}
