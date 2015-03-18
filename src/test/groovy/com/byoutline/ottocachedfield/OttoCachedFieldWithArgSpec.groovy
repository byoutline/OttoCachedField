package com.byoutline.ottocachedfield

import com.byoutline.ottocachedfield.events.ResponseEventWithArg
import com.squareup.otto.Bus
import spock.lang.Shared
import spock.lang.Unroll

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com> on 27.06.14.
 */
class OttoCachedFieldWithArgSpec extends spock.lang.Specification {
    @Shared
    Map<Integer, String> argToValueMap = [1: 'a', 2: 'b']
    @Shared
    ResponseEventWithArg<String, Integer> successEvent
    ResponseEventWithArg<Exception, Integer> errorEvent
    Bus bus

    def setup() {
        bus = Mock()
        successEvent = Mock()
        errorEvent = Mock()

        OttoCachedField.init(MockFactory.getSameSessionIdProvider(), bus)
    }

    @Unroll
    def "should post value: #val , error times: #eC for arg: #arg"() {
        given:
        OttoCachedFieldWithArg field = OttoCachedFieldWithArg.builder()
                .withValueProvider(MockFactory.getStringGetter(argToValueMap))
                .withSuccessEvent(successEvent)
                .withResponseErrorEvent(errorEvent)
                .build();
        when:
        field.postValue(arg)
        sleep 3

        then:

        sC * successEvent.setResponse(val, arg)
        0 * errorEvent.setResponse(_, _)

        where:
        val  | arg | sC
        null | 0   | 1
        'a'  | 1   | 1
        'b'  | 2   | 1
    }

    def "postValue should post error with argument"() {
        given:
        Exception errorVal = null;
        Integer errorArg = null;
        ResponseEventWithArg<Exception, Integer> errorEvent =
                { Exception val, Integer arg ->
                    errorVal = val; errorArg = arg
                } as ResponseEventWithArg<Exception, Integer>
        OttoCachedFieldWithArg field = OttoCachedFieldWithArg.builder()
                .withValueProvider(MockFactory.getFailingStringGetterWithArg())
                .withSuccessEvent(successEvent)
                .withResponseErrorEvent(errorEvent)
                .build();
        when:
        field.postValue(2)
        sleep 3

        then:
        errorVal.message == "E2"
        errorArg == 2
    }

}
