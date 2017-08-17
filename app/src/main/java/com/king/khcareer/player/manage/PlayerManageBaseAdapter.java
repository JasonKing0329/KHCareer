package com.king.khcareer.player.manage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.king.khcareer.base.CustomDialog;
import com.king.khcareer.base.KApplication;
import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.image.glide.GlideOptions;
import com.king.khcareer.common.image.interaction.controller.InteractionController;
import com.king.khcareer.download.DownloadItem;
import com.king.khcareer.model.http.Command;
import com.king.khcareer.model.http.RequestCallback;
import com.king.khcareer.model.http.bean.ImageUrlBean;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.khcareer.settings.SettingProperty;
import com.king.khcareer.utils.ConstellationUtil;
import com.king.mytennis.view.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/8/17 16:14
 */
public abstract class PlayerManageBaseAdapter extends RecyclerView.Adapter implements View.OnClickListener, RequestCallback {

    private Context context;

    protected List<PlayerBean> list;
    protected boolean selectMode;
    protected SparseBooleanArray mCheckMap;
    private OnPlayerItemClickListener onPlayerItemClickListener;

    /**
     * 单击头像位置
     */
    private int nGroupPosition;

    /**
     * 保存首次从文件夹加载的图片序号
     */
    protected Map<String, Integer> playerImageIndexMap;

    /**
     * Player list，下载图片/刷新头像/管理图片
     */
    protected InteractionController interactionController;

    public PlayerManageBaseAdapter(Context context, List<PlayerBean> list) {
        this.context = context;
        this.list = list;
        mCheckMap = new SparseBooleanArray();
        playerImageIndexMap = new HashMap<>();
        interactionController = new InteractionController(this);
    }

    public void setSelectMode(boolean selectMode) {
        this.selectMode = selectMode;
        if (!selectMode) {
            mCheckMap.clear();
        }
    }

    public void setList(List<PlayerBean> list) {
        this.list = list;
    }

    public void setOnPlayerItemClickListener(OnPlayerItemClickListener onPlayerItemClickListener) {
        this.onPlayerItemClickListener = onPlayerItemClickListener;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }

    public List<PlayerBean> getSelectedList() {
        List<PlayerBean> dlist = new ArrayList<>();
        for (int i = 0; i < list.size(); i ++) {
            if (mCheckMap.get(i)) {
                dlist.add(list.get(i));
            }
        }
        return dlist;
    }

