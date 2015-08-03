package com.byoutline.ottocachedfield

import com.byoutline.cachedfield.dbcache.DbCacheArg
import com.byoutline.cachedfield.dbcache.DbWriter
import com.byoutline.cachedfield.dbcache.DbWriterWithArg
import com.byoutline.cachedfield.dbcache.FetchType
import com.byoutline.ibuscachedfield.events.ResponseEventWithArgImpl
import com.squareup.otto.Bus
import spock.lang.Shared

import javax.inject.Provider

/**
 *
 * @author Sebastian Kacprzak <sebastian.kacprzak at byoutline.com> on 27.06.14.
 */
class DbCacheSpec extends spock.lang.Specification {
    @Shared
    String value = "value"
    @Shared
    Map<Integer, String> argToValueMap = [1: 'a', 2: 'b']
    @Shared
    String differentValue = "different value"
    ResponseEventWithArgImpl<String, FetchType> successEvent
    ResponseEventWithArgImpl<Exception, FetchType> errorEvent
    ResponseEventWithArgImpl<String, DbCacheArg<Integer>> successEventWithArg
    ResponseEventWithArgImpl<Exception, DbCacheArg<Integer>> errorEventWithArg
    Bus bus

    def setup() {
        bus = Mock()
        successEvent = new ResponseEventWithArgImpl<>()
        errorEvent = new ResponseEventWithArgImpl<>()
        successEventWithArg = new ResponseEventWithArgImpl<>()
        errorEventWithArg = new ResponseEventWithArgImpl<>()

        OttoCachedField.init(MockFactory.getSameSessionIdProvider(), bus)
    }


    def "should post value from API"() {
        given:
        def dbSaver = {} as DbWriter
        OttoCachedFieldWithArg<String, FetchType> field = OttoCachedField.<String> builder()
                .withApiFetcher(MockFactory.getStringGetter(value))
                .withDbWriter(dbSaver)
                .withDbReader(MockFactory.getStringGetter(value))
                .withSuccessEvent(successEvent)
                .withResponseErrorEvent(errorEvent)
                .build();

        when:
        OttoCachedFieldWithArgSpec.postAndWaitUntilFieldStopsLoading(field, FetchType.API)

        then:
        value == successEvent.getResponse()
        FetchType.API == successEvent.getArgValue()
    }

    def "should post value from API with arg"() {
        given:
        def dbSaverArg = null
        DbWriterWithArg<String, Integer> dbSaver = new DbWriterWithArg<String, Integer>() {
            @Override
            void saveToDb(String value, Integer arg) {
                dbSaverArg = value
            }
        };
        OttoCachedFieldWithArg<String, DbCacheArg<Integer>> field = OttoCachedFieldWithArg.<String> builder()
                .withApiFetcher(MockFactory.getStringGetter(argToValueMap))
                .withDbWriter(dbSaver)
                .withDbReader(MockFactory.getStringGetter(argToValueMap))
                .withSuccessEvent(successEventWithArg)
                .withResponseErrorEvent(errorEventWithArg)
                .build();

        when:
        OttoCachedFieldWithArgSpec.postAndWaitUntilFieldStopsLoading(field, DbCacheArg.create(1, FetchType.API))

        then:
        'a' == successEventWithArg.getResponse()
        DbCacheArg.create(1, FetchType.API) == successEventWithArg.getArgValue()
    }

    def "should post value from DB"() {
        given:
        def dbSaver = {} as DbWriter
        OttoCachedFieldWithArg<String, FetchType> field = OttoCachedField.<String> builder()
                .withApiFetcher(MockFactory.getStringGetter(value))
                .withDbWriter(dbSaver)
                .withDbReader(MockFactory.getStringGetter(differentValue))
                .withSuccessEvent(successEvent)
                .withResponseErrorEvent(errorEvent)
                .build();

        when:
        OttoCachedFieldWithArgSpec.postAndWaitUntilFieldStopsLoading(field, FetchType.DB)

        then:
        differentValue == successEvent.getResponse()
        FetchType.DB == successEvent.getArgValue()
    }

    def "should allow different return type for DB and API"() {
        given:
        def dbSaver = {} as DbWriter
        def event = new ResponseEventWithArgImpl<Integer, FetchType>()
        OttoCachedFieldWithArg<Integer, FetchType> field = OttoCachedField.<String> builder()
                .withApiFetcher(MockFactory.getStringGetter(value))
                .withDbWriter(dbSaver)
                .withDbReader({ return 1 } as Provider<Integer>)
                .withSuccessEvent(event)
                .build();

        when:
        OttoCachedFieldWithArgSpec.postAndWaitUntilFieldStopsLoading(field, FetchType.API)

        then:
        event.getResponse() == 1
    }
}
