package com.king.khcareer.glory.title;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.image.ImageUtil;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.mytennis.view.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述: 线性展开列表，只有一级
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/13 9:22
 */
public class SeqListAdapter extends RecyclerView.Adapter<SeqListAdapter.ItemHolder> implements View.OnClickListener {

    private List<Record> recordList;
    private OnRecordItemListener onRecordItemListener;
    private boolean showCompetitor;
    private boolean showTitle;
    private boolean hideSequence;
    private boolean showLose;
    private List<String> titleList;

    public SeqListAdapter(List<Record> recordList) {
        this.recordList = recordList;
    }

    @Override
    public SeqListAdapter.ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_glory_list_item, parent, false));
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }

    public void setShowCompetitor(boolean showCompetitor) {
        this.showCompetitor = showCompetitor;
    }

    public void setShowTitle(boolean showTitle) {
        this.showTitle = showTitle;
    }

    public void setShowLose(boolean showLose) {
        this.showLose = showLose;
    }

    public void setHideSequence(boolean hideSequence) {
        this.hideSequence = hideSequence;
    }

    public void setTitleList(List<String> list) {
        this.titleList = list;
    }

    @Override
    public void onBindViewHolder(SeqListAdapter.ItemHolder holder, int position) {
        Record record = recordList.get(position);
        holder.tvCity.setText(record.getMatchCountry() + "/" + record.getCity());
        holder.tvLevel.setText(record.getLevel());
        holder.tvName.setText(record.getMatch());
        holder.tvYear.setText(record.getStrDate());

        // list是倒序排列的
        if (hideSequence) {
            holder.tvSeq.setVisibility(View.GONE);
        }
        else {
            holder.tvSeq.setVisibility(View.VISIBLE);
            holder.tvSeq.setText(String.valueOf(getItemCount() - position));
        }

        ImageUtil.load("file://" + ImageFactory.getMatchHeadPath(record.getMatch(), record.getCourt()), holder.ivMatch);

        holder.groupItem.setTag(position);
        holder.groupItem.setOnClickListener(this);

        if (showCompetitor) {
            holder.groupCompetitor.setVisibility(View.VISIBLE);
            holder.tvCompetitor.setText(record.getCompetitor() + "(" + record.getCptCountry() + ")");
            holder.tvScore.setText(record.getScore());
            ImageUtil.load("file://" + ImageFactory.getPlayerHeadPath(record.getCompetitor()), holder.ivCompetitor);
        }
        else {
            holder.groupCompetitor.setVisibility(View.GONE);
        }

        if (showTitle) {
            holder.tvTitle.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(titleList.get(position));
        }
        else {
            holder.tvTitle.setVisibility(View.GONE);
        }

        if (showLose && !record.getWinner().equals(MultiUserManager.USER_DB_FLAG)) {
            holder.tvLose.setVisibility(View.VISIBLE);
        }
        else {
            holder.tvLose.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return recordList == null ? 0 : recordList.size();
    }

    @Override
    public void onClick(View v) {
        if (onRecordItemListener != null) {
            int position = (int) v.getTag();
            onRecordItemListener.onClickRecord(recordList.get(position));
        }
    }

    public void setOnRecordItemListener(OnRecordItemListener onRecordItemListener) {
        this.onRecordItemListener = onRecordItemListener;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.group_item)
        ViewGroup groupItem;
        @BindView(R.id.group_competitor)
        ViewGroup groupCompetitor;
        @BindView(R.id.iv_match)
        RoundedImageView ivMatch;
        @BindView(R.id.iv_competitor)
        CircularImageView ivCompetitor;
        @BindView(R.id.tv_seq)
        TextView tvSeq;
        @BindView(R.id.tv_lose)
        TextView tvLose;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_level)
        TextView tvLevel;
        @BindView(R.id.tv_city)
        TextView tvCity;
        @BindView(R.id.tv_year)
        TextView tvYear;
        @BindView(R.id.tv_competitor)
        TextView tvCompetitor;
        @BindView(R.id.tv_score)
        TextView tvScore;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
