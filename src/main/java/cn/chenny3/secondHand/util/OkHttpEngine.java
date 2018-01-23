package cn.chenny3.secondHand.util;


import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.*;
import org.springframework.stereotype.Component;

/**
 * Created by chenny on 2017/11/14.
 */
@Component
public class OkHttpEngine {
    private static volatile OkHttpEngine okHttpEngine;
    private OkHttpClient okHttpClient;


    private OkHttpEngine() {
        //init okHttpClient
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)

                .build();


    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    //单例模式
    public static OkHttpEngine getInstance() {
        if (okHttpEngine == null) {
            synchronized (OkHttpEngine.class) {
                if (okHttpEngine == null) {
                    okHttpEngine = new OkHttpEngine();
                }
            }
        }
        return okHttpEngine;
    }

    public Response syncGetOnlyNet(String url, Map<String, String> headers, Map<String, String> params) throws IOException {
        Request.Builder builder = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(url);
        //设置header
        for (String key : headers.keySet()) {
            builder.addHeader(key, headers.get(key));
        }
        Request request = builder.get().build();

        Call call = okHttpClient.newCall(request);
        Response response = null;
        response = call.execute();
        return response;
    }

    public Response syncPost(String url, Map<String, String> params) {
        //设置提交参数
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (String key : params.keySet()) {
            formBodyBuilder.add(key, params.get(key));
        }

        Request request = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(url).post(formBodyBuilder.build()).build();

        Call call = okHttpClient.newCall(request);
        Response response = null;
        try {
            response = call.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public Response syncPost(String url, Map<String, String> headers, Map<String, String> params) {
        Request.Builder builder = new Request.Builder().cacheControl(CacheControl.FORCE_NETWORK).url(url);
        //设置header
        for (String key : headers.keySet()) {
            builder.addHeader(key, headers.get(key));
        }

        //设置提交参数
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (String key : params.keySet()) {
            formBodyBuilder.add(key, params.get(key));
        }
        //构造请求
        Request request = builder.post(formBodyBuilder.build()).build();

        Call call = okHttpClient.newCall(request);
        Response response = null;
        try {
            response = call.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

}
