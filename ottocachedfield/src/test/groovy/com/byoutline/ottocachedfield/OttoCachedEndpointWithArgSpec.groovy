package com.byoutline.ottocachedfield

import com.byoutline.cachedfield.cachedendpoint.CallResult
import com.byoutline.cachedfield.cachedendpoint.EndpointState
import com.byoutline.cachedfield.cachedendpoint.StateAndValue
import com.google.common.util.concurrent.MoreExecutors
import com.squareup.otto.Bus
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com> on 27.06.14.
 */
class OttoCachedEndpointWithArgSpec extends Specification {
    @Shared
    Map<Integer, String> argToValueMap = [1: 'a', 2: 'b']
    @Shared
    com.byoutline.ibuscachedfield.events.ResponseEventWithArg<StateAndValue<String, Integer>, Integer> resultEvent
    Bus bus

    def setup() {
        bus = Mock()
        resultEvent = Mock()

        OttoCachedField.init(MockFactory.getSameSessionIdProvider(), bus)
    }

    @Unroll
    def "should post value: #val , times: #sC for arg: #arg"() {
        given:
        OttoCachedEndpointWithArg field = OttoCachedEndpointWithArg.builder()
                .withValueProvider(MockFactory.getStringGetter(argToValueMap))
                .withResultEvent(resultEvent)
                .withCustomValueGetterExecutor(MoreExecutors.newDirectExecutorService())
                .build();
        when:
        field.call(arg)

        then:

        sC * resultEvent.setResponse(
                StateAndValue.create(EndpointState.CALL_SUCCESS,
                        CallResult.create(val, null), arg), arg)

        where:
        val  | arg | sC
        null | 0   | 1
        'a'  | 1   | 1
        'b'  | 2   | 1
    }

    def "postValue should post error with argument"() {
        given:
        OttoCachedEndpointWithArg field = OttoCachedEndpointWithArg.builder()
                .withValueProvider(MockFactory.getFailingStringGetterWithArg())
                .withResultEvent(resultEvent)
                .withCustomValueGetterExecutor(MoreExecutors.newDirectExecutorService())
                .build();
        when:
        field.call(2)

        then:
        def result = field.getStateAndValue()
        result.state == EndpointState.CALL_FAILED
        result.arg == 2
        result.value.successResult == null
        result.value.failureResult.getMessage() == "E2"
    }
}
