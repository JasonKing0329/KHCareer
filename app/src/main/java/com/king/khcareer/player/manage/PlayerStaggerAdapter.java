package com.king.khcareer.player.manage;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.mytennis.view.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/8/17 14:54
 */
public class PlayerStaggerAdapter extends PlayerManageBaseAdapter {

    private int spanWidth;

    public PlayerStaggerAdapter(Context context, List<PlayerBean> list, int imgWidth) {
        super(context, list);
        this.spanWidth = imgWidth;
    }

    @Override
    public PlayerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlayerHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_player_stagger, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        PlayerStaggerAdapter.PlayerHolder holder = (PlayerHolder) h;
        PlayerBean bean = list.get(position);
        String path = getPlayerPath(position);
        // 瀑布流item高度
        ViewGroup.LayoutParams params = holder.ivPlayer.getLayoutParams();
        if (path == null) {
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            holder.ivPlayer.setLayoutParams(params);
        } else {
            params.height = getTargetHeight(path);
            holder.ivPlayer.setLayoutParams(params);
        }
        // image view相关显示及事件
        updateItemImage(path, position, holder.ivPlayer);

        holder.tvName.setText(bean.getNameChn());
        holder.tvNameEng.setText(bean.getNameEng());
        // 当前排序是按星座排序，显示星座名称
        if (isSortByConstellation()) {
            holder.tvBirthday.setText(getConstellation(position));
        }
        else {
            holder.tvBirthday.setText(bean.getBirthday());
        }
        holder.tvCountry.setText("(" + bean.getCountry() + ")");

        holder.groupCard.setTag(position);
        holder.groupCard.setOnClickListener(this);

        // item 背景
        updateItemBackground(position, holder.groupCard);

        // check 状态
        updateCheckStatus(position, holder.cbCheck);
    }

    private int getTargetHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        float rate = (float) spanWidth / (float) options.outWidth;
        return (int) (options.outHeight * rate);
    }

    public static class PlayerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_player)
        ImageView ivPlayer;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_country)
        TextView tvCountry;
        @BindView(R.id.group_name)
        LinearLayout groupName;
        @BindView(R.id.tv_name_eng)
        TextView tvNameEng;
        @BindView(R.id.tv_birthday)
        TextView tvBirthday;
        @BindView(R.id.group_card)
        CardView groupCard;
        @BindView(R.id.cb_check)
        CheckBox cbCheck;

        public PlayerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
