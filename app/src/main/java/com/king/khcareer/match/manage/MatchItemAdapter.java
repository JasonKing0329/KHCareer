package com.king.khcareer.match.manage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;
import com.king.mytennis.view.R;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/20 13:27
 */
public class MatchItemAdapter extends MatchManageBaseAdapter {

    public MatchItemAdapter(List<MatchNameBean> list) {
        super(list);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_match_manage_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ItemHolder holder = (ItemHolder) viewHolder;
        MatchNameBean bean = list.get(position);
        holder.tvIndex.setText(String.valueOf(position + 1));
        holder.tvName.setText(bean.getName());
        holder.tvInfor.setText(bean.getMatchBean().getLevel() + "/" + bean.getMatchBean().getCourt()
                + " W" + String.valueOf(bean.getMatchBean().getWeek()));
        holder.tvCountry.setText(bean.getMatchBean().getCountry());
        holder.tvCity.setText(bean.getMatchBean().getCity());

        onBindCheckStatus(holder.check, position);

        onBindGroupStatus(holder.group, position);

        onBindImage(holder.image, position);
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
