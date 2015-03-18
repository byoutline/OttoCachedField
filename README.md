OttoCachedField
===============

Wrapper for expensive values (like API calls) that post results by Otto bus. Additionally it guards against displaying data from one user to another.

How to use
----------
##### Including dependency #####
Add to your ```build.gradle```:
```groovy
compile 'com.byoutline.ottocachedfield:ottocachedfield:1.3.2'
```

##### Init commmon settings #####
To avoid passing same values to each of your CachedFields put following into your code (typically to ```Application``` ```onCreate``` method).
```java
OttoCachedField.init(sessionIdProvider, bus);
```
where bus is ```Otto``` bus instance, and sessionIdProvider is a ```Provider``` of current session. ```Provider``` is supposed to return same string as long as same user is logged in. Typically it something like authorization header for your API calls.

##### Declare your fields #####
To declare your field you should pass it a ```Provider```, that synchronously calculates/fetches your value. You also have to pass ```ResponseEvent``` that will be posted when value its ready, and optionally Event that will be posted in case of failure. 
```java
public final CachedField<YourExpensiveValue> expensiveValue = new OttoCachedField<>(new Provider<YourExpensiveValue>() {
        @Override
        public YourExpensiveValue get() {
            return service.getValueFromApi();
        }
    }, new ValueFetchedEvent(), new ValueFetchFailedEvent());
```
```java
public class ValueFetchedEvent extends ResponseEventImpl<YourExpensiveValue> {
}
```
If you skipped init common settings step or want to override default project value for this specific field you may also pass sessionIdProvider and Otto bus instance.

Note: It is advised to put your cached field in some sort of a manager or other object that is not connected to Android view lifecycle. It will allow you to keep your cached values between screen rotation, etc.

##### Get value when it ready #####
```java
    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
        manager.expensiveValue.postValue();
    }

    @Override
    public void onPause() {
        bus.unregister(this);
        super.onPause();
    }
    
    @Subscribe
    public void onValueFetched(ValueFetchedEvent event) {
        YourExpensiveValue value = event.getResponse();
        // do something with your value
    }
    @Subscribe
    public void onValueFetchFailedFailed(ValueFetchFailedEvent event) {
        // do something about failure
    }
```

#### Interface description ####
See [Cached field](https://github.com/byoutline/CachedField#interface-description)

Example Project
---------------
If you want to see complete project that uses OttoCachedField take a look at [Android Live Code Warsaw Flickr project on Github](https://github.com/byoutline/AndroidLiveCodeWarsawFlickr/).

Not an Otto bus user?
---------------------
If you do not want to use Otto bus check [CachedField](https://github.com/byoutline/CachedField) project.
