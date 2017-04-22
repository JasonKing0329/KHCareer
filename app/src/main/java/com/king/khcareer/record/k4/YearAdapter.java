package com.king.khcareer.record.k4;

import android.view.View;
import android.widget.TextView;

import com.king.khcareer.utils.DebugLog;
import com.king.mytennis.view.R;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractExpandableAdapterItem;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/21 16:15
 */
public class YearAdapter extends AbstractExpandableAdapterItem {

    private TextView tvYear;

    @Override
    public int getLayoutResId() {
        return R.layout.adapter_record_year;
    }

    @Override
    public void onBindViews(View root) {
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doExpandOrUnexpand();
            }
        });
        tvYear = (TextView) root.findViewById(R.id.tv_year);
    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        DebugLog.e("position=" + position);

        YearItem item = (YearItem) model;
        for (int i = 0; i < item.getChildItemList().size(); i ++) {
            item.getChildItemList().get(i).setYearPosition(position);
        }

        String year = item.getYear();
        tvYear.setText(year);
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onExpansionToggled(boolean expanded) {

    }
}
