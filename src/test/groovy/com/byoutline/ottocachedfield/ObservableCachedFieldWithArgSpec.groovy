package com.byoutline.ottocachedfield

import android.databinding.Observable
import com.byoutline.shadow.ObservableField
import com.google.common.util.concurrent.MoreExecutors
import com.squareup.otto.Bus
import spock.lang.Shared
import spock.lang.Unroll

/**
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com>
 */
public class ObservableCachedFieldWithArgSpec extends spock.lang.Specification {
    @Shared
    Map<Integer, String> argToValueMap = [1: 'a', 2: 'b']

    def setup() {
        Bus bus = Mock()
        OttoCachedField.init(MockFactory.getSameSessionIdProvider(), bus)
    }

    @Unroll
    def "should notify about new value: #val for arg: #arg"() {
        given:
        ObservableCachedFieldWithArg field = ObservableCachedFieldWithArg.builder()
                .withValueProvider(MockFactory.getStringGetter(argToValueMap))
                .withoutEvents()
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
