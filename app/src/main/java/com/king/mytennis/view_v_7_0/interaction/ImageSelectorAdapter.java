package com.king.mytennis.view_v_7_0.interaction;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.mytennis.http.bean.ImageUrlBean;
import com.king.mytennis.service.ImageUtil;
import com.king.mytennis.utils.DebugLog;
import com.king.mytennis.utils.FileSizeUtil;
import com.king.mytennis.view.R;
import com.king.mytennis.view.settings.SettingProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/7/12 0012.
 */
public class ImageSelectorAdapter extends RecyclerView.Adapter<ImageSelectorAdapter.BgHolder>
    implements View.OnClickListener{

    private Context mContext;
    private ImageUrlBean imageUrlBean;

    private SparseBooleanArray mCheckMap;

    public ImageSelectorAdapter(Context context, ImageUrlBean imageUrlBean) {
        mContext = context;
        this.imageUrlBean = imageUrlBean;
        mCheckMap = new SparseBooleanArray();
        if (imageUrlBean.getUrlList() != null) {
            for (int i = 0; i < imageUrlBean.getUrlList().size(); i ++) {
                mCheckMap.put(i, false);
            }
        }
    }
    @Override
    public BgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_image_selector, parent, false);
        BgHolder holder = new BgHolder(view);
        holder.container = (ViewGroup) view.findViewById(R.id.bg_selector_container);
        holder.name = (TextView) view.findViewById(R.id.img_selector_size);
        holder.image = (ImageView) view.findViewById(R.id.img_selector_img);
        holder.check = (CheckBox) view.findViewById(R.id.img_selector_check);
        return holder;
    }

    @Override
    public void onBindViewHolder(BgHolder holder, int position) {
        holder.position = position;
        holder.container.setTag(holder);
        holder.container.setOnClickListener(this);
        if (mCheckMap.get(position)) {
            holder.check.setChecked(true);
        }
        else {
            holder.check.setChecked(false);
        }
        holder.name.setText(FileSizeUtil.convertFileSize(imageUrlBean.getSizeList().get(position)));
        String url = SettingProperty.getServerBaseUrl(mContext) + imageUrlBean.getUrlList().get(position);
        DebugLog.e(url);
        ImageUtil.load(url, holder.image);
    }

    @Override
    public int getItemCount() {
        return imageUrlBean.getUrlList().size();
    }

    @Override
    public void onClick(View v) {
        BgHolder holder = (BgHolder) v.getTag();
        boolean check = !holder.check.isChecked();
        mCheckMap.put(holder.position, check);
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedKey() {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < mCheckMap.size(); i ++) {
            if (mCheckMap.get(i)) {
                result.add(i);
            }
        }
        return result;
    }

    public static class BgHolder extends RecyclerView.ViewHolder {

        public ViewGroup container;
        public TextView name;
        public ImageView image;
        public CheckBox check;
        public int position;

        public BgHolder(View itemView) {
            super(itemView);
        }
    }
}
