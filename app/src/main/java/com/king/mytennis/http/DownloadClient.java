package com.king.mytennis.http;

import com.king.mytennis.http.progress.ProgressInterceptor;
import com.king.mytennis.http.progress.ProgressListener;

import okhttp3.OkHttpClient;
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
        // 由于下载大文件容易引起OOM，所以在@Streaming的基础上得把log去掉，但是不加logging这个interceptor的话
        // 如果onNext调用了接受inputStream并保存到磁盘的方法，那么一直会进入onError，异常是networkOnMainThread
        // 原因是因为与服务端保持着一边接收一边保存的操作，而保存的操作在onNext里，属于主线程了
        // 所以在用retrofit进行observeOn的时候不能再指定mainThread了，可以指定为Schedual.io()
        // 另外，subscriber里的3个回调方法也是运行在io线程了，再通知ui变化就需要用Handler来通知了
        // 这一部分在DownloadManager进行了修改
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
//            @Override
//            public void log(String message) {
//                try {
////                    new JsonParser().parse(message);
////                    Logger.json(message);
//                } catch (JsonSyntaxException e) {
//                    // 不打印实际下载内容
//                }
//            }
//        });
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client;
        if (progressListener != null) {
            ProgressInterceptor progress = new ProgressInterceptor(progressListener);
            client = new OkHttpClient.Builder()
                    .addInterceptor(progress)
                    .build();
        } else {
            client = new OkHttpClient.Builder()
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
