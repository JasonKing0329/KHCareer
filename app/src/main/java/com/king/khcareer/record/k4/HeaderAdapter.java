package com.king.khcareer.record.k4;

import android.view.View;
import android.widget.TextView;

import com.king.khcareer.model.sql.player.bean.Record;
import com.king.mytennis.view.R;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractAdapterItem;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/21 16:15
 */
public class HeaderAdapter extends AbstractAdapterItem {

    private TextView tvMatchFirst;
    private TextView tvMatchName;
    private TextView tvMatchLevel;
    private TextView tvMatchDate;
    private TextView tvMatchRound;

    @Override
    public int getLayoutResId() {
        return R.layout.adapter_record_group;
    }

    @Override
    public void onBindViews(View root) {
        tvMatchFirst = (TextView) root.findViewById(R.id.tv_match_first);
        tvMatchName = (TextView) root.findViewById(R.id.tv_match_name);
        tvMatchLevel = (TextView) root.findViewById(R.id.tv_match_level);
        tvMatchDate = (TextView) root.findViewById(R.id.tv_match_date);
        tvMatchRound = (TextView) root.findViewById(R.id.tv_match_round);
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(Object model, int position) {

        Record record = ((HeaderItem) model).getRecord();
        tvMatchFirst.setText(String.valueOf(record.getMatchCountry().charAt(0)));
        tvMatchName.setText(record.getMatchCountry().charAt(0));
        tvMatchRound.setText(record.getRound());
        tvMatchDate.setText(record.getStrDate());
        tvMatchLevel.setText(record.getLevel());
    }
}
