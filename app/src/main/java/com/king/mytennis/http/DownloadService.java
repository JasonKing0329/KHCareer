package com.king.mytennis.http;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import rx.Observable;

public interface DownloadService {

    @Streaming
    @GET("download")
    Observable<ResponseBody> download(@Query("name") String name, @Query("type") String type);

}
