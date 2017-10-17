package com.king.khcareer.common.image.interaction;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.khcareer.model.http.bean.ImageItemBean;
import com.king.khcareer.utils.FileSizeUtil;
import com.king.mytennis.view.R;

import java.util.List;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/10/17 14:39
 */
public class ImageGridAdapter extends RecyclerView.Adapter<ImageGridAdapter.BgHolder> implements View.OnClickListener {

    private ImageSelectorAdapter imageSelectorAdapter;

    protected List<ImageItemBean> itemList;
    protected Map<String,Boolean> mCheckMap;

    public static String getCheckKey(ImageItemBean bean) {
        return bean.getKey() + "_" + bean.getUrl();
    }

    public ImageGridAdapter(ImageSelectorAdapter imageSelectorAdapter) {
        this.imageSelectorAdapter = imageSelectorAdapter;
    }

    public void setList(List<ImageItemBean> list) {
        this.itemList = list;
    }

    public void setCheckMap(Map<String,Boolean> checkMap) {
        this.mCheckMap = checkMap;
    }

    @Override
    public BgHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BgHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_image_selector, parent, false));
    }

    @Override
    public void onBindViewHolder(BgHolder holder, int position) {
        ImageItemBean bean = itemList.get(position);

        holder.position = position;
        holder.container.setTag(holder);
        holder.container.setOnClickListener(this);
        Boolean isChecked = mCheckMap.get(getCheckKey(bean));
        if (isChecked != null && isChecked) {
            holder.check.setChecked(true);
        }
        else {
            holder.check.setChecked(false);
        }
        holder.name.setText(FileSizeUtil.convertFileSize(bean.getSize()));

        imageSelectorAdapter.onBindItemImage(holder.image, bean);
        imageSelectorAdapter.onBindItemMark(holder.markNew, bean);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public void onClick(View v) {
        BgHolder holder = (BgHolder) v.getTag();
        boolean check = !holder.check.isChecked();
        mCheckMap.put(getCheckKey(itemList.get(holder.position)), check);
        notifyDataSetChanged();
    }

    public static class BgHolder extends RecyclerView.ViewHolder {

        public ViewGroup container;
        public TextView name;
        public ImageView image;
        public CheckBox check;
        public int position;
        public ImageView markNew;

        public BgHolder(View itemView) {
            super(itemView);
            container = (ViewGroup) itemView.findViewById(R.id.bg_selector_container);
            name = (TextView) itemView.findViewById(R.id.img_selector_size);
            image = (ImageView) itemView.findViewById(R.id.img_selector_img);
            check = (CheckBox) itemView.findViewById(R.id.img_selector_check);
            markNew = (ImageView) itemView.findViewById(R.id.img_selector_mark_new);
        }
    }

}
