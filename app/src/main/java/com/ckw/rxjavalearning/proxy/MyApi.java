package com.ckw.rxjavalearning.proxy;

import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by ckw
 * on 2018/7/25.
 */
public interface MyApi {

    @POST("postUrl")
    String postValue(String id);

    @GET("getUrl")
    String getValue(String id);
}
