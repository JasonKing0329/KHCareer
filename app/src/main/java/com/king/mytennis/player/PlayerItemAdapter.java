package com.king.mytennis.player;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.king.mytennis.download.DownloadItem;
import com.king.mytennis.http.Command;
import com.king.mytennis.http.RequestCallback;
import com.king.mytennis.http.bean.ImageUrlBean;
import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.pubdata.bean.PlayerBean;
import com.king.mytennis.service.ImageUtil;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;
import com.king.mytennis.view_v_7_0.interaction.controller.InteractionController;
import com.king.mytennis.view_v_7_0.view.CircleImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/20 13:27
 */
public class PlayerItemAdapter extends RecyclerView.Adapter<PlayerItemAdapter.ItemHolder> implements View.OnClickListener, RequestCallback {

    private List<PlayerBean> list;
    private boolean selectMode;
    private SparseBooleanArray mCheckMap;
    private OnPlayerItemClickListener onPlayerItemClickListener;

    /**
     * 保存首次从文件夹加载的图片序号
     */
    private Map<String, Integer> playerImageIndexMap;

    /**
     * Player list，下载图片/刷新头像/管理图片
     */
    private InteractionController interactionController;

    /**
     * 单击头像位置
     */
    private int nGroupPosition;

    private Context context;

    public PlayerItemAdapter(List<PlayerBean> list) {
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

    public void setOnPlayerItemClickListener(OnPlayerItemClickListener onPlayerItemClickListener) {
        this.onPlayerItemClickListener = onPlayerItemClickListener;
    }

    public void setList(List<PlayerBean> list) {
        this.list = list;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_player_manage_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        PlayerBean bean = list.get(position);
        holder.tvIndex.setText(String.valueOf(position + 1));
        holder.tvName.setText(bean.getNameChn());
        holder.tvNameEng.setText(bean.getNameEng());
        holder.tvBirthday.setText(bean.getBirthday());
        holder.tvCountry.setText(bean.getCountry());
        if (selectMode) {
            if (position < PlayerManageActivity.FIXED_PLAYER) {// 不允许删除
                holder.check.setVisibility(View.INVISIBLE);
            }
            else {
                holder.check.setVisibility(View.VISIBLE);
            }
        }
        else {
            holder.check.setVisibility(View.GONE);
        }
        holder.check.setChecked(mCheckMap.get(position));

        holder.group.setTag(position);
        holder.group.setOnClickListener(this);

        String filePath;
        if (playerImageIndexMap.get(list.get(position).getNameChn()) == null) {
            filePath = ImageFactory.getPlayerHeadPath(list.get(position).getNameChn(), playerImageIndexMap);
        }
        else {
            filePath = ImageFactory.getPlayerHeadPath(list.get(position).getNameChn(), playerImageIndexMap.get(list.get(position).getNameChn()));
        }
        ImageUtil.load("file://" + filePath, holder.image, R.drawable.icon_list);
        holder.image.setOnClickListener(this);
        holder.image.setTag(R.id.tag_record_list_player_group_index, position);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
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

    public List<PlayerBean> getSelectedList() {
        List<PlayerBean> dlist = new ArrayList<>();
        for (int i = 0; i < list.size(); i ++) {
            if (mCheckMap.get(i)) {
                dlist.add(list.get(i));
            }
        }
        return dlist;
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

    public static class ItemHolder extends RecyclerView.ViewHolder {

        ViewGroup group;
        TextView tvIndex;
        TextView tvName;
        TextView tvNameEng;
        TextView tvCountry;
        TextView tvBirthday;
        CheckBox check;
        CircleImageView image;
        public ItemHolder(View itemView) {
            super(itemView);
            group = (ViewGroup) itemView.findViewById(R.id.manage_item_group);
            tvIndex = (TextView) itemView.findViewById(R.id.manage_item_index);
            tvName = (TextView) itemView.findViewById(R.id.manage_item_name);
            tvCountry = (TextView) itemView.findViewById(R.id.manage_item_country);
            tvNameEng = (TextView) itemView.findViewById(R.id.manage_item_name_eng);
            tvBirthday = (TextView) itemView.findViewById(R.id.manage_item_birthday);
            check = (CheckBox) itemView.findViewById(R.id.manage_item_check);
            image = (CircleImageView) itemView.findViewById(R.id.manage_item_image);
        }
    }
}
