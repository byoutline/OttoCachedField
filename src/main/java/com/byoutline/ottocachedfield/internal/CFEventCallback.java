package com.byoutline.ottocachedfield.internal;

import com.byoutline.eventcallback.CallbackConfig;
import com.byoutline.eventcallback.EventCallbackBuilder;
import com.google.gson.reflect.TypeToken;
import retrofit.RetrofitError;

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class CFEventCallback<S> extends EventCallbackBuilder<S, RetrofitError> {

    private CFEventCallback(CallbackConfig config) {
        super(config, new TypeToken<RetrofitError>() {
        });
    }

    public static <S> CFEventCallback<S> builder(CallbackConfig config) {
        return new CFEventCallback<S>(config);
    }
}
