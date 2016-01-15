package com.byoutline.ottocachedfield

import com.byoutline.ibuscachedfield.internal.NullArgumentException
import spock.lang.Shared
import spock.lang.Unroll

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com> on 27.06.14.
 */
class DefaultObservableBuilderArgsSpec extends spock.lang.Specification {
    @Shared
    Map<Integer, String> argToValueMap = [1: 'a', 2: 'b']

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
        builder                  | name
        MockFactory.obsWithArgBuilder()
                .withValueProvider(MockFactory.getStringGetter(argToValueMap))
                .withoutEvents() | "ObservableCachedFieldWithArg"
    }
}
