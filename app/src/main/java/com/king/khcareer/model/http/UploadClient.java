package com.king.khcareer.model.http;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 文件下载
 */
public class UploadClient {

    private String baseUrl;

    private UploadService uploadService;

    // UploadClient每次都是new，直接获取最新的baseUrl即可
    public UploadClient() {
        baseUrl = BaseUrl.getInstance().getBaseUrl();

        OkHttpClient client = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        uploadService = retrofit.create(UploadService.class);
    }

    public UploadService getUploadService() {
        return uploadService;
    }
}
