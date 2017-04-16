package com.king.khcareer.match.manage;

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

import com.king.khcareer.download.DownloadItem;
import com.king.khcareer.model.http.Command;
import com.king.khcareer.model.http.RequestCallback;
import com.king.khcareer.model.http.bean.ImageUrlBean;
import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;
import com.king.khcareer.common.image.ImageUtil;
import com.king.khcareer.utils.DebugLog;
import com.king.khcareer.base.CustomDialog;
import com.king.mytennis.view.R;
import com.king.khcareer.common.image.interaction.controller.InteractionController;

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
public class MatchItemAdapter extends RecyclerView.Adapter<MatchItemAdapter.ItemHolder> implements View.OnClickListener, RequestCallback {

    private List<MatchNameBean> list;
    private boolean selectMode;
    private SparseBooleanArray mCheckMap;
    private OnMatchItemClickListener onMatchItemClickListener;

    /**
     * 下载/浏览网络图库 控制器
     */
    private InteractionController interactionController;

    /**
     * 保存首次从文件夹加载的图片序号
     */
    private Map<String, Integer> imageIndexMap;

    /**
     * 单击头像位置
     */
    private int nGroupPosition;

    private Context context;

    public MatchItemAdapter(List<MatchNameBean> list) {
        this.list = list;
        mCheckMap = new SparseBooleanArray();
        imageIndexMap = new HashMap<>();
        interactionController = new InteractionController(this);
    }

    public void setSelectMode(boolean selectMode) {
        this.selectMode = selectMode;
        if (!selectMode) {
            mCheckMap.clear();
        }
    }

    public void setOnMatchItemClickListener(OnMatchItemClickListener onMatchItemClickListener) {
        this.onMatchItemClickListener = onMatchItemClickListener;
    }

    public void setList(List<MatchNameBean> list) {
        this.list = list;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_match_manage_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        MatchNameBean bean = list.get(position);
        holder.tvIndex.setText(String.valueOf(position + 1));
        holder.tvName.setText(bean.getName());
        holder.tvInfor.setText(bean.getMatchBean().getLevel() + "/" + bean.getMatchBean().getCourt()
                + " W" + String.valueOf(bean.getMatchBean().getWeek()));
        holder.tvCountry.setText(bean.getMatchBean().getCountry());
        holder.tvCity.setText(bean.getMatchBean().getCity());
        holder.check.setVisibility(selectMode ? View.VISIBLE:View.GONE);
        holder.check.setChecked(mCheckMap.get(position));

        holder.group.setTag(position);
        holder.group.setOnClickListener(this);

        String filePath;
        if (imageIndexMap.get(bean.getName()) == null) {
            filePath = ImageFactory.getMatchHeadPath(bean.getName(), bean.getMatchBean().getCourt(), imageIndexMap);
        }
        else {
            filePath = ImageFactory.getMatchHeadPath(bean.getName(), bean.getMatchBean().getCourt(), imageIndexMap.get(bean.getName()));
        }
        ImageUtil.load("file://" + filePath, holder.image);
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
                if (onMatchItemClickListener != null) {
                    onMatchItemClickListener.onMatchItemClick(position);
                }
            }
        }
        else if (v instanceof ImageView) {
            nGroupPosition = (int) v.getTag(R.id.tag_record_list_player_group_index);

            AlertDialog.Builder dlg = new AlertDialog.Builder(v.getContext());
            dlg.setTitle(list.get(nGroupPosition).getName());
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

    private void onItemClickManage(int position) {
        final String match = list.get(position).getName();
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
                ImageUrlBean bean = interactionController.getMatchImageUrlBean(match);
                data.put("data", bean);
                data.put("flag", Command.TYPE_IMG_MATCH);
            }
        });
    }

    private void onItemClickDownload(int position) {
        interactionController.getImages(Command.TYPE_IMG_MATCH, list.get(position).getName());
    }

    private void onItemClickRefresh(int position) {
        String path = ImageFactory.getMatchHeadPath(list.get(position).getName(), list.get(position).getMatchBean().getCourt(), imageIndexMap);
        DebugLog.e(path);
        notifyDataSetChanged();
    }

    public List<MatchNameBean> getSelectedList() {
        List<MatchNameBean> dlist = new ArrayList<>();
        for (int i = 0; i < list.size(); i ++) {
            if (mCheckMap.get(i)) {
                dlist.add(list.get(i));
            }
        }
        return dlist;
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
                item.setFlag(Command.TYPE_IMG_MATCH);
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
                        data.put("flag", Command.TYPE_IMG_MATCH);
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
        File file = new File(Configuration.IMG_MATCH_BASE + key);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }
        interactionController.downloadImage(context, list, file.getPath(), true);
    }

    public interface OnMatchItemClickListener {
        void onMatchItemClick(int position);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        ViewGroup group;
        TextView tvIndex;
        TextView tvName;
        TextView tvCountry;
        TextView tvCity;
        TextView tvInfor;
        CheckBox check;
        ImageView image;
        public ItemHolder(View itemView) {
            super(itemView);
            group = (ViewGroup) itemView.findViewById(R.id.manage_item_group);
            tvIndex = (TextView) itemView.findViewById(R.id.manage_item_index);
            tvName = (TextView) itemView.findViewById(R.id.manage_item_name);
            tvCountry = (TextView) itemView.findViewById(R.id.manage_item_country);
            tvCity = (TextView) itemView.findViewById(R.id.manage_item_city);
            tvInfor = (TextView) itemView.findViewById(R.id.manage_item_infor);
            check = (CheckBox) itemView.findViewById(R.id.manage_item_check);
            image = (ImageView) itemView.findViewById(R.id.manage_item_image);
        }
    }
}
