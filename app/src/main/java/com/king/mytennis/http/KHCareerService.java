package com.king.mytennis.http;

import com.king.mytennis.http.bean.ImageUrlBean;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/9/1.
 */
public interface KHCareerService {
    @GET("getImages")
    Observable<ImageUrlBean> getImages(@Query("type") String type, @Query("key") String key);
}
