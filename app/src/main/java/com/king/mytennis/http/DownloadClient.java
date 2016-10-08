package com.king.mytennis.http;

import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.king.mytennis.http.progress.ProgressInterceptor;
import com.king.mytennis.http.progress.ProgressListener;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * 文件下载
 */
public class DownloadClient {

    private String baseUrl;

    private DownloadService mDownloadService;

    // DownloadClient每次都是new，直接获取最新的baseUrl即可
    public DownloadClient(ProgressListener progressListener) {
        baseUrl = BaseUrl.getInstance().getBaseUrl();
        // 真是太奇怪了，遇到的问题
        // 由于下载大文件容易引起OOM，所以在@Streaming的基础上得把log去掉，但是不加logging这个interceptor的话
        // 如果onNext调用了接受inputStream并保存到磁盘的方法，那么一直会进入onError，异常是networkOnMainThread
        // 猜想原因是因为与服务端保持着一边接收一边保存的操作，而保存的操作在onNext里，属于主线程了
        // 然后就注释掉保存的操作，这样一来就没有下载过程了。
        // 然后从别的项目中copy成下面这样，竟然就可以了！
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                try {
                    new JsonParser().parse(message);
//                    Logger.json(message);
                } catch (JsonSyntaxException e) {
                    // 不打印实际下载内容
                }
            }
        });
        OkHttpClient client;
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (progressListener != null) {
            ProgressInterceptor progress = new ProgressInterceptor(progressListener);
            client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(progress)
                    .build();
        } else {
            client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();
        mDownloadService = retrofit.create(DownloadService.class);
    }

    public DownloadService getDownloadService() {
        return mDownloadService;
    }
}
