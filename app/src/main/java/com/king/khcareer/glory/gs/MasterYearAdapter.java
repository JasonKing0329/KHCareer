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
public class MasterYearAdapter extends RecyclerView.Adapter<MasterYearAdapter.ItemHolder> implements View.OnClickListener {

    private List<GloryMasterItem> itemList;
    private OnRecordItemListener onRecordItemListener;

    public MasterYearAdapter(List<GloryMasterItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_glory_atp1000_year_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        GloryMasterItem item = itemList.get(position);
        holder.tvYear.setText(String.valueOf(item.getYear()));
        holder.tvIw.setText(TextUtils.isEmpty(item.getIw()) ? "--" : item.getIw());
        holder.tvMm.setText(TextUtils.isEmpty(item.getMiami()) ? "--" : item.getMiami());
        holder.tvMc.setText(TextUtils.isEmpty(item.getMc()) ? "--" : item.getMc());
        holder.tvMd.setText(TextUtils.isEmpty(item.getMadrid()) ? "--" : item.getMadrid());
        holder.tvRo.setText(TextUtils.isEmpty(item.getRoma()) ? "--" : item.getRoma());
        holder.tvRc.setText(TextUtils.isEmpty(item.getRc()) ? "--" : item.getRc());
        holder.tvCc.setText(TextUtils.isEmpty(item.getCicinati()) ? "--" : item.getCicinati());
        holder.tvSh.setText(TextUtils.isEmpty(item.getSh()) ? "--" : item.getSh());
        holder.tvPa.setText(TextUtils.isEmpty(item.getParis()) ? "--" : item.getParis());

        updateTextAppearence(holder.tvIw, item.getIw());
        updateTextAppearence(holder.tvMm, item.getMiami());
        updateTextAppearence(holder.tvMc, item.getMc());
        updateTextAppearence(holder.tvMd, item.getMadrid());
        updateTextAppearence(holder.tvRo, item.getRoma());
        updateTextAppearence(holder.tvRc, item.getRc());
        updateTextAppearence(holder.tvCc, item.getCicinati());
        updateTextAppearence(holder.tvSh, item.getSh());
        updateTextAppearence(holder.tvPa, item.getParis());

        holder.tvIw.setTag(item.getRecordIW());
        holder.tvIw.setOnClickListener(this);
        holder.tvMm.setTag(item.getRecordMiami());
        holder.tvMm.setOnClickListener(this);
        holder.tvMc.setTag(item.getRecordMC());
        holder.tvMc.setOnClickListener(this);
        holder.tvMd.setTag(item.getRecordMadrid());
        holder.tvMd.setOnClickListener(this);
        holder.tvRo.setTag(item.getRecordRoma());
        holder.tvRo.setOnClickListener(this);
        holder.tvRc.setTag(item.getRecordRC());
        holder.tvRc.setOnClickListener(this);
        holder.tvCc.setTag(item.getRecordCicinati());
        holder.tvCc.setOnClickListener(this);
        holder.tvSh.setTag(item.getRecordSH());
        holder.tvSh.setOnClickListener(this);
        holder.tvPa.setTag(item.getRecordParis());
        holder.tvPa.setOnClickListener(this);
    }

    private void updateTextAppearence(TextView textView, String result) {
        if ("W".equals(result)) {
            textView.setTextAppearance(textView.getContext(), R.style.TvMatchResultItemWinner);
        }
        else {
            textView.setTextAppearance(textView.getContext(), R.style.TvMatchResultItemNormal);
        }
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
        @BindView(R.id.tv_iw)
        TextView tvIw;
        @BindView(R.id.tv_mm)
        TextView tvMm;
        @BindView(R.id.tv_mc)
        TextView tvMc;
        @BindView(R.id.tv_md)
        TextView tvMd;
        @BindView(R.id.tv_ro)
        TextView tvRo;
        @BindView(R.id.tv_rc)
        TextView tvRc;
        @BindView(R.id.tv_cc)
        TextView tvCc;
        @BindView(R.id.tv_sh)
        TextView tvSh;
        @BindView(R.id.tv_pa)
        TextView tvPa;

        public ItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
