package com.king.khcareer.common.image.interaction.controller;

import android.content.Context;

import com.king.khcareer.download.DownloadDialog;
import com.king.khcareer.download.DownloadItem;
import com.king.khcareer.model.http.AppHttpClient;
import com.king.khcareer.model.http.Command;
import com.king.khcareer.model.http.KHCareerHttpClient;
import com.king.khcareer.model.http.RequestCallback;
import com.king.khcareer.model.http.bean.ImageItemBean;
import com.king.khcareer.model.http.bean.ImageUrlBean;
import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.update.GdbRespBean;
import com.king.khcareer.base.CustomDialog;
import com.king.khcareer.common.image.interaction.HttpImageSelector;
import com.king.khcareer.common.image.interaction.LocalImageSelector;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
                .subscribe(new Consumer<GdbRespBean>() {
                    @Override
                    public void accept(GdbRespBean gdbRespBean) throws Exception {
                        if (gdbRespBean.isOnline()) {
                            requestGetImages(flag, key);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        mCallback.onServiceDisConnected();
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
                .subscribe(new Consumer<ImageUrlBean>() {
                    @Override
                    public void accept(ImageUrlBean bean) throws Exception {
                        mCallback.onImagesReceived(bean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        mCallback.onRequestError();
                    }
                });
    }

    /**
     * 启动下载器进行图片下载
     * @param context
     * @param itemList 要下载的图片url列表
     * @param savePath 保存的路径，如果为null，则从DownloadItem里的flag进行判断
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
     * 删除指定图片文件
     * @param list
     * @param deleteParentWhileEmpty 删除文件后如果父目录下再无其他文件，则删除父目录
     */
    public void deleteImages(List<String> list, boolean deleteParentWhileEmpty) {
        for (int i = 0; i < list.size(); i ++) {
            File file = new File(list.get(i));
            if (file.exists()) {
                file.delete();

                if (deleteParentWhileEmpty) {
                    File parent = file.getParentFile();
                    if (parent.exists() && parent.list().length == 0) {
                        parent.delete();
                    }
                }
            }
        }
    }

    /**
     * 获取本地match对应的图片
     * @param match
     * @return
     */
    public ImageUrlBean getMatchImageUrlBean(String match) {
        String[] folders = new String[]{Configuration.IMG_MATCH_BASE};
        String[] cmdTypes = new String[]{Command.TYPE_IMG_MATCH};
        return createImageUrlBean(match, folders, cmdTypes);
    }

    /**
     * 获取本地player对应的图片
     * @param player
     * @return
     */
    public ImageUrlBean getPlayerImageUrlBean(String player) {
        String[] folders = new String[]{Configuration.IMG_PLAYER_BASE, Configuration.IMG_PLAYER_HEAD};
        String[] cmdTypes = new String[]{Command.TYPE_IMG_PLAYER, Command.TYPE_IMG_PLAYER_HEAD};
        return createImageUrlBean(player, folders, cmdTypes);
    }

    /**
     * key对应的本地图片包括basePath目录下的单张jpg图片，和basePath下以key为文件夹里的所有图片
     * @param key
     * @param basePaths
     * @return
     */
    private ImageUrlBean createImageUrlBean(String key, String[] basePaths, String[] cmdTypes) {
        ImageUrlBean bean = new ImageUrlBean();
        bean.setKey(key);
        bean.setItemList(new ArrayList<ImageItemBean>());

        for (int i = 0; i < basePaths.length; i ++) {
            String basePath = basePaths[i];
            // 先检查单张图片
            File file = new File(basePath + key + ".jpg");
            if (file.exists()) {
                ImageItemBean itemBean = new ImageItemBean();
                itemBean.setKey(cmdTypes[i]);
                itemBean.setUrl(file.getPath());
                itemBean.setSize(file.length());
                bean.getItemList().add(itemBean);
            }
            // 检查文件夹中的图片
            file = new File(basePath + key);
            if (file.exists() && file.isDirectory()) {
                File[] files = file.listFiles();
                for (File f:files) {
                    ImageItemBean itemBean = new ImageItemBean();
                    itemBean.setKey(cmdTypes[i]);
                    itemBean.setUrl(f.getPath());
                    itemBean.setSize(f.length());
                    bean.getItemList().add(itemBean);
                }
            }
        }
        return bean;
    }
}
