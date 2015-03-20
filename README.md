OttoCachedField
===============
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.byoutline.ottocachedfield/ottocachedfield/badge.svg?style=flat)](http://mvnrepository.com/artifact/com.byoutline.ottocachedfield/ottocachedfield)  

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
To declare your field you should pass it a ```Provider```, that synchronously calculates/fetches your value. You also have to pass ```ResponseEvent``` that will be posted when value is ready, and optionally Event that will be posted in case of failure. 
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

##### Get value when it's ready #####
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

Calling ```postValue``` or ```refresh``` will always cause CachedField to post either Success Event or Error Event.

### Interface description ###

See [Cached field](https://github.com/byoutline/CachedField#interface-description) to read more about basic methods like ```refresh```, ```drop``` etc.


### Parametric fields ###

In case your value depends on some argument  (for example API GET call that requires item ID) you can use [OttoCachedFieldWithArg](https://github.com/byoutline/OttoCachedField/blob/master/src/main/java/com/byoutline/ottocachedfield/OttoCachedFieldWithArg.java) . It supports same methods but requires you to pass argument to ```post``` and ```refresh``` calls. Only one value will be cached at the time, so changing argument will force a refresh .

If you ask ```OttoCachedFieldWithArg``` for value with new argument before last call had chance to finish, Success Event will be posted only about with value for current argument. Previous call will be assumed obsolete, and its return value(if any) will be discarded and Error Event will be posted instead.

If you want to check which call to field was completed check ```argValue``` parameter passed to your [ResponseEventWithArg](https://github.com/byoutline/OttoCachedField/blob/master/src/main/java/com/byoutline/ottocachedfield/events/ResponseEventWithArg.java)


Prametric field classes have ```withArg``` suffix, and behave same as their no arg counterparts. Split exist only to enforce passing extra argument to methods that depend on it.

without arguments                              | with arguments
-----------------------------------------------|-----------------------------------------------
[OttoCachedField](https://github.com/byoutline/OttoCachedField/blob/master/src/main/java/com/byoutline/ottocachedfield/OttoCachedField.java)  | [OttoCachedFieldWithArg](https://github.com/byoutline/OttoCachedField/blob/master/src/main/java/com/byoutline/ottocachedfield/OttoCachedFieldWithArg.java)
[OttoCachedFieldBuilder](https://github.com/byoutline/OttoCachedField/blob/master/src/main/java/com/byoutline/ottocachedfield/OttoCachedFieldBuilder.java)  | [OttoCachedFieldWithArgBuilder](https://github.com/byoutline/OttoCachedField/blob/master/src/main/java/com/byoutline/ottocachedfield/OttoCachedFieldWithArgBuilder.java)
[ResponseEvent](https://github.com/byoutline/EventCallback/blob/master/src/main/java/com/byoutline/eventcallback/ResponseEvent.java) | [ResponseEventWithArg](https://github.com/byoutline/OttoCachedField/blob/master/src/main/java/com/byoutline/ottocachedfield/events/ResponseEventWithArg.java)
[ResponseEventImpl](https://github.com/byoutline/EventCallback/blob/master/src/main/java/com/byoutline/eventcallback/ResponseEventImpl.java) | [ResponseEventWithArgImpl](https://github.com/byoutline/OttoCachedField/blob/master/src/main/java/com/byoutline/ottocachedfield/events/ResponseEventWithArgImpl.java)
[Provider](https://docs.oracle.com/javaee/7/api/javax/inject/Provider.html) | [ProviderWithArg](https://github.com/byoutline/CachedField/blob/master/src/main/java/com/byoutline/cachedfield/ProviderWithArg.java)


### Builder syntax for OttoCachedField instance creation ###
You may choose use ```builder``` instead of constructor to create yout fields:
```java
new OttoCachedFieldBuilder<>()
    .withValueProvider(new Provider<YourExpensiveValue>() {
        @Override
        public YourExpensiveValue get() {
            return service.getValueFromApi();
        }
    }).withSuccessEvent(new ValueFetchedEvent())
    .withResponseErrorEvent(new ValueFetchFailedEvent())
    .build();
```
Builder syntax is slightly longer, but makes it obvious which argument does what, and allows for better IDE autocompletion.

Example Project
---------------
If you want to see complete project that uses OttoCachedField take a look at [Android Live Code Warsaw Flickr project on Github](https://github.com/byoutline/AndroidLiveCodeWarsawFlickr/).

Not an Otto bus user?
---------------------
If you do not want to use Otto bus check [CachedField](https://github.com/byoutline/CachedField) project.
