package com.king.mytennis.match;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.king.mytennis.download.DownloadItem;
import com.king.mytennis.http.Command;
import com.king.mytennis.http.RequestCallback;
import com.king.mytennis.http.bean.ImageUrlBean;
import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.Constants;
import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.service.ImageUtil;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;
import com.king.mytennis.match.UserMatchAdapter.ItemHolder;
import com.king.mytennis.view_v_7_0.controller.ObjectCache;
import com.king.mytennis.view_v_7_0.interaction.controller.InteractionController;
import com.king.mytennis.view_v_7_0.view.MatchActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/315 10:1
 */
public class UserMatchAdapter extends RecyclerView.Adapter<ItemHolder> implements RequestCallback, View.OnClickListener {

    private Context mContext;
    private List<UserMatchBean> list;

    private int colorHard;
    private int colorClay;
    private int colorGrass;
    private int colorInnerHard;
    private String[] courtValues;

    /**
     * 下载/浏览网络图库 控制器
     */
    private InteractionController interactionController;

    /**
     * 保存首次从文件夹加载的图片序号
     */
    private Map<String, Integer> imageIndexMap;

    public UserMatchAdapter(Context mContext, List<UserMatchBean> list) {
        this.list = list;
        this.mContext = mContext;
        courtValues = Constants.RECORD_MATCH_COURTS;
        colorHard = mContext.getResources().getColor(R.color.swipecard_text_hard);
        colorClay = mContext.getResources().getColor(R.color.swipecard_text_clay);
        colorGrass = mContext.getResources().getColor(R.color.swipecard_text_grass);
        colorInnerHard = mContext.getResources().getColor(R.color.swipecard_text_innerhard);

        imageIndexMap = new HashMap<>();
        interactionController = new InteractionController(this);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_user_match_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        UserMatchBean bean = list.get(position);

        String filePath;
        if (imageIndexMap.get(bean.getNameBean().getName()) == null) {
            filePath = ImageFactory.getMatchHeadPath(bean.getNameBean().getName()
                    , bean.getNameBean().getMatchBean().getCourt(), imageIndexMap);
        }
        else {
            filePath = ImageFactory.getMatchHeadPath(bean.getNameBean().getName()
                    , bean.getNameBean().getMatchBean().getCourt(), imageIndexMap.get(bean.getNameBean().getName()));
        }
        ImageUtil.load("file://" + filePath, holder.image, R.drawable.swipecard_default_img);
        holder.level.setText(bean.getNameBean().getMatchBean().getLevel());
        holder.court.setText(bean.getNameBean().getMatchBean().getCourt());
        holder.total.setText("总胜负  " + bean.getWin() + "胜" + bean.getLose() + "负");
        String best = "最佳战绩  " + bean.getBest();
        if (bean.getBestYears().length() > 0) {
            best = best + "(" + bean.getBestYears() + ")";
        }
        holder.best.setText(best);

        int color = getCardIndexColor(position);
        holder.court.setTextColor(color);

        holder.group.setTag(position);
        holder.group.setOnClickListener(this);
    }

    public int getCardIndexColor(int position) {
        int color = colorHard;
        if (position < list.size()) {
            if (list.get(position).getNameBean().getMatchBean().getCourt().equals(courtValues[1])) {
                color = colorClay;
            }
            else if (list.get(position).getNameBean().getMatchBean().getCourt().equals(courtValues[2])) {
                color = colorGrass;
            }
            else if (list.get(position).getNameBean().getMatchBean().getCourt().equals(courtValues[3])) {
                color = colorInnerHard;
            }
        }
        return color;
    }
    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }

    public void startDownload(int index) {
        interactionController.getImages(Command.TYPE_IMG_MATCH, list.get(index).getNameBean().getName());
    }

    public void refreshImage(int index) {
        ImageFactory.getMatchHeadPath(list.get(index).getNameBean().getName()
                , list.get(index).getNameBean().getMatchBean().getCourt(), imageIndexMap);
        notifyDataSetChanged();
    }

    public void deleteImage(int index) {
        final String match = list.get(index).getNameBean().getName();
        interactionController.showLocalImageDialog(mContext, new CustomDialog.OnCustomDialogActionListener() {
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

    @Override
    public void onServiceDisConnected() {
        Toast.makeText(mContext, R.string.gdb_server_offline, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestError() {
        Toast.makeText(mContext, R.string.gdb_request_fail, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onImagesReceived(final ImageUrlBean bean) {
        if (bean.getUrlList() == null) {
            String text = mContext.getString(R.string.image_not_found);
            text = String.format(text, bean.getKey());
            Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
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
                interactionController.showHttpImageDialog(mContext, new CustomDialog.OnCustomDialogActionListener() {
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
        interactionController.downloadImage(mContext, list, file.getPath(), true);
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();
        ObjectCache.putUserMatchBean(list.get(position));
        Intent intent = new Intent().setClass(view.getContext(), MatchActivity.class);
        view.getContext().startActivity(intent);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        ImageView image;
        ViewGroup group;
        TextView level, court, total, best;
        public ItemHolder(View convertView) {
            super(convertView);
            group = (ViewGroup) convertView.findViewById(R.id.match_group);
            level = (TextView) convertView.findViewById(R.id.swipecard_match_level);
            court = (TextView) convertView.findViewById(R.id.swipecard_match_court);
            total = (TextView) convertView.findViewById(R.id.swipecard_match_total);
            best = (TextView) convertView.findViewById(R.id.swipecard_match_best);
            image = (ImageView) convertView.findViewById(R.id.swipecard_match_img);
        }
    }
}
