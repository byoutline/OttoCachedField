package com.byoutline.ottocachedfield.internal;

import com.byoutline.cachedfield.ErrorListener;
import com.byoutline.eventcallback.IBus;
import javax.annotation.Nonnull;

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class OttoErrorListener implements ErrorListener {

    private final IBus bus;
    private final ErrorEvent event;

    public OttoErrorListener(@Nonnull IBus bus, @Nonnull ErrorEvent event) {
        this.bus = bus;
        this.event = event;
    }

    @Override
    public void valueLoadingFailed(Exception excptn) {
        if (event != null) {
            event.post(bus, excptn);
        }
    }
}
