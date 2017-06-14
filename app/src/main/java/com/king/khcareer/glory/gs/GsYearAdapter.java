package com.king.khcareer.glory.gs;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.king.khcareer.glory.title.OnRecordItemListener;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.mytennis.view.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/14 11:30
 */
public class GsYearAdapter extends RecyclerView.Adapter<GsYearAdapter.ItemHolder> implements View.OnClickListener {

    private List<GloryGsItem> itemList;
    private OnRecordItemListener onRecordItemListener;

    public GsYearAdapter(List<GloryGsItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_glory_gs_year_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        GloryGsItem item = itemList.get(position);
        holder.tvYear.setText(String.valueOf(item.getYear()));
        holder.tvAo.setText(TextUtils.isEmpty(item.getAo()) ? "--":item.getAo());
        holder.tvFo.setText(TextUtils.isEmpty(item.getFo()) ? "--":item.getFo());
        holder.tvWo.setText(TextUtils.isEmpty(item.getWo()) ? "--":item.getWo());
        holder.tvUo.setText(TextUtils.isEmpty(item.getUo()) ? "--":item.getUo());

        holder.tvAo.setTag(item.getRecordAo());
        holder.tvAo.setOnClickListener(this);
        holder.tvFo.setTag(item.getRecordFo());
        holder.tvFo.setOnClickListener(this);
        holder.tvWo.setTag(item.getRecordWo());
        holder.tvWo.setOnClickListener(this);
        holder.tvUo.setTag(item.getRecordUo());
        holder.tvUo.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    @Override
    public void onClick(View v) {
        if (onRecordItemListener != null) {
            if (v.getTag() != null) {
                Record record = (Record) v.getTag();
                onRecordItemListener.onClickRecord(record);
            }
        }
    }

    public void setOnRecordItemListener(OnRecordItemListener onRecordItemListener) {
        this.onRecordItemListener = onRecordItemListener;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_year)
        TextView tvYear;
        @BindView(R.id.tv_ao)
        TextView tvAo;
        @BindView(R.id.tv_fo)
        TextView tvFo;
        @BindView(R.id.tv_wo)
        TextView tvWo;
        @BindView(R.id.tv_uo)
        TextView tvUo;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
