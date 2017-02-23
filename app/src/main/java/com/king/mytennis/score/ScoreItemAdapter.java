package com.king.mytennis.score;

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
public class ScoreItemAdapter extends RecyclerView.Adapter<ScoreItemAdapter.ScoreItemHolder> {

    private List<ScoreBean> list;

    public ScoreItemAdapter(List<ScoreBean> list) {
        this.list = list;
    }

    public void setList(List<ScoreBean> list) {
        this.list = list;
    }

    @Override
    public ScoreItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScoreItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_score_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ScoreItemHolder holder, int position) {
        holder.name.setText(list.get(position).getName());
        holder.score.setText(list.get(position).getScore());
//        holder.complete.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
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
