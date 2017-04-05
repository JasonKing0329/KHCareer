package com.king.mytennis.score.rank;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.mytennis.view.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/5 11:45
 */
public class RankItemAdapter extends RecyclerView.Adapter<RankItemAdapter.RankItemHolder> implements View.OnClickListener {

    private List<RankFinalBean> list;
    private OnRankActionListener onRankActionListener;

    public RankItemAdapter(List<RankFinalBean> list) {
        this.list = list;
    }

    public void setList(List<RankFinalBean> list) {
        this.list = list;
    }

    public void setOnRankActionListener(OnRankActionListener onRankActionListener) {
        this.onRankActionListener = onRankActionListener;
    }

    @Override
    public RankItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RankItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_rank_manage_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RankItemHolder holder, int position) {
        RankFinalBean bean = list.get(position);
        holder.tvYear.setText(String.valueOf(bean.getYear()));
        holder.tvRank.setText(String.valueOf(bean.getRank()));
        holder.ivDelete.setTag(position);
        holder.ivEdit.setTag(position);
        holder.ivDelete.setOnClickListener(this);
        holder.ivEdit.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        switch (v.getId()) {
            case R.id.iv_delete:
                onRankActionListener.onDeleteRank(position);
                break;
            case R.id.iv_edit:
                onRankActionListener.onEditRank(position);
                break;
        }
    }

    public static class RankItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_year)
        TextView tvYear;
        @BindView(R.id.iv_edit)
        ImageView ivEdit;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;
        @BindView(R.id.tv_rank)
        TextView tvRank;

        public RankItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnRankActionListener {
        void onDeleteRank(int position);
        void onEditRank(int position);
    }
}
