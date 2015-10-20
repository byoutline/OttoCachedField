package com.byoutline.ottocachedfield.internal;

/**
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class NullArgumentException extends IllegalArgumentException {
    private final static String NULL_EXECUTOR = "Null executors are not allowed. Please set defaults by calling OttoCachedField.init or pass non null executors directly.";

    public NullArgumentException() {
        super(NULL_EXECUTOR);
    }
}
