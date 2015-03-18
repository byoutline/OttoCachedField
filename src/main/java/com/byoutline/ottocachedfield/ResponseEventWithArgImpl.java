package com.byoutline.ottocachedfield;

/**
 * Simple implementation of {@link ResponseEventWithArg} that contains only getters and setter.
 *
 * @param <RETURN_TYPE> Type of response value
 * @param <ARG_TYPE>    Type of argument used to receive/calculate response
 */
public class ResponseEventWithArgImpl<RETURN_TYPE, ARG_TYPE> implements ResponseEventWithArg<RETURN_TYPE, ARG_TYPE> {
    private RETURN_TYPE value;
    private ARG_TYPE argValue;

    public RETURN_TYPE getValue() {
        return value;
    }

    public ARG_TYPE getArgValue() {
        return argValue;
    }

    @Override
    public void setResponse(RETURN_TYPE value, ARG_TYPE argValue) {
        this.value = value;
        this.argValue = argValue;
    }
}