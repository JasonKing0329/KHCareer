package com.king.khcareer.match.page;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.king.khcareer.base.KApplication;
import com.king.khcareer.common.config.Constants;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.image.glide.GlideOptions;
import com.king.khcareer.common.multiuser.MultiUser;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.mytennis.view.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述: title + items
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/21 9:34
 */
public class PageRecordAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private final int TYPE_TITLE = 1;
    private final int TYPE_RECORD = 0;

    private final MultiUser user;

    private List<Object> list;

    private RequestOptions requestOptions;

    private OnItemClickListener onItemClickListener;

    /**
     * 保存首次从文件夹加载的图片序号
     */
    private Map<String, Integer> imageIndexMap;

    public PageRecordAdapter(MultiUser user, List<Object> list) {
        this.user = user;
        this.list = list;
        requestOptions = GlideOptions.getDefaultMatchOptions();
        imageIndexMap = new HashMap<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) instanceof PageTitleBean) {
            return TYPE_TITLE;
        }
        return TYPE_RECORD;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TITLE) {
            return new TitleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_match_page_group_title, parent, false));
        }
        return new RecordHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_match_page_record_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleHolder) {
            onBindTitle((TitleHolder) holder, (PageTitleBean) list.get(position));
        } else {
            onBindRecord((RecordHolder) holder, (Record) list.get(position));
        }
    }

    private void onBindTitle(TitleHolder holder, PageTitleBean pageTitleBean) {
        holder.tvTitle.setText(String.valueOf(pageTitleBean.getYear()));
        holder.ivCup.setVisibility(pageTitleBean.isWinner() ? View.VISIBLE:View.GONE);
    }

    private void onBindRecord(RecordHolder holder, Record record) {
        holder.tvLevel.setText(Constants.getMasterGloryForRound(record.getRound()));
        holder.tvLine2.setText(record.getCompetitor() + " " + record.getCptSeed() + "/" + record.getCptRank());
        String winner = record.getWinner();
        GradientDrawable drawable = (GradientDrawable) holder.tvLevel.getBackground();
        if (winner.equals(MultiUserManager.USER_DB_FLAG)) {
            winner = user.getDisplayName();
            if (record.getRound().equals(Constants.RECORD_MATCH_ROUNDS[0])) {
                drawable.setColor(holder.tvLevel.getResources().getColor(R.color.round_tag_winner));
            }
            else {
                drawable.setColor(holder.tvLevel.getResources().getColor(R.color.round_tag_normal));
            }
        }
        else {
            drawable.setColor(holder.tvLevel.getResources().getColor(R.color.round_tag_normal));
        }
        holder.tvLevel.setBackground(drawable);
        holder.tvLine3.setText(winner + " " + record.getScore());

        String filePath;
        if (imageIndexMap.get(record.getCompetitor()) == null) {
            filePath = ImageFactory.getPlayerHeadPath(record.getCompetitor(), imageIndexMap);
        }
        else {
            filePath = ImageFactory.getPlayerHeadPath(record.getCompetitor(), imageIndexMap.get(record.getCompetitor()));
        }
        Glide.with(KApplication.getInstance())
                .load(filePath)
                .apply(requestOptions)
                .into(holder.ivPlayer);

        holder.groupCard.setOnClickListener(this);
        holder.groupCard.setTag(record);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onClickRecord((Record) v.getTag());
        }
    }

    public static class TitleHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.iv_cup)
        ImageView ivCup;

        public TitleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class RecordHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.group_card)
        ViewGroup groupCard;
        @BindView(R.id.iv_player)
        ImageView ivPlayer;
        @BindView(R.id.tv_level)
        TextView tvLevel;
        @BindView(R.id.tv_line2)
        TextView tvLine2;
        @BindView(R.id.tv_line3)
        TextView tvLine3;

        public RecordHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onClickRecord(Record record);
    }
}
