package com.byoutline.ottocachedfield

import android.databinding.Observable
import android.databinding.ObservableField
import com.byoutline.observablecachedfield.ObservableCachedFieldWithArg
import com.google.common.util.concurrent.MoreExecutors
import com.squareup.otto.Bus
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class ObservableCachedFieldWithArgSpec extends Specification {
    @Shared
    Map<Integer, String> argToValueMap = [1: 'a', 2: 'b']

    def setup() {
        Bus bus = Mock()
        OttoCachedField.init(MockFactory.getSameSessionIdProvider(), bus)
    }

    @Unroll
    def "should notify about new value: #val for arg: #arg"() {
        given:
        ObservableCachedFieldWithArg field = new CachedFieldBuilder()
                .withValueProviderWithArg(MockFactory.getStringGetter(argToValueMap))
                .asObservable()
                .withCustomValueGetterExecutor(MoreExecutors.newDirectExecutorService())
                .build();
        boolean called = false
        def callback = new Observable.OnPropertyChangedCallback() {

            @Override
            void onPropertyChanged(Observable sender, int propertyId) {
                called = true
            }
        }
        ObservableField<String> obs = field.observable()
        obs.addOnPropertyChangedCallback(callback)

        when:
        field.postValue(arg)

        then:
        called
        obs.get() == val

        where:
        val  | arg
        'a'  | 1
        'b'  | 2
    }
}
