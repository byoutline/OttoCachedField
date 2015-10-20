package com.byoutline.ottocachedfield

import com.byoutline.eventcallback.ResponseEvent
import com.byoutline.eventcallback.ResponseEventImpl
import com.byoutline.ibuscachedfield.events.ResponseEventWithArg
import com.byoutline.ibuscachedfield.events.ResponseEventWithArgImpl
import com.byoutline.ottocachedfield.internal.NullArgumentException
import spock.lang.Shared
import spock.lang.Unroll

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com> on 27.06.14.
 */
class DefaultBuilderArgsSpec extends spock.lang.Specification {
    @Shared
    String value = "value"
    @Shared
    Map<Integer, String> argToValueMap = [1: 'a', 2: 'b']
    @Shared
    ResponseEvent responseEvent = new ResponseEventImpl()
    @Shared
    ResponseEventWithArg responseEventWithArg = new ResponseEventWithArgImpl()

    def setupSpec() {
        OttoCachedField.init(null, null, null, null)
    }

    @Unroll
    def "#name builder should throw exception if stateListenerExecutor is null"() {
        when:
        builder.build()

        then:
        thrown(NullArgumentException)

        where:
        builder                                         | name
        OttoCachedField.builder()
                .withValueProvider(MockFactory.getStringGetter(value))
                .withSuccessEvent(responseEvent)        | "OttoCachedField"

        OttoCachedFieldWithArg.builder()
                .withValueProvider(MockFactory.getStringGetter(argToValueMap))
                .withSuccessEvent(responseEventWithArg) | "OttoCachedFieldWithArg"

        OttoCachedEndpointWithArg.builder()
                .withValueProvider(MockFactory.getStringGetter(argToValueMap))
                .withResultEvent(responseEventWithArg)  | "OttoCachedEndpointWithArg"

        ObservableCachedFieldWithArg.builder()
                .withValueProvider(MockFactory.getStringGetter(argToValueMap))
                .withoutEvents()                        | "ObservableCachedFieldWithArg"
    }
}
