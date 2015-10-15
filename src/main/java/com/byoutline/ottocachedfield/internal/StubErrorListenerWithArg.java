package com.byoutline.ottocachedfield.internal;

/**
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class StubErrorListenerWithArg<ARG_TYPE> implements com.byoutline.cachedfield.ErrorListenerWithArg<ARG_TYPE> {
    @Override
    public void valueLoadingFailed(Exception ex, ARG_TYPE arg) {
    }
}
