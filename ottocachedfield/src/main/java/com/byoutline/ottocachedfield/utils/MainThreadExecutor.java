package com.byoutline.ottocachedfield.utils;

import android.os.Handler;
import android.os.Looper;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;

/**
 * Executor that invokes commands on {@link Looper#getMainLooper()}.<br/>
 * It can be useful if for example you update UI from {@link com.byoutline.cachedfield.FieldStateListener}.<br/>
 */
public class MainThreadExecutor implements Executor {

    @Override
    public void execute(@Nonnull Runnable command) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            // We're not in the main loop, so we need to get into it.
            (new Handler(Looper.getMainLooper())).post(command);
        } else {
            command.run();
        }
    }
}