package com.king.khcareer.glory.title;

import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.View;
import android.widget.TextView;

import com.king.khcareer.common.config.Constants;
import com.king.mytennis.view.R;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractExpandableAdapterItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/21 10:37
 */
public class HeaderAdapter extends AbstractExpandableAdapterItem {

    @BindView(R.id.tv_key)
    TextView tvKey;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_tag)
    TextView tvTag;

    @Override
    public int getLayoutResId() {
        return R.layout.adapter_glory_list_group;
    }

    @Override
    public void onBindViews(View root) {
        ButterKnife.bind(this, root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doExpandOrUnexpand();
            }
        });
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        HeaderItem item = (HeaderItem) model;
        String key = item.getHeaderBean().getKey();
        tvKey.setText(key);
        tvContent.setText(item.getHeaderBean().getContent());

        GradientDrawable drawable = (GradientDrawable) tvTag.getBackground();
        if (Constants.RECORD_MATCH_COURTS[0].equals(key)) {
            tvTag.setText(key.substring(0, 1).toUpperCase());
            drawable.setColor(tvTag.getContext().getResources().getColor(R.color.normal_court_hard));
            tvKey.setTextColor(tvTag.getContext().getResources().getColor(R.color.normal_court_hard));
        }
        else if (Constants.RECORD_MATCH_COURTS[1].equals(key)) {
            tvTag.setText(key.substring(0, 1).toUpperCase());
            drawable.setColor(tvTag.getContext().getResources().getColor(R.color.normal_court_clay));
            tvKey.setTextColor(tvTag.getContext().getResources().getColor(R.color.normal_court_clay));
        }
        else if (Constants.RECORD_MATCH_COURTS[2].equals(key)) {
            tvTag.setText(key.substring(0, 1).toUpperCase());
            drawable.setColor(tvTag.getContext().getResources().getColor(R.color.normal_court_grass));
            tvKey.setTextColor(tvTag.getContext().getResources().getColor(R.color.normal_court_grass));
        }
        else if (Constants.RECORD_MATCH_COURTS[3].equals(key)) {
            tvTag.setText(key.substring(0, 1).toUpperCase());
            drawable.setColor(tvTag.getContext().getResources().getColor(R.color.normal_court_inhard));
            tvKey.setTextColor(tvTag.getContext().getResources().getColor(R.color.normal_court_inhard));
        }
        else if (Constants.RECORD_MATCH_LEVELS[0].equals(key)) {
            tvTag.setText("GS");
            drawable.setColor(tvTag.getContext().getResources().getColor(R.color.normal_level_gs));
            tvKey.setTextColor(tvTag.getContext().getResources().getColor(R.color.normal_level_gs));
        }
        else if (Constants.RECORD_MATCH_LEVELS[1].equals(key)) {
            tvTag.setText("MC");
            drawable.setColor(tvTag.getContext().getResources().getColor(R.color.normal_level_mc));
            tvKey.setTextColor(tvTag.getContext().getResources().getColor(R.color.normal_level_mc));
        }
        else if (Constants.RECORD_MATCH_LEVELS[2].equals(key)) {
            tvTag.setText("1000");
            drawable.setColor(tvTag.getContext().getResources().getColor(R.color.normal_level_1000));
            tvKey.setTextColor(tvTag.getContext().getResources().getColor(R.color.normal_level_1000));
        }
        else if (Constants.RECORD_MATCH_LEVELS[3].equals(key)) {
            tvTag.setText("500");
            drawable.setColor(tvTag.getContext().getResources().getColor(R.color.normal_level_500));
            tvKey.setTextColor(tvTag.getContext().getResources().getColor(R.color.normal_level_500));
        }
        else if (Constants.RECORD_MATCH_LEVELS[4].equals(key)) {
            tvTag.setText("250");
            drawable.setColor(tvTag.getContext().getResources().getColor(R.color.normal_level_250));
            tvKey.setTextColor(tvTag.getContext().getResources().getColor(R.color.normal_level_250));
        }
        else if (Constants.RECORD_MATCH_LEVELS[6].equals(key)) {
            tvTag.setText("OG");
            drawable.setColor(tvTag.getContext().getResources().getColor(R.color.normal_level_oly));
            tvKey.setTextColor(tvTag.getContext().getResources().getColor(R.color.normal_level_oly));
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {

    }
}
