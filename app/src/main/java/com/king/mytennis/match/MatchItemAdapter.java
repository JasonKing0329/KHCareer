package com.king.mytennis.match;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.pubdata.bean.MatchNameBean;
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
public class MatchItemAdapter extends RecyclerView.Adapter<MatchItemAdapter.ItemHolder> implements View.OnClickListener {

    private List<MatchNameBean> list;
    private boolean selectMode;
    private SparseBooleanArray mCheckMap;
    private OnMatchItemClickListener onMatchItemClickListener;

    public MatchItemAdapter(List<MatchNameBean> list) {
        this.list = list;
        mCheckMap = new SparseBooleanArray();
    }

    public void setSelectMode(boolean selectMode) {
        this.selectMode = selectMode;
        if (!selectMode) {
            mCheckMap.clear();
        }
    }

    public void setOnMatchItemClickListener(OnMatchItemClickListener onMatchItemClickListener) {
        this.onMatchItemClickListener = onMatchItemClickListener;
    }

    public void setList(List<MatchNameBean> list) {
        this.list = list;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_match_manage_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        MatchNameBean bean = list.get(position);
        holder.tvIndex.setText(String.valueOf(position + 1));
        holder.tvName.setText(bean.getName());
        holder.tvInfor.setText(bean.getMatchBean().getLevel() + "/" + bean.getMatchBean().getCourt()
                + " W" + String.valueOf(bean.getMatchBean().getWeek()));
        holder.tvCountry.setText(bean.getMatchBean().getCountry());
        holder.tvCity.setText(bean.getMatchBean().getCity());
        holder.check.setVisibility(selectMode ? View.VISIBLE:View.GONE);
        holder.check.setChecked(mCheckMap.get(position));

        holder.group.setTag(position);
        holder.group.setOnClickListener(this);

        String filePath = "file://" + ImageFactory.getMatchHeadPath(bean.getName(), bean.getMatchBean().getCourt());
        ImageUtil.load(filePath, holder.image);
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
            if (onMatchItemClickListener != null) {
                onMatchItemClickListener.onMatchItemClick(position);
            }
        }
    }

    public List<MatchNameBean> getSelectedList() {
        List<MatchNameBean> dlist = new ArrayList<>();
        for (int i = 0; i < list.size(); i ++) {
            if (mCheckMap.get(i)) {
                dlist.add(list.get(i));
            }
        }
        return dlist;
    }

    public interface OnMatchItemClickListener {
        void onMatchItemClick(int position);
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        ViewGroup group;
        TextView tvIndex;
        TextView tvName;
        TextView tvCountry;
        TextView tvCity;
        TextView tvInfor;
        CheckBox check;
        ImageView image;
        public ItemHolder(View itemView) {
            super(itemView);
            group = (ViewGroup) itemView.findViewById(R.id.manage_item_group);
            tvIndex = (TextView) itemView.findViewById(R.id.manage_item_index);
            tvName = (TextView) itemView.findViewById(R.id.manage_item_name);
            tvCountry = (TextView) itemView.findViewById(R.id.manage_item_country);
            tvCity = (TextView) itemView.findViewById(R.id.manage_item_city);
            tvInfor = (TextView) itemView.findViewById(R.id.manage_item_infor);
            check = (CheckBox) itemView.findViewById(R.id.manage_item_check);
            image = (ImageView) itemView.findViewById(R.id.manage_item_image);
        }
    }
}
