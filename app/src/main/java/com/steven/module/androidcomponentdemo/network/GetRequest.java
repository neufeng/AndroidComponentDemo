package com.steven.module.androidcomponentdemo.network;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * The interface Get request.
 */
public interface GetRequest {


    /**
     * Get请求
     *
     * @param url       url
     * @param queryMap  参数
     * @param headerMap 请求头
     * @return
     */
    @GET
    Call<ResponseBody> get(@Url String url, @QueryMap Map<String, Object> queryMap, @HeaderMap Map<String, Object> headerMap);
}
