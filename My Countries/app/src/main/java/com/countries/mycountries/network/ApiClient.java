package com.countries.mycountries.network;

import android.support.annotation.Nullable;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dell on 7/16/2017.
 */

public class ApiClient {

    private static final String BASE_URL = "http://www.androidbegin.com/";
    @Nullable
    private static Retrofit retrofit = null;

    @Nullable
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
        }
        return retrofit;
    }

}
