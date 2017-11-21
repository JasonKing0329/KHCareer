package com.king.khcareer.model.http;

import com.king.khcareer.model.http.bean.ImageUrlBean;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/9/1.
 */
public interface KHCareerService {
    @GET("getImages")
    Observable<ImageUrlBean> getImages(@Query("type") String type, @Query("key") String key);
}
