package com.byoutline.ottocachedfield;

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 * @param <RETURN_TYPE> Type of returned value
 * @param <ARG_TYPE> Type of argument required to get response
 */
public interface ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> {

    /**
     * @param value response from server/or calculated value.
     * @param argValue argument that was used to get value.
     */
    void setResponse(RETURN_TYPE value, ARG_TYPE argValue);
}
