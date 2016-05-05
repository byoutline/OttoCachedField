package com.byoutline.ottocachedfield

import com.byoutline.eventcallback.ResponseEvent
import com.byoutline.eventcallback.ResponseEventImpl
import com.byoutline.ibuscachedfield.events.ResponseEventWithArg
import com.byoutline.ibuscachedfield.events.ResponseEventWithArgImpl
import com.google.common.util.concurrent.MoreExecutors
import com.squareup.otto.Bus
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.FutureTask

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com> on 27.06.14.
 */
class CustomExecutorsSpec extends Specification {
    @Shared
    String value = "value"
    @Shared
    Map<Integer, String> argToValueMap = [1: 'a', 2: 'b']
    Bus bus

    def setup() {
        bus = Mock()
        OttoCachedField.init(MockFactory.getSameSessionIdProvider(), bus)
    }

    def "should use passed executor for loading data no arg"() {
        given:
        boolean called = false
        ResponseEvent<String> successEvent = new ResponseEventImpl<>()
        ResponseEvent<Exception> errorEvent = new ResponseEventImpl<>()
        ExecutorService executor = [
                submit: { called = true; return new FutureTask((Runnable) it, null); }
        ] as ExecutorService
        OttoCachedField field = OttoCachedField.builder()
                .withValueProvider(MockFactory.getStringGetter(value))
                .withSuccessEvent(successEvent)
                .withGenericErrorEvent(errorEvent)
                .withCustomValueGetterExecutor(executor)
                .build();

        when:
        field.postValue()

        then:
        called
    }

    def "should use passed executor for state listener no arg"() {
        given:
        boolean called = false
        ResponseEvent<String> successEvent = new ResponseEventImpl<>()
        ResponseEvent<Exception> errorEvent = new ResponseEventImpl<>()
        Executor stateListenersExecutor = { called = true; it.run() } as Executor
        OttoCachedField field = OttoCachedField.builder()
                .withValueProvider(MockFactory.getStringGetter(value))
                .withSuccessEvent(successEvent)
                .withGenericErrorEvent(errorEvent)
                .withCustomValueGetterExecutor(MoreExecutors.newDirectExecutorService())
                .withCustomStateListenerExecutor(stateListenersExecutor)
                .build();

        when:
        field.postValue()

        then:
        called
    }


    def "should use passed executor for loading data with arg"() {
        given:
        boolean called = false
        ResponseEventWithArg<String, Integer> successEvent = new ResponseEventWithArgImpl<>()
        ResponseEventWithArg<Exception, Integer> errorEvent = new ResponseEventWithArgImpl<>()
        ExecutorService executor = [
                submit: { called = true; return new FutureTask((Runnable) it, null); }
        ] as ExecutorService
        OttoCachedFieldWithArg field = OttoCachedFieldWithArg.builder()
                .withValueProvider(MockFactory.getStringGetter(argToValueMap))
                .withSuccessEvent(successEvent)
                .withResponseErrorEvent(errorEvent)
                .withCustomValueGetterExecutor(executor)
                .build();

        when:
        field.postValue()

        then:
        called
    }

    def "should use passed executor for state listener with arg"() {
        given:
        boolean called = false
        ResponseEventWithArg<String, Integer> successEvent = new ResponseEventWithArgImpl<>()
        ResponseEventWithArg<Exception, Integer> errorEvent = new ResponseEventWithArgImpl<>()
        Executor stateListenersExecutor = { called = true; it.run() } as Executor
        OttoCachedFieldWithArg field = OttoCachedFieldWithArg.builder()
                .withValueProvider(MockFactory.getStringGetter(argToValueMap))
                .withSuccessEvent(successEvent)
                .withResponseErrorEvent(errorEvent)
                .withCustomValueGetterExecutor(MoreExecutors.newDirectExecutorService())
                .withCustomStateListenerExecutor(stateListenersExecutor)
                .build();

        when:
        field.postValue()

        then:
        called
    }
}
