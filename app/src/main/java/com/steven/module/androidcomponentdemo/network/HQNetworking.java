package com.steven.module.androidcomponentdemo.network;

import com.google.gson.GsonBuilder;
import com.steven.module.androidcomponentdemo.network.File.FileRequest;
import com.steven.module.androidcomponentdemo.network.File.ProgressInfo;
import com.steven.module.androidcomponentdemo.network.File.ProgressListener;
import com.steven.module.androidcomponentdemo.network.File.ProgressManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HQNetworking {

    private static int TIME_OUT = 30; // 30秒超时断开连接

    // httpclient
    public static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .build();

    /**
     * 网络框架单例
     */
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("")
            .client(client)
            .build();

    /**
     * 发送GET请求
     * @param url 请求地址
     * @param paramMap 参数
     * @param resultCallback 回调
     */
    public static <T extends BaseResult> void get(String url, Map<String, Object> paramMap, final ResultCallback<T> resultCallback) {
        get(url, paramMap, null, resultCallback);
    }

    /**
     * 发送GET请求
     * @param url 请求地址
     * @param paramMap 参数
     * @param headerMap 请求头
     * @param resultCallback 回调
     */
    public static <T extends BaseResult> void get(String url, Map<String, Object> paramMap, Map<String, Object> headerMap, final ResultCallback<T> resultCallback) {
        GetRequest getRequest = retrofit.create(GetRequest.class);

        Call<ResponseBody> call = getRequest.get(url, paramMap, headerMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Class<T> entityClass = (Class<T>)((ParameterizedType) resultCallback.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

                    T t = new GsonBuilder().create().fromJson(response.body().string(), entityClass);
                    resultCallback.onResult(t);
                } catch (IOException e) {
                    e.printStackTrace();
                    resultCallback.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                resultCallback.onFailure(t);
            }
        });
    }

    /**
     * 发送POST请求
     * @param url 请求地址
     * @param paramMap 参数
     * @param resultCallback 回调
     */
    public static <T extends BaseResult> void post(String url, Map<String, Object> paramMap, final ResultCallback<T> resultCallback) {
        post(url, paramMap, null, resultCallback);
    }

    /**
     * 发送POST请求
     * @param url 请求地址
     * @param paramMap 参数
     * @param headerMap 请求头
     * @param resultCallback 回调
     */
    public static <T extends BaseResult> void post(String url, Map<String, Object> paramMap, Map<String, Object> headerMap, final ResultCallback<T> resultCallback) {
        PostRequest postRequest = retrofit.create(PostRequest.class);

        Call<ResponseBody> call = postRequest.post(url, paramMap, headerMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Class<T> entityClass = (Class<T>)((ParameterizedType) resultCallback.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

                    T t = new GsonBuilder().create().fromJson(response.body().string(), entityClass);
                    resultCallback.onResult(t);
                } catch (IOException e) {
                    e.printStackTrace();
                    resultCallback.onFailure(e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                resultCallback.onFailure(t);
            }
        });
    }

    /**
     * 上传文件
     * @param url 请求地址
     * @param fileCallback 回调
     */
    public static <T extends BaseResult> void upload(String url, File file, final FileCallback<T> fileCallback) {
        OkHttpClient.Builder clientBuilder = ProgressManager.getInstance().with(new OkHttpClient.Builder());

        // 进度跟踪
        ProgressManager.getInstance().addResponseListener(url, getProgressListener(fileCallback));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileRequest fileRequest = retrofit.create(FileRequest.class);

        Map<String, RequestBody> paramMap = new HashMap<>();
        addMultiPart(paramMap, "file", file);

        // 构建请求
        Call<ResponseBody> call = fileRequest.upload(url, paramMap);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                fileCallback.onSuccess(response.body().byteStream());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                fileCallback.onFailure(t);
            }
        });
    }

    /**
     * 下载文件
     * @param url 请求地址
     * @param fileCallback 回调
     */
    public static <T extends BaseResult> void download(String url, final FileCallback<T> fileCallback) {
        // 回调方法执行器，定义回调在子线程中执行，避免Callback返回到MainThread，导致文件下载出现NetworkOnMainThreadException
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        OkHttpClient.Builder clientBuilder = ProgressManager.getInstance().with(new OkHttpClient.Builder());

        // 进度跟踪
        ProgressManager.getInstance().addResponseListener(url, getProgressListener(fileCallback));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(clientBuilder.build())
                .callbackExecutor(executorService)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileRequest fileRequest = retrofit.create(FileRequest.class);
        // 构建请求
        Call<ResponseBody> call = fileRequest.download(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    fileCallback.onSuccess(response.body().byteStream());
                } else {
                    fileCallback.onFailure(new Throwable(response.message()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                fileCallback.onFailure(t);
            }
        });
    }

    /**
     * 添加多媒体类型
     *
     * @param paramMap 参数对
     * @param key      键
     * @param obj      值
     */
    private static void addMultiPart(Map<String, RequestBody> paramMap, String key, Object obj) {
        if (obj instanceof String) {
            RequestBody body = RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), (String) obj);
            paramMap.put(key, body);
        } else if (obj instanceof File) {
            RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), (File) obj);
            paramMap.put(key + "\"; filename=\"" + ((File) obj).getName() + "", body);
        }
    }

    private static <T extends BaseResult> ProgressListener getProgressListener(final FileCallback<T> fileCallback) {
        return new ProgressListener() {
            @Override
            public void onProgress(ProgressInfo progressInfo) {
                fileCallback.onProgress(progressInfo.getCurrentbytes(), progressInfo.getContentLength());
            }

            @Override
            public void onError(long id, Exception e) {
                fileCallback.onFailure(e);
            }
        };
    }

    public static abstract class ResultCallback<T> {

        /**
         * 请求结果
         *
         * @param t 结果数据
         */
        public abstract void onResult(T t);

        /**
         * 失败回调
         *
         * @param t 异常
        */
        public abstract void onFailure(Throwable t);
    }

    public static abstract class FileCallback<T> {

        /**
         * 接收数据体
         * @param inputStream 接收流
         */
        public abstract void onSuccess(InputStream inputStream);

        /**
         * 上传下载失败
         */
        public abstract void onFailure(Throwable t);

        /**
         * 进度回调
         *
         * @param currentBytes 已完成字节数
         * @param totalBytes 总字节数
         */
        public abstract void onProgress(long currentBytes, long totalBytes);
    }

}
