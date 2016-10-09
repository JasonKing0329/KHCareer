package com.king.mytennis.http;

import retrofit2.Retrofit;

/**
 * Created by Administrator on 2016/9/5.
 */
public class KHCareerHttpClient extends BaseHttpClient {

    private KHCareerService mService;

    private KHCareerHttpClient() {
        super();
    }

    @Override
    protected void createService(Retrofit retrofit) {
        mService = retrofit.create(KHCareerService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final KHCareerHttpClient INSTANCE = new KHCareerHttpClient();
    }

    //获取单例
    public static KHCareerHttpClient getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public KHCareerService getService() {
        return mService;
    }

}
