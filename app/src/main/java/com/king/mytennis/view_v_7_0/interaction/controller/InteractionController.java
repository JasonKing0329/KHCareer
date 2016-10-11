package com.king.mytennis.view_v_7_0.interaction.controller;

import android.content.Context;

import com.king.mytennis.download.DownloadDialog;
import com.king.mytennis.download.DownloadItem;
import com.king.mytennis.http.AppHttpClient;
import com.king.mytennis.http.KHCareerHttpClient;
import com.king.mytennis.http.RequestCallback;
import com.king.mytennis.http.bean.ImageUrlBean;
import com.king.mytennis.model.Configuration;
import com.king.mytennis.update.GdbRespBean;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view_v_7_0.interaction.HttpImageSelector;
import com.king.mytennis.view_v_7_0.interaction.LocalImageSelector;

import java.io.File;
import java.util.ArrayList;
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

    /**
     * 从服务端获取key有哪些图片，将通过mCallback反馈所有图片的URL
     * @param flag 图片类型
     * @param key 图片关键字
     */
    public void getImages(final String flag, final String key) {
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
                            requestGetImages(flag, key);
                        }
                    }
                });

    }

    /**
     * 从服务端获取key有哪些图片，将通过mCallback反馈所有图片的URL
     * @param flag
     * @param key
     */
    private void requestGetImages(String flag, String key) {
        KHCareerHttpClient.getInstance().getService().getImages(flag, key)
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

    /**
     * 启动下载器进行图片下载
     * @param context
     * @param itemList 要下载的图片url列表
     * @param savePath 保存的路径
     * @param noOption 是否提示有多少需要下载
     */
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

    /**
     * 显示网络图片浏览框
     * @param context
     * @param listener
     */
    public void showHttpImageDialog(Context context, CustomDialog.OnCustomDialogActionListener listener) {
        new HttpImageSelector(context, listener).show();
    }

    /**
     * 显示本地图片浏览框
     * @param context
     * @param listener
     */
    public void showLocalImageDialog(Context context, CustomDialog.OnCustomDialogActionListener listener) {
        new LocalImageSelector(context, listener).show();
    }

    /**
     * 删除制定图片文件
     * @param list
     */
    public void deleteImages(List<String> list) {
        for (int i = 0; i < list.size(); i ++) {
            File file = new File(list.get(i));
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 获取本地match对应的图片
     * @param match
     * @return
     */
    public ImageUrlBean getMatchImageUrlBean(String match) {
        return createImageUrlBean(match, Configuration.IMG_MATCH_BASE);
    }

    /**
     * 获取本地player对应的图片
     * @param player
     * @return
     */
    public ImageUrlBean getPlayerImageUrlBean(String player) {
        return createImageUrlBean(player, Configuration.IMG_PLAYER_BASE);
    }

    /**
     * key对应的本地图片包括basePath目录下的单张jpg图片，和basePath下以key为文件夹里的所有图片
     * @param key
     * @param basePath
     * @return
     */
    private ImageUrlBean createImageUrlBean(String key, String basePath) {
        ImageUrlBean bean = new ImageUrlBean();
        bean.setUrlList(new ArrayList<String>());
        bean.setSizeList(new ArrayList<Long>());
        bean.setKey(key);

        // 先检查单张图片
        File file = new File(basePath + key + ".jpg");
        if (file.exists()) {
            bean.getUrlList().add(file.getPath());
            bean.getSizeList().add(file.length());
        }
        // 检查文件夹中的图片
        file = new File(basePath + key);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f:files) {
                bean.getUrlList().add(f.getPath());
                bean.getSizeList().add(f.length());
            }
        }
        return bean;
    }
}
