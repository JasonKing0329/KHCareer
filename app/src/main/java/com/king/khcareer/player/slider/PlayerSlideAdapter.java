package com.king.khcareer.player.slider;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.king.khcareer.base.KApplication;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.image.glide.GlideOptions;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.mytennis.view.R;

import java.util.List;

public class PlayerSlideAdapter extends RecyclerView.Adapter<PlayerSlideAdapter.SliderCard> implements View.OnClickListener {

    private List<PlayerBean> list;
    private OnPlayerItemListener onPlayerItemListener;

    private RequestOptions starOptions;

    public PlayerSlideAdapter() {
        starOptions = GlideOptions.getDefaultPlayerOptions();
    }

    @Override
    public SliderCard onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.adapter_slide_player_item, parent, false);

        return new SliderCard(view);
    }

    @Override
    public void onBindViewHolder(SliderCard holder, int position) {

        Glide.with(KApplication.getInstance())
                .load(ImageFactory.getPlayerHeadPath(list.get(position).getNameChn()))
                .apply(starOptions)
                .into(holder.imageView);

        if (onPlayerItemListener != null) {
            holder.groupCard.setTag(position);
            holder.groupCard.setOnClickListener(this);
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }

    public void setList(List<PlayerBean> list) {
        this.list = list;
    }

    @Override
    public void onClick(View v) {
        if (onPlayerItemListener != null) {
            int position = (int) v.getTag();
            onPlayerItemListener.onClickPlayer(list.get(position), position);
        }

    }

    public void setOnPlayerItemListener(OnPlayerItemListener onPlayerItemListener) {
        this.onPlayerItemListener = onPlayerItemListener;
    }

    public static class SliderCard extends RecyclerView.ViewHolder {

        ViewGroup groupCard;
        ImageView imageView;

        public SliderCard(View itemView) {
            super(itemView);
            groupCard = (ViewGroup) itemView.findViewById(R.id.group_card);
            imageView = (ImageView) itemView.findViewById(R.id.iv_thumb);
        }
    }

    public interface OnPlayerItemListener {
        void onClickPlayer(PlayerBean bean, int position);
    }
}
