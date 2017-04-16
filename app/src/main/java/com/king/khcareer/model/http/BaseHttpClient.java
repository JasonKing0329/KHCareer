package com.king.khcareer.model.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2016/9/1.
 */
public abstract class BaseHttpClient implements BaseUrlSubscriber {

    private String baseUrl;

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    public BaseHttpClient() {
        baseUrl = BaseUrl.getInstance().getBaseUrl();
        BaseUrl.getInstance().addSubscriber(this);
        createRetrofit();
    }

    private void createRetrofit() {
        // 查看通信LOG，可以输入OkHttp来过滤
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 设置连接超时
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        // 设置log打印器
        builder.addInterceptor(loggingInterceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(builder.build())
                .build();

        createService(retrofit);
    }

    protected abstract void createService(Retrofit retrofit);

    @Override
    public void onBaseUrlChanged(String url) {
        baseUrl = url;
        createRetrofit();
    }

}
