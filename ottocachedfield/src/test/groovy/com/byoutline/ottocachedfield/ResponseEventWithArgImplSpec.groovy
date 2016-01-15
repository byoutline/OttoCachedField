package com.byoutline.ottocachedfield

import com.byoutline.ottocachedfield.events.ResponseEventWithArgImpl
import spock.lang.Unroll

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com> on 27.06.14.
 */
class ResponseEventWithArgImplSpec extends spock.lang.Specification {

    @Unroll
    def "should store passed response: #resp and argValue: #argVal"() {
        given:
        ResponseEventWithArgImpl instance = new ResponseEventWithArgImpl()

        when:
        instance.setResponse(resp, argVal)

        then:

        resp == instance.getResponse()
        argVal == instance.getArgValue()

        where:
        resp | argVal
        null | 0
        2    | '1'
        'b'  | null
    }

}
