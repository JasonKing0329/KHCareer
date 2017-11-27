package com.king.khcareer.player.manage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.mytennis.view.R;
import com.king.khcareer.pubview.CircleImageView;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/20 13:27
 */
public class PlayerItemAdapter extends PlayerManageBaseAdapter {

    public PlayerItemAdapter(Context context, List<PlayerBean> list) {
        super(context, list);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_player_manage_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        PlayerItemAdapter.ItemHolder holder = (ItemHolder) h;
        PlayerBean bean = list.get(position);
        holder.tvIndex.setText(String.valueOf(position + 1));
        holder.tvName.setText(bean.getNameChn());
        holder.tvNameEng.setText(bean.getNameEng());
        // 当前排序是按星座排序，显示星座名称
        if (isSortByConstellation()) {
            holder.tvBirthday.setText(getConstellation(position));
        }
        else {
            holder.tvBirthday.setText(bean.getBirthday());
        }
        holder.tvCountry.setText(bean.getCountry());

        holder.group.setTag(position);
        holder.group.setOnClickListener(this);

        // item 背景
        updateItemBackground(position, holder.container);

        // image view相关显示及事件
        updateItemImage(getPlayerPath(position), position, holder.image);

        // check状态
        updateCheckStatus(position, holder.check);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        ViewGroup container;
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
            container = (ViewGroup) itemView.findViewById(R.id.manage_item_container);
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
