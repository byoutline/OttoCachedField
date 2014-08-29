OttoCachedField
===============

Wrapper for expensives values (like API calls) that post results by Otto bus. Additionaly fields use @Produce annotation to post events automatically to your subscribers, so you only need to @Subscribe to get latest value.
