package com.king.khcareer.common.image.interaction;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.king.khcareer.base.CustomDialog;
import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.common.image.interaction.controller.InteractionController;
import com.king.khcareer.download.DownloadItem;
import com.king.khcareer.model.http.Command;
import com.king.khcareer.model.http.RequestCallback;
import com.king.khcareer.model.http.bean.ImageItemBean;
import com.king.khcareer.model.http.bean.ImageUrlBean;
import com.king.mytennis.view.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 描述: 从服务端下载、刷新、管理本地图片，弹出列表及后续动作
 * <p/>作者：景阳
 * <p/>创建时间: 2017/10/17 13:06
 */
public class ImageManager implements RequestCallback {

    private final Context context;

    /**
     * Player list，下载图片/刷新头像/管理图片
     */
    protected InteractionController interactionController;

    private OnActionListener onActionListener;

    private DataProvider dataProvider;

    private int position;
    private String flag;
    private String key;

    public ImageManager(Context context) {
        this.context = context;
        interactionController = new InteractionController(this);
    }

    public InteractionController getInteractionController() {
        return interactionController;
    }

    /**
     * 直接从服务端下载
     * @param flag
     * @param key
     */
    public void download(String flag, String key) {
        this.flag = flag;
        this.key = key;
        onItemClickDownload();
    }

    /**
     * 直接管理本地图片列表
     */
    public void manageLocal() {
        onItemClickManage();
    }

    /**
     * 显示从服务端下载、刷新、管理本地图片列表
     * @param title
     * @param position
     * @param flag
     * @param key
     */
    public void showOptions(String title, int position, String flag, String key) {
        this.position = position;
        this.flag = flag;
        this.key = key;

        AlertDialog.Builder dlg = new AlertDialog.Builder(context);
        dlg.setTitle(title);
        dlg.setItems(context.getResources().getStringArray(R.array.cptdlg_item_oper)
                , itemListener);
        dlg.show();
    }

    DialogInterface.OnClickListener itemListener = new DialogInterface.OnClickListener() {

        private final int DOWNLOAD = 0;
        private final int REFRESH = 1;
        private final int MANAGE = 2;
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DOWNLOAD) {
                onItemClickDownload();
            }
            else if (which == REFRESH) {
                if (onActionListener != null) {
                    onActionListener.onRefresh(position);
                }
            }
            else if (which == MANAGE) {
                onItemClickManage();
            }
        }
    };

    /**
     * 设置事件回调
     * @param onActionListener
     */
    public void setOnActionListener(OnActionListener onActionListener) {
        this.onActionListener = onActionListener;
    }

    /**
     * 设置数据提供者
     * @param dataProvider
     */
    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    private void onItemClickDownload() {
        interactionController.getImages(flag, key);
    }

    private void onItemClickManage() {
        interactionController.showLocalImageDialog(context, new CustomDialog.OnCustomDialogActionListener() {
            @Override
            public boolean onSave(Object object) {
                List<String> list = (List<String>) object;
                interactionController.deleteImages(list, true);
                if (onActionListener != null) {
                    onActionListener.onManageFinished();
                }
                return false;
            }

            @Override
            public boolean onCancel() {
                return false;
            }

            @Override
            public void onLoadData(HashMap<String, Object> data) {
                if (dataProvider != null) {
                    ImageUrlBean bean = dataProvider.createImageUrlBean(interactionController);
                    data.put("data", bean);
                }
            }
        });
    }

    @Override
    public void onServiceDisConnected() {
        Toast.makeText(context, R.string.gdb_server_offline, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestError() {
        Toast.makeText(context, R.string.gdb_request_fail, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onImagesReceived(final ImageUrlBean bean) {
        if (bean.getItemList() == null) {
            String text = context.getString(R.string.image_not_found);
            text = String.format(text, bean.getKey());
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
        else {
            // 直接下载更新
            if (bean.getItemList().size() == 1) {
                ImageItemBean itemBean = bean.getItemList().get(0);
                List<DownloadItem> list = new ArrayList<>();
                DownloadItem item = new DownloadItem();
                item.setKey(itemBean.getUrl());
                item.setFlag(itemBean.getKey());
                item.setSize(itemBean.getSize());
                item.setTargetPath(getSavePathFromFlag(itemBean.getKey(), bean.getKey()));

                String url = itemBean.getUrl();
                if (url.contains("/")) {
                    String[] array = url.split("/");
                    url = array[array.length - 1];
                }
                item.setName(url);

                list.add(item);

                startDownload(list);
            }
            // 显示对话框选择下载
            else {
                interactionController.showHttpImageDialog(context, new CustomDialog.OnCustomDialogActionListener() {
                    @Override
                    public boolean onSave(Object object) {
                        List<DownloadItem> list = (List<DownloadItem>) object;
                        for (DownloadItem item:list) {
                            item.setTargetPath(getSavePathFromFlag(item.getFlag(), bean.getKey()));
                        }
                        startDownload(list);
                        return false;
                    }

                    @Override
                    public boolean onCancel() {
                        return false;
                    }

                    @Override
                    public void onLoadData(HashMap<String, Object> data) {
                        data.put("data", bean);
                    }
                });
            }
        }
    }

    @Override
    public void onDownloadFinished() {
        if (onActionListener != null) {
            onActionListener.onDownloadFinished();
        }
    }

    private void startDownload(List<DownloadItem> list) {
        interactionController.downloadImage(context, list, null, true);
    }

    /**
     * 根据flag设置默认保存目录
     * @param flag
     * @param key
     * @return
     */
    private String getSavePathFromFlag(String flag, String key) {
        if (flag.equals(Command.TYPE_IMG_PLAYER)) {
            File file = new File(Configuration.IMG_PLAYER_BASE + key);
            if (!file.exists() || !file.isDirectory()) {
                file.mkdir();
            }
            return file.getPath();
        }
        else if (flag.equals(Command.TYPE_IMG_PLAYER_HEAD)) {
            File file = new File(Configuration.IMG_PLAYER_HEAD + key);
            if (!file.exists() || !file.isDirectory()) {
                file.mkdir();
            }
            return file.getPath();
        }
        else if (flag.equals(Command.TYPE_IMG_MATCH)) {
            File file = new File(Configuration.IMG_MATCH_BASE + key);
            if (!file.exists() || !file.isDirectory()) {
                file.mkdir();
            }
            return file.getPath();
        }
        return null;
    }

    /**
     * 数据提供
     */
    public interface DataProvider {
        ImageUrlBean createImageUrlBean(InteractionController interactionController);
    }

    /**
     * 事件回调
     */
    public interface OnActionListener {
        void onRefresh(int position);

        void onManageFinished();

        void onDownloadFinished();
    }
}
