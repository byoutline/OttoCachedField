package com.byoutline.ottocachedfield

import com.byoutline.cachedfield.FieldState
import com.byoutline.cachedfield.ProviderWithArg

import javax.inject.Provider

static Provider<String> getSameSessionIdProvider() {
    return { return "sessionId" } as Provider<String>
}

static Provider<String> getMultiSessionIdProvider() {
    int i = 1;
    return { return "sessionId" + i++ } as Provider<String>
}

static Provider<String> getDelayedStringGetter(String value) {
    return getDelayedStringGetter(value, 5)
}

static Provider<String> getDelayedStringGetter(String value, long sleepTime) {
    return [get     : { Thread.sleep(sleepTime); return value },
            toString: { sleepTime + "delayedStringGetter: " + value }] as Provider<String>
}

static Provider<String> getStringGetter(String value) {
    return [get     : { return value },
            toString: { "string getter: " + value }] as Provider<String>
}

static ProviderWithArg<String, Integer> getStringGetter(Map<Integer, String> argToValueMap) {
    return [get     : { Integer arg -> return argToValueMap.get(arg) },
            toString: { "string getter with arg: " + argToValueMap }
    ] as ProviderWithArg<String, Integer>
}

static Provider<String> getFailingStringGetter(Exception ex) {
    return [get     : { throw ex },
            toString: { "fail provider with: " + ex }] as Provider<String>
}

static ProviderWithArg<String, Integer> getFailingStringGetterWithArg() {
    return [get     : { Integer arg -> throw new RuntimeException("E" + arg) },
            toString: { "fail provider with arg" }] as ProviderWithArg<String, Integer>
}

static void waitUntilFieldLoads(OttoCachedField field) {
    while (field.getState() != FieldState.LOADED) {
        sleep 1
    }
}
