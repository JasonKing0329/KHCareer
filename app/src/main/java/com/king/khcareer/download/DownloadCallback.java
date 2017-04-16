package com.king.khcareer.download;

/**
 * Created by Administrator on 2016/9/2.
 */
public interface DownloadCallback {
    void onDownloadFinish(DownloadItem item);
    void onDownloadError(DownloadItem item);
    void onDownloadAllFinish();
}
