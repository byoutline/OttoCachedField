package com.byoutline.ottocachedfield

import com.byoutline.eventcallback.ResponseEvent
import com.squareup.otto.Bus
import spock.lang.Shared
import spock.lang.Unroll

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com> on 27.06.14.
 */
class OttoCachedFieldSpec extends spock.lang.Specification {
    @Shared
    String value = "value"
    @Shared
    Exception exception = new RuntimeException("Cached Field test exception")
    ResponseEvent<String> successEvent
    ResponseEvent<Exception> errorEvent
    Bus bus

    def setup() {
        bus = Mock()
        successEvent = Mock()
        errorEvent = Mock()

        OttoCachedField.init(MockFactory.getSameSessionIdProvider(), bus)
    }

    def "postValue should return immediately"() {
        given:
        OttoCachedField field = OttoCachedField.builder()
                .withValueProvider(MockFactory.getDelayedStringGetter(value, 1000))
                .withSuccessEvent(successEvent)
                .build();

        when:
        boolean tookToLong = false
        Thread.start {
            sleep 15
            tookToLong = true;
        }
        field.postValue()

        then:
        if (tookToLong) {
            throw new AssertionError("Test took to long to execute")
        }
    }

    @Unroll
    def "should post success times: #sC, error times: #eC for valueProvider: #valProv"() {
        when:
        OttoCachedField field = OttoCachedField.builder()
                .withValueProvider(valProv)
                .withSuccessEvent(successEvent)
                .withResponseErrorEvent(errorEvent)
                .build();
        field.postValue()
        sleep 3

        then:
        sC * successEvent.setResponse(value)
        eC * errorEvent.setResponse(exception)

        where:
        sC | eC | valProv
        0  | 1  | MockFactory.getFailingStringGetter(exception)
        1  | 0  | MockFactory.getStringGetter(value)
    }

    def "postValue should post generic error"() {
        given:
        Object expEvent = "exp"
        OttoCachedField field = OttoCachedField.builder()
                .withValueProvider(MockFactory.getFailingStringGetter(exception))
                .withSuccessEvent(successEvent)
                .withGenericErrorEvent(expEvent)
                .build();

        when:
        field.postValue()
        sleep 3

        then:
        1 * bus.post(expEvent)
    }

    def "2 arg constructor should post on default bus"() {
        given:
        def field = new OttoCachedField(MockFactory.getStringGetter(value), successEvent)

        when:
        field.postValue()
        sleep 3

        then:
        1 * bus.post(_)
    }

    def "3 arg constructor with error ResponseEvent should post on default bus"() {
        given:
        def field = new OttoCachedField(MockFactory.getStringGetter(value), successEvent, errorEvent)

        when:
        field.postValue()
        sleep 3

        then:
        1 * bus.post(_)
    }

    def "3 arg constructor with generic error event should post on default bus"() {
        given:
        def field = new OttoCachedField(MockFactory.getStringGetter(value), successEvent, new Object())

        when:
        field.postValue()
        sleep 3

        then:
        1 * bus.post(_)
    }


}
