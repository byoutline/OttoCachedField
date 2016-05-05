package com.byoutline.ottocachedfield

import com.byoutline.cachedfield.CachedField
import com.byoutline.cachedfield.FieldState
import com.byoutline.cachedfield.FieldStateListener
import com.byoutline.eventcallback.ResponseEvent
import com.squareup.otto.Bus
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import javax.inject.Provider

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com> on 27.06.14.
 */
class OttoCachedFieldSpec extends Specification {
    @Shared
    String value = "value"
    @Shared
    Exception exception = new RuntimeException("Cached Field test exception")
    ResponseEvent<String> successEvent
    ResponseEvent<Exception> errorEvent
    Bus bus

    static void postAndWaitUntilFieldStopsLoading(CachedField field) {
        boolean duringValueLoad = true
        boolean loadingStarted = false
        def listener = { FieldState newState ->
            switch(newState) {
                case FieldState.NOT_LOADED:
                    if (loadingStarted) duringValueLoad = false
                    break
                case FieldState.CURRENTLY_LOADING:
                    loadingStarted = true
                    break
                case FieldState.LOADED:
                    duringValueLoad = false
                    break
            }
        } as FieldStateListener

        field.addStateListener(listener)
        field.postValue()
        while (duringValueLoad) {
            sleep 1
        }
        field.removeStateListener(listener)
        sleep 4
    }

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
            sleep 45
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
        given:
        CachedField field = OttoCachedField.builder()
                .withValueProvider(valProv)
                .withSuccessEvent(successEvent)
                .withResponseErrorEvent(errorEvent)
                .build();
        when:
        postAndWaitUntilFieldStopsLoading(field)

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
        CachedField field = OttoCachedField.builder()
                .withValueProvider(MockFactory.getFailingStringGetter(exception))
                .withSuccessEvent(successEvent)
                .withGenericErrorEvent(expEvent)
                .build();

        when:
        postAndWaitUntilFieldStopsLoading(field)

        then:
        1 * bus.post(expEvent)
    }

    def "2 arg constructor should post on default bus"() {
        given:
        def field = new OttoCachedField(MockFactory.getStringGetter(value), successEvent)

        when:
        postAndWaitUntilFieldStopsLoading(field)

        then:
        1 * bus.post(_)
    }

    def "3 arg constructor with error ResponseEvent should post on default bus"() {
        given:
        def field = new OttoCachedField(MockFactory.getStringGetter(value), successEvent, errorEvent)

        when:
        postAndWaitUntilFieldStopsLoading(field)

        then:
        1 * bus.post(_)
    }

    def "3 arg constructor with generic error event should post on default bus"() {
        given:
        def field = new OttoCachedField(MockFactory.getStringGetter(value), successEvent, new Object())

        when:
        postAndWaitUntilFieldStopsLoading(field)

        then:
        1 * bus.post(_)
    }

    def "when custom bus passed to builder it should be used instead of default"() {
        given:
        def sessionProv = { return "custom" } as Provider<String>
        Bus customBus = Mock()
        CachedField field = OttoCachedField.builder()
                .withValueProvider(MockFactory.getStringGetter("val"))
                .withSuccessEvent(successEvent)
                .withResponseErrorEvent(errorEvent)
                .withCustomSessionIdProvider(sessionProv)
                .withCustomBus(customBus)
                .build();

        when:
        postAndWaitUntilFieldStopsLoading(field)

        then:
        1 * customBus.post(_)
        0 * bus.post(_)
    }
}
