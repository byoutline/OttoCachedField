package com.byoutline.ottocachedfield.internal;

import com.byoutline.cachedfield.SuccessListenerWithArg;

/**
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class StubSuccessListenerWithArg<RETURN_TYPE, ARG_TYPE> implements SuccessListenerWithArg<RETURN_TYPE, ARG_TYPE> {
    @Override
    public void valueLoaded(RETURN_TYPE value, ARG_TYPE arg) {
    }
}