    /**
     * Item背景色
     * @param position
     * @param view
     */
    protected void updateItemBackground(int position, View view) {
        if (position < PubDataProvider.VIRTUAL_PLAYER) {// 显示为浅灰背景
            view.setBackgroundColor(context.getResources().getColor(R.color.lightgrey));
        }
        else {
            view.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
    }

    protected boolean isSortByConstellation() {
        return SettingProperty.getPlayerSortMode(context) == SettingProperty.VALUE_SORT_PLAYER_CONSTELLATION;
    }

    protected String getConstellation(int position) {
        String constellation;
        try {
            constellation = ConstellationUtil.getConstellationEng(list.get(position).getBirthday());
            constellation = constellation.concat("(").concat(list.get(position).getBirthday()).concat(")");
        } catch (ConstellationUtil.ConstellationParseException e) {
            e.printStackTrace();
            constellation = list.get(position).getBirthday();
        }
        return constellation;
    }

    /**
     * image path
     * @param position
     * @return
     */
    protected String getPlayerPath(int position) {
        String filePath;
        if (playerImageIndexMap.get(list.get(position).getNameChn()) == null) {
            filePath = ImageFactory.getPlayerHeadPath(list.get(position).getNameChn(), playerImageIndexMap);
        }
        else {
            filePath = ImageFactory.getPlayerHeadPath(list.get(position).getNameChn(), playerImageIndexMap.get(list.get(position).getNameChn()));
        }
        return filePath;
    }

    /**
     * show and set listener
     * @param path
     * @param position
     * @param view
     */
    protected void updateItemImage(String path, int position, ImageView view) {
        if (path == null) {
            Glide.with(KApplication.getInstance())
                    .load(R.drawable.pic_def)
                    .into(view);
        }
        else {
            Glide.with(KApplication.getInstance())
                    .load("file://" + path)
                    .apply(GlideOptions.getDefaultPlayerOptions())
                    .into(view);
        }
        view.setOnClickListener(this);
        view.setTag(R.id.tag_record_list_player_group_index, position);
    }

    /**
     * check status
     * @param position
     * @param checkBox
     */
    protected void updateCheckStatus(int position, CheckBox checkBox) {
        if (selectMode) {
            if (position < PubDataProvider.VIRTUAL_PLAYER) {// 不允许删除
                checkBox.setVisibility(View.INVISIBLE);
            }
            else {
                checkBox.setVisibility(View.VISIBLE);
            }
        }
        else {
            checkBox.setVisibility(View.GONE);
        }
        checkBox.setChecked(mCheckMap.get(position));
    }

    @Override
    public void onClick(View v) {
        if (v instanceof ViewGroup) {
            int position = (int) v.getTag();
            if (selectMode) {
                mCheckMap.put(position, !mCheckMap.get(position));
                notifyDataSetChanged();
            }
            else {
                if (onPlayerItemClickListener != null) {
                    onPlayerItemClickListener.onPlayerItemClick(position);
                }
            }
        }
        else if (v instanceof ImageView) {
            nGroupPosition = (int) v.getTag(R.id.tag_record_list_player_group_index);

            AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
            dlg.setTitle(list.get(nGroupPosition).getNameChn());
            dlg.setItems(v.getContext().getResources().getStringArray(R.array.cptdlg_item_oper)
                    , itemListener);
            dlg.show();
        }
    }

    DialogInterface.OnClickListener itemListener = new DialogInterface.OnClickListener() {

        private final int DOWNLOAD = 0;
        private final int REFRESH = 1;
        private final int MANAGE = 2;
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == DOWNLOAD) {
                onItemClickDownload(nGroupPosition);
            }
            else if (which == REFRESH) {
                onItemClickRefresh(nGroupPosition);
            }
            else if (which == MANAGE) {
                onItemClickManage(nGroupPosition);
            }
        }
    };

    public void onItemClickDownload(int position) {
        String name = list.get(position).getNameChn();
        interactionController.getImages(Command.TYPE_IMG_PLAYER_HEAD, name);
    }

    public void onItemClickRefresh(int position) {
        String name = list.get(position).getNameChn();
        ImageFactory.getPlayerHeadPath(name, playerImageIndexMap);
        notifyDataSetChanged();
    }

    public void onItemClickManage(int position) {
        final String name = list.get(position).getNameChn();
        interactionController.showLocalImageDialog(context, new CustomDialog.OnCustomDialogActionListener() {
            @Override
            public boolean onSave(Object object) {
                List<String> list = (List<String>) object;
                interactionController.deleteImages(list);
                notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onCancel() {
                return false;
            }

            @Override
            public void onLoadData(HashMap<String, Object> data) {
                ImageUrlBean bean = interactionController.getPlayerHeadUrlBean(name);
                data.put("data", bean);
                data.put("flag", Command.TYPE_IMG_PLAYER_HEAD);
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
        if (bean.getUrlList() == null) {
            String text = context.getString(R.string.image_not_found);
            text = String.format(text, bean.getKey());
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
        else {
            // 直接下载更新
            if (bean.getUrlList().size() == 1) {
                List<DownloadItem> list = new ArrayList<>();
                DownloadItem item = new DownloadItem();
                item.setKey(bean.getUrlList().get(0));
                item.setFlag(Command.TYPE_IMG_PLAYER_HEAD);
                item.setSize(bean.getSizeList().get(0));

                String url = bean.getUrlList().get(0);
                if (url.contains("/")) {
                    String[] array = url.split("/");
                    url = array[array.length - 1];
                }
                item.setName(url);

                list.add(item);

                startDownload(list, bean.getKey());
            }
            // 显示对话框选择下载
            else {
                interactionController.showHttpImageDialog(context, new CustomDialog.OnCustomDialogActionListener() {
                    @Override
                    public boolean onSave(Object object) {
                        List<DownloadItem> list = (List<DownloadItem>) object;
                        startDownload(list, bean.getKey());
                        return false;
                    }

                    @Override
                    public boolean onCancel() {
                        return false;
                    }

                    @Override
                    public void onLoadData(HashMap<String, Object> data) {
                        data.put("data", bean);
                        data.put("flag", Command.TYPE_IMG_PLAYER_HEAD);
                    }
                });
            }
        }
    }

    @Override
    public void onDownloadFinished() {
        notifyDataSetChanged();
    }

    private void startDownload(List<DownloadItem> list, String key) {
        File file = new File(Configuration.IMG_PLAYER_HEAD + key);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }
        interactionController.downloadImage(context, list, file.getPath(), true);
    }

    public interface OnPlayerItemClickListener {
        void onPlayerItemClick(int position);
    }

}
