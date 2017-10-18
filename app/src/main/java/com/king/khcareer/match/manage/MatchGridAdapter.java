package com.king.khcareer.match.manage;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;
import com.king.mytennis.view.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/10/18 10:53
 */
public class MatchGridAdapter extends MatchManageBaseAdapter implements View.OnClickListener {

    public MatchGridAdapter(List<MatchNameBean> list) {
        super(list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_match_grid, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vholder, int position) {
        ItemHolder holder = (ItemHolder) vholder;
        MatchNameBean bean = list.get(position);
        holder.tvName.setText(bean.getName());
        holder.tvCountry.setText(bean.getMatchBean().getCountry() + "/" + bean.getMatchBean().getCity());
        holder.tvLine2.setText(bean.getMatchBean().getLevel() + "/" + bean.getMatchBean().getCourt());
        holder.tvWeek.setText("W" + String.valueOf(bean.getMatchBean().getWeek()));

        onBindCheckStatus(holder.cbCheck, position);

        onBindGroupStatus(holder.groupCard, position);

        onBindImage(holder.ivMatch, position);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_match)
        ImageView ivMatch;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.group_name)
        LinearLayout groupName;
        @BindView(R.id.tv_country)
        TextView tvCountry;
        @BindView(R.id.tv_line2)
        TextView tvLine2;
        @BindView(R.id.tv_week)
        TextView tvWeek;
        @BindView(R.id.cb_check)
        CheckBox cbCheck;
        @BindView(R.id.group_card)
        CardView groupCard;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}