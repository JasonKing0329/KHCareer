package com.king.mytennis.view_v_7_0.interaction;

import android.content.Context;

import com.king.mytennis.download.DownloadDialog;
import com.king.mytennis.download.DownloadItem;
import com.king.mytennis.http.AppHttpClient;
import com.king.mytennis.http.Command;
import com.king.mytennis.http.KHCareerHttpClient;
import com.king.mytennis.http.RequestCallback;
import com.king.mytennis.http.bean.ImageUrlBean;
import com.king.mytennis.update.GdbRespBean;
import com.king.mytennis.view.CustomDialog;

import java.util.HashMap;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/10/9.
 */
public class InteractionController {

    private RequestCallback mCallback;
    public InteractionController(RequestCallback callback) {
        mCallback = callback;
    }

    public void getImages(final String key) {
        AppHttpClient.getInstance().getAppService().isServerOnline()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GdbRespBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mCallback.onServiceDisConnected();
                    }

                    @Override
                    public void onNext(GdbRespBean gdbRespBean) {
                        if (gdbRespBean.isOnline()) {
                            requestGetImages(key);
                        }
                    }
                });

    }

    private void requestGetImages(String key) {
        KHCareerHttpClient.getInstance().getService().getImages(Command.TYPE_IMG_PLAYER, key)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ImageUrlBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mCallback.onRequestError();
                    }

                    @Override
                    public void onNext(ImageUrlBean bean) {
                        mCallback.onImagesReceived(bean);
                    }
                });
    }

    public void downloadImage(Context context, final List<DownloadItem> itemList, final String savePath, final boolean noOption) {
        final DownloadDialog dialog = new DownloadDialog(context, new CustomDialog.OnCustomDialogActionListener() {
            @Override
            public boolean onSave(Object object) {
                return false;
            }

            @Override
            public boolean onCancel() {
                return false;
            }

            @Override
            public void onLoadData(HashMap<String, Object> data) {
                data.put("items", itemList);
                data.put("savePath", savePath);
                data.put("noOption", noOption);
            }
        });
        dialog.setOnDownloadListener(new DownloadDialog.OnDownloadListener() {
            @Override
            public void onDownloadFinish(DownloadItem item) {
                mCallback.onDownloadFinished();
            }

            @Override
            public void onDownloadFinish(List<DownloadItem> downloadList) {
                mCallback.onDownloadFinished();
            }
        });
        dialog.show();
    }

    public void showImageDialog(Context context, CustomDialog.OnCustomDialogActionListener listener) {
        new ImageSelector(context, listener).show();
    }
}
