package com.king.mytennis.http;

import com.king.mytennis.http.bean.ImageUrlBean;

/**
 * Created by Administrator on 2016/10/9.
 */
public interface RequestCallback {
    void onServiceDisConnected();
    void onRequestError();
    void onImagesReceived(ImageUrlBean bean);
    void onDownloadFinished();
}
