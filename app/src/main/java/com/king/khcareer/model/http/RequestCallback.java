package com.king.khcareer.model.http;

import com.king.khcareer.model.http.bean.ImageUrlBean;

/**
 * Created by Administrator on 2016/10/9.
 */
public interface RequestCallback {
    void onServiceDisConnected();
    void onRequestError();
    void onImagesReceived(ImageUrlBean bean);
    void onDownloadFinished();
}
