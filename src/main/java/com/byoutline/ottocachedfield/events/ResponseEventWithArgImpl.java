package com.byoutline.ottocachedfield.events;

/**
 * Simple implementation of {@link ResponseEventWithArg} that contains only getters and setter.
 *
 * @param <RETURN_TYPE> Type of response response
 * @param <ARG_TYPE>    Type of argument used to receive/calculate response
 */
public class ResponseEventWithArgImpl<RETURN_TYPE, ARG_TYPE> implements ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> {
    private RETURN_TYPE response;
    private ARG_TYPE argValue;

    public RETURN_TYPE getResponse() {
        return response;
    }

    public ARG_TYPE getArgValue() {
        return argValue;
    }

    @Override
    public void setResponse(RETURN_TYPE value, ARG_TYPE argValue) {
        this.response = value;
        this.argValue = argValue;
    }
}