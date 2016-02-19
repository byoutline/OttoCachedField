package com.byoutline.ottocachedfield.utils;

import android.os.Handler;
import android.os.Looper;

import javax.annotation.Nonnull;
import java.util.concurrent.Executor;

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