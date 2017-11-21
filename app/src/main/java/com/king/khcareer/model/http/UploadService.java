package com.king.khcareer.model.http;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface UploadService {

    @Multipart
    @POST("upload")
    Observable<ResponseBody> upload(@PartMap Map<String, RequestBody> params);

}
