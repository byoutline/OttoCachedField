package com.byoutline.ottocachedfield

import com.byoutline.cachedfield.CachedFieldWithArg
import com.byoutline.cachedfield.FieldState
import com.byoutline.cachedfield.FieldStateListener
import com.byoutline.ottocachedfield.events.ResponseEventWithArg
import com.squareup.otto.Bus
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import javax.inject.Provider

import static com.byoutline.ottocachedfield.MockFactory.failingStringGetterWithArg
import static com.byoutline.ottocachedfield.MockFactory.getStringGetter

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com> on 27.06.14.
 */
class PostingToBusObservableFieldWithArgSpec extends Specification {
    @Shared
    Map<Integer, String> argToValueMap = [1: 'a', 2: 'b']
    @Shared
    ResponseEventWithArg<String, Integer> successEvent
    ResponseEventWithArg<Exception, Integer> errorEvent
    Bus bus

    static <ARG_TYPE> void postAndWaitUntilFieldStopsLoading(CachedFieldWithArg<?, ARG_TYPE> field, ARG_TYPE arg) {
        boolean duringValueLoad = true
        def listener = { FieldState newState ->
            if (newState == FieldState.NOT_LOADED || newState == FieldState.LOADED) {
                duringValueLoad = false
            }
        } as FieldStateListener

        field.addStateListener(listener)
        field.postValue(arg)
        while (duringValueLoad) {
            sleep 1
        }
        field.removeStateListener(listener)
        sleep 8 // wait for event to be posted
    }

    def setup() {
        bus = Mock()
        successEvent = Mock()
        errorEvent = Mock()

        OttoCachedField.init(MockFactory.getSameSessionIdProvider(), bus)
    }

    @Unroll
    def "should post value: #val #text, times: #sC for arg: #arg when created by: #builder.class.simpleName"() {
        given:
        CachedFieldWithArg field = new CachedFieldBuilder()
                .withValueProviderWithArg(getStringGetter(argToValueMap))
                .asObservable()
                .withSuccessEvent(successEvent)
                .withErrorEvent(errorEvent)
                .build();
        when:
        postAndWaitUntilFieldStopsLoading(field, arg)

        then:

        sC * successEvent.setResponse(val, arg)
        0 * errorEvent.setResponse(_, _)

        where:
        val  | arg || sC
        null | 0   || 1
        'a'  | 1   || 1
        'b'  | 2   || 1
    }

    @Unroll
    def "postValue should post error with argument when created by: #name"() {
        given:
        Exception errorVal = null;
        Integer errorArg = null;
        ResponseEventWithArg<Exception, Integer> errorEvent =
                { Exception val, Integer arg ->
                    errorVal = val; errorArg = arg
                } as ResponseEventWithArg<Exception, Integer>
        CachedFieldWithArg field = builder
                .withSuccessEvent(successEvent)
                .withErrorEvent(errorEvent)
                .build();
        when:
        postAndWaitUntilFieldStopsLoading(field, 2)

        then:
        errorVal.message == "E2"
        errorArg == 2

        where:
        name            | builder
        "otto"          | new CachedFieldBuilder().withValueProviderWithArg(getFailingStringGetterWithArg())
        "observable"    | new CachedFieldBuilder().withValueProviderWithArg(getFailingStringGetterWithArg()).asObservable()
    }

    @Unroll
    def "custom bus passed to builder should be used instead of default when created by: #name"() {
        given:
        def sessionProv = { return "custom" } as Provider<String>
        Bus customBus = Mock()
        CachedFieldWithArg field = builder
                .withSuccessEvent(successEvent)
                .withErrorEvent(errorEvent)
                .withCustomBus(customBus)
                .withCustomSessionIdProvider(sessionProv)
                .build();

        when:
        postAndWaitUntilFieldStopsLoading(field, 1)

        then:
        1 * customBus.post(_)
        0 * bus.post(_)

        where:
        name            | builder
        "otto"          | new CachedFieldBuilder().withValueProviderWithArg(getStringGetter(argToValueMap))
        "observable"    | new CachedFieldBuilder().withValueProviderWithArg(getStringGetter(argToValueMap)).asObservable()
    }
}
