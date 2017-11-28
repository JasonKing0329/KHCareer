package com.king.khcareer.player.page;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.siyamed.shapeimageview.BubbleImageView;
import com.king.khcareer.base.KApplication;
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
public class PageRecordAdapter extends RecyclerView.Adapter implements View.OnClickListener, View.OnLongClickListener {

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

    public void setList(List<Object> list) {
        this.list = list;
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
            return new TitleHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_player_page_record_title, parent, false));
        }
        return new RecordHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_player_page_record_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TitleHolder) {
            onBindTitle((TitleHolder) holder, (PageTitleBean) list.get(position), position);
        } else {
            onBindRecord((RecordHolder) holder, (Record) list.get(position));
        }
    }

    private void onBindTitle(TitleHolder holder, PageTitleBean pageTitleBean, int position) {
        holder.tvTitle.setText(pageTitleBean.getYear() + " （" + pageTitleBean.getWin() + "胜" + pageTitleBean.getLose() + "负）");
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.vPoint.getLayoutParams();
        if (position == 0) {
            params.addRule(RelativeLayout.CENTER_VERTICAL);
        }
        else {
            params.removeRule(RelativeLayout.CENTER_VERTICAL);
        }
        holder.vPoint.setLayoutParams(params);
    }

    private void onBindRecord(RecordHolder holder, Record record) {
        holder.tvLine1.setText(record.getLevel() + "  " + record.getStrDate().split("-")[1] + "月  " + record.getRound());
        holder.tvLine2.setText(record.getMatch() + "  " + record.getCourt());
        String winner = record.getWinner();
        if (winner.equals(MultiUserManager.USER_DB_FLAG)) {
            winner = user.getDisplayName();
            holder.tvLine3.setText(winner + "  def.  " + record.getCptSeed() + "/" + record.getCptRank());
            holder.tvLine3.setTextColor(holder.tvLine3.getResources().getColor(R.color.record_item_text_gray));
        }
        else {
            holder.tvLine3.setText(winner + " " + record.getCptSeed() + "/" + record.getCptRank() + "  def.");
            holder.tvLine3.setTextColor(holder.tvLine3.getResources().getColor(R.color.red));
        }
        holder.tvLine4.setText(record.getScore());

        String filePath;
        if (imageIndexMap.get(record.getMatch()) == null) {
            filePath = ImageFactory.getMatchHeadPath(record.getMatch(), record.getCourt(), imageIndexMap);
        }
        else {
            filePath = ImageFactory.getMatchHeadPath(record.getMatch(), record.getCourt(), imageIndexMap.get(record.getMatch()));
        }
        Glide.with(KApplication.getInstance())
                .load(filePath)
                .apply(requestOptions)
                .into(holder.ivMatch);

        holder.groupCard.setTag(record);
        holder.groupCard.setOnClickListener(this);
        holder.groupCard.setOnLongClickListener(this);
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

    @Override
    public boolean onLongClick(View v) {
        if (onItemClickListener != null) {
            onItemClickListener.onLongClickRecord(v, (Record) v.getTag());
        }
        return true;
    }

    public static class TitleHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.v_point)
        View vPoint;

        public TitleHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class RecordHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.group_card)
        ViewGroup groupCard;
        @BindView(R.id.iv_match)
        BubbleImageView ivMatch;
        @BindView(R.id.tv_line1)
        TextView tvLine1;
        @BindView(R.id.tv_line2)
        TextView tvLine2;
        @BindView(R.id.tv_line3)
        TextView tvLine3;
        @BindView(R.id.tv_line4)
        TextView tvLine4;

        public RecordHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onClickRecord(Record record);
        void onLongClickRecord(View view, Record record);
    }
}
