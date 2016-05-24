package com.byoutline.ottocachedfield

import com.byoutline.ibuscachedfield.internal.NullArgumentException
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com> on 27.06.14.
 */
class DefaultObservableBuilderArgsSpec extends Specification {
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
        new CachedFieldBuilder()
                .withValueProviderWithArg(MockFactory.getStringGetter(argToValueMap))
                .asObservable()  | "ObservableCachedFieldWithArg"
        new CachedFieldBuilder()
                .withValueProvider(MockFactory.getStringGetter("str"))
                .asObservable()  | "ObservableCachedField(no arg)"
    }
}
