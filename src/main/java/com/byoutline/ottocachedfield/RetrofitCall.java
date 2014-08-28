package com.byoutline.ottocachedfield;

import retrofit.Callback;

/**
 * Fetches value with retrofit.
 * 
* @author Sebastian Kacprzak <nait at naitbit.com>
 */
public interface RetrofitCall<T> {

    void get(Callback<T> cb);
}
