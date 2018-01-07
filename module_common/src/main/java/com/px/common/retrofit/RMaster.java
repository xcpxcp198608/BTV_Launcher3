package com.px.common.retrofit;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by patrick on 13/10/2017.
 * create time : 11:09 AM
 */

public class RMaster {

    private static Retrofit retrofit;
    private static final String BASE_URL = "http://localhost:8080/";

    static{
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

}
