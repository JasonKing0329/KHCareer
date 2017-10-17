package com.king.khcareer.common.image.interaction;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.khcareer.model.http.Command;
import com.king.khcareer.model.http.bean.ImageItemBean;
import com.king.khcareer.model.http.bean.ImageUrlBean;
import com.king.mytennis.view.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/12 0012.
 * 抽象出基类Adapter，local浏览和http浏览，不同点仅仅在于加载图片的方式
 */
public abstract class ImageSelectorAdapter extends RecyclerView.Adapter {

    private final int TYPE_HEAD = 0;
    private final int TYPE_ITEM = 1;

    protected Context mContext;
    protected ImageUrlBean imageUrlBean;

    protected List<ItemPack> itemList;

    /**
     * key为ImageItemBean的 "key_url" 形式，调用ImageGridAdapter.getKey
     */
    private Map<String, Boolean> checkMap;

    public ImageSelectorAdapter(Context context, ImageUrlBean imageUrlBean) {
        mContext = context;
        this.imageUrlBean = imageUrlBean;
        checkMap = new HashMap<>();

        createItemPacks();
    }

    private void createItemPacks() {
        itemList = new ArrayList<>();
        if (imageUrlBean.getItemList() != null) {
            Map<String, GridItemPack> gridMaps = new HashMap<>();
            for (int i = 0; i < imageUrlBean.getItemList().size(); i ++) {
                ImageItemBean bean = imageUrlBean.getItemList().get(i);

                GridItemPack gPack = gridMaps.get(bean.getKey());
                if (gPack == null) {
                    // head第一次出现，添加head
                    HeadItemPack pack = new HeadItemPack();
                    String key = bean.getKey();
                    pack.viewType = TYPE_HEAD;
                    pack.typeName = getTypeName(key);
                    itemList.add(pack);
                    // 初始化grid list
                    gPack = new GridItemPack();
                    gPack.viewType = TYPE_ITEM;
                    gPack.itemList = new ArrayList<>();
                    itemList.add(gPack);
                    gridMaps.put(bean.getKey(), gPack);
                }
                // 对应head的list中添加bean
                gPack.itemList.add(bean);
            }
        }
    }

    private String getTypeName(String key) {
        if (key.equals(Command.TYPE_IMG_PLAYER)) {
            return "Player Normal";
        }
        else if (key.equals(Command.TYPE_IMG_MATCH)) {
            return "Match";
        }
        else {
            return "Player Head";
        }
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).viewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            return new HeadHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_image_selector_head, parent, false));
        }
        else {
            return new GridHolder(LayoutInflater.from(mContext).inflate(R.layout.adapter_image_selector_grid, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vholder, int position) {
        if (vholder instanceof HeadHolder) {
            HeadHolder holder = (HeadHolder) vholder;
            HeadItemPack pack = (HeadItemPack) itemList.get(position);
            holder.name.setText(pack.typeName);
        }
        else if (vholder instanceof GridHolder) {
            GridHolder holder = (GridHolder) vholder;
            GridItemPack pack = (GridItemPack) itemList.get(position);
            if (holder.adapter == null) {
                holder.adapter = new ImageGridAdapter(this);
                holder.adapter.setCheckMap(checkMap);
                holder.adapter.setList(pack.itemList);
                holder.rvGrid.setAdapter(holder.adapter);
            }
            else {
                holder.adapter.setList(pack.itemList);
                holder.adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /**
     * 加载item图片
     * @param imageView
     * @param bean
     */
    public abstract void onBindItemImage(ImageView imageView, ImageItemBean bean);

    /**
     * 加载Item角标
     * @param markNew
     * @param bean
     */
    public abstract void onBindItemMark(ImageView markNew, ImageItemBean bean);

    public List<ImageItemBean> getSelectedKey() {
        List<ImageItemBean> result = new ArrayList<>();
        for (int i = 0; i < itemList.size(); i ++) {
            if (itemList.get(i) instanceof GridItemPack) {
                List<ImageItemBean> imageList = ((GridItemPack) itemList.get(i)).itemList;
                for (ImageItemBean bean:imageList) {
                    Boolean icChecked = checkMap.get(ImageGridAdapter.getCheckKey(bean));
                    if (icChecked != null && icChecked) {
                        result.add(bean);
                    }
                }
            }
        }
        return result;
    }

    public static class HeadHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public HeadHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_key);
        }
    }

    public static class GridHolder extends RecyclerView.ViewHolder {

        RecyclerView rvGrid;
        ImageGridAdapter adapter;

        public GridHolder(View itemView) {
            super(itemView);
            rvGrid = (RecyclerView) itemView.findViewById(R.id.rv_grid);
            GridLayoutManager manager = new GridLayoutManager(itemView.getContext(), 3);
            rvGrid.setLayoutManager(manager);
        }

    }

    protected class ItemPack {
        int viewType;
    }

    protected class HeadItemPack extends ItemPack {
        String typeName;
    }

    protected class GridItemPack extends ItemPack {
        List<ImageItemBean> itemList;
    }

}
