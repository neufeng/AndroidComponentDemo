package com.steven.module.androidcomponentdemo.network;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * The interface Post request.
 */
public interface PostRequest {

    /**
     * Post请求
     *
     * @param url       url
     * @param paramMap  参数
     * @param headerMap 请求头
     * @return
     */
    @FormUrlEncoded
    @POST
    Call<ResponseBody> post(@Url String url, @FieldMap Map<String, Object> paramMap, @HeaderMap Map<String, Object> headerMap);
}
