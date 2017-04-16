package com.king.khcareer.model.http;

import com.king.khcareer.update.AppCheckBean;
import com.king.khcareer.update.GdbRespBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface AppService {
    @GET("online")
    Observable<GdbRespBean> isServerOnline();

    @GET("checkNew")
    Observable<AppCheckBean> checkAppUpdate(@Query("type") String type, @Query("version") String version);

    @GET("checkNew")
    Observable<AppCheckBean> checkGdbDatabaseUpdate(@Query("type") String type, @Query("version") String version);
}
