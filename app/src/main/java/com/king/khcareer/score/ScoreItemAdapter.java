package com.king.khcareer.score;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.mytennis.view.R;

import java.util.List;

/**
 * TODO 描述: v2.4.4 score界面的详细赛事积分记录设计为可点击item，预备adapter，在数据库模型完成后继续完成该部分
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/23 15:22
 */
public class ScoreItemAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private final int TITLE = 0;
    private final int ITEM = 1;

    private List<ScoreBean> list;

    private OnScoreItemClickListener onScoreItemClickListener;

    public ScoreItemAdapter(List<ScoreBean> list) {
        this.list = list;
    }

    public void setList(List<ScoreBean> list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).isTitle() ? TITLE:ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TITLE) {
            return new TitleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_score_title, parent, false));
        }
        else {
            return new ScoreItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_score_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vHolder, int position) {
        ScoreBean bean = list.get(position);
        if (vHolder instanceof TitleHolder) {
            TitleHolder holder = (TitleHolder) vHolder;
            holder.tvTitle.setText(bean.getTitle());
        }
        else {
            ScoreItemHolder holder = (ScoreItemHolder) vHolder;
            if (bean.getMatchBean() == null) {// 500 赛罚分
                holder.cup.setVisibility(View.INVISIBLE);
                holder.name.setText("500赛罚分");
                holder.score.setText("0");
                holder.complete.setVisibility(View.INVISIBLE);
                holder.group.setOnClickListener(null);
            }
            else {
                holder.cup.setVisibility(bean.isChampion() ? View.VISIBLE:View.INVISIBLE);
                holder.name.setText(bean.getMatchBean().getName());
                holder.score.setText(String.valueOf(bean.getScore()));
                holder.complete.setVisibility(bean.isCompleted() ? View.VISIBLE:View.INVISIBLE);
                holder.group.setTag(position);
                holder.group.setOnClickListener(this);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        if (onScoreItemClickListener != null) {
            int positin = (int) v.getTag();
            if (!list.get(positin).isTitle()) {
                onScoreItemClickListener.onScoreItemClick(list.get(positin));
            }
        }
    }

    public void setOnScoreItemClickListener(OnScoreItemClickListener onScoreItemClickListener) {
        this.onScoreItemClickListener = onScoreItemClickListener;
    }

    public interface OnScoreItemClickListener {
        void onScoreItemClick(ScoreBean bean);
    }

    public static class TitleHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;

        public TitleHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

    public static class ScoreItemHolder extends RecyclerView.ViewHolder {

        ViewGroup group;
        TextView name;
        TextView score;
        TextView complete;
        ImageView cup;
        public ScoreItemHolder(View itemView) {
            super(itemView);
            group = (ViewGroup) itemView.findViewById(R.id.score_item_group);
            name = (TextView) itemView.findViewById(R.id.score_item_name);
            score = (TextView) itemView.findViewById(R.id.score_item_score);
            complete = (TextView) itemView.findViewById(R.id.score_item_complete);
            cup = (ImageView) itemView.findViewById(R.id.score_item_winner);
        }
    }
}
