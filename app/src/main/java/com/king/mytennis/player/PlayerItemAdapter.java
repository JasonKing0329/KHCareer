package com.king.mytennis.player;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.pubdata.bean.PlayerBean;
import com.king.mytennis.service.ImageUtil;
import com.king.mytennis.view.R;
import com.king.mytennis.view_v_7_0.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/20 13:27
 */
public class PlayerItemAdapter extends RecyclerView.Adapter<PlayerItemAdapter.ItemHolder> implements View.OnClickListener {

    private List<PlayerBean> list;
    private boolean selectMode;
    private SparseBooleanArray mCheckMap;
    private OnPlayerItemClickListener onPlayerItemClickListener;

    public PlayerItemAdapter(List<PlayerBean> list) {
        this.list = list;
        mCheckMap = new SparseBooleanArray();
    }

    public void setSelectMode(boolean selectMode) {
        this.selectMode = selectMode;
        if (!selectMode) {
            mCheckMap.clear();
        }
    }

    public void setOnPlayerItemClickListener(OnPlayerItemClickListener onPlayerItemClickListener) {
        this.onPlayerItemClickListener = onPlayerItemClickListener;
    }

    public void setList(List<PlayerBean> list) {
        this.list = list;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_player_manage_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        PlayerBean bean = list.get(position);
        holder.tvIndex.setText(String.valueOf(position + 1));
        holder.tvName.setText(bean.getNameChn());
        holder.tvNameEng.setText(bean.getNameEng());
        holder.tvBirthday.setText(bean.getBirthday());
        holder.tvCountry.setText(bean.getCountry());
        if (selectMode) {
            if (position < PlayerManageActivity.FIXED_PLAYER) {// 不允许删除
                holder.check.setVisibility(View.INVISIBLE);
            }
            else {
                holder.check.setVisibility(View.VISIBLE);
            }
        }
        else {
            holder.check.setVisibility(View.GONE);
        }
        holder.check.setChecked(mCheckMap.get(position));

        holder.group.setTag(position);
        holder.group.setOnClickListener(this);

        String filePath = "file://" + ImageFactory.getPlayerHeadPath(list.get(position).getNameChn());
        ImageUtil.load(filePath, holder.image, R.drawable.icon_list);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0:list.size();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (selectMode) {
            mCheckMap.put(position, !mCheckMap.get(position));
            notifyDataSetChanged();
        }
        else {
            if (onPlayerItemClickListener != null) {
                onPlayerItemClickListener.onPlayerItemClick(position);
            }
        }
    }

    public List<PlayerBean> getSelectedList() {
        List<PlayerBean> dlist = new ArrayList<>();
        for (int i = 0; i < list.size(); i ++) {
            if (mCheckMap.get(i)) {
                dlist.add(list.get(i));
            }
        }
        return dlist;
    }

    public interface OnPlayerItemClickListener {
        void onPlayerItemClick(int position);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        ViewGroup group;
        TextView tvIndex;
        TextView tvName;
        TextView tvNameEng;
        TextView tvCountry;
        TextView tvBirthday;
        CheckBox check;
        CircleImageView image;
        public ItemHolder(View itemView) {
            super(itemView);
            group = (ViewGroup) itemView.findViewById(R.id.manage_item_group);
            tvIndex = (TextView) itemView.findViewById(R.id.manage_item_index);
            tvName = (TextView) itemView.findViewById(R.id.manage_item_name);
            tvCountry = (TextView) itemView.findViewById(R.id.manage_item_country);
            tvNameEng = (TextView) itemView.findViewById(R.id.manage_item_name_eng);
            tvBirthday = (TextView) itemView.findViewById(R.id.manage_item_birthday);
            check = (CheckBox) itemView.findViewById(R.id.manage_item_check);
            image = (CircleImageView) itemView.findViewById(R.id.manage_item_image);
        }
    }
}
