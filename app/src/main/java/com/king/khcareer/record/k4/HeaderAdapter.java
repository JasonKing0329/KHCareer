package com.king.khcareer.record.k4;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.king.khcareer.base.KApplication;
import com.king.khcareer.common.config.Constants;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.image.glide.GlideOptions;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.utils.DebugLog;
import com.king.mytennis.view.R;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractExpandableAdapterItem;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/21 16:15
 */
public class HeaderAdapter extends AbstractExpandableAdapterItem {

//    private TextView tvMatchFirst;
    private ImageView ivMatch;
    private TextView tvMatchName;
    private TextView tvMatchLevel;
    private TextView tvMatchDate;
    private TextView tvMatchRound;
    private ImageView ivWinnerCup;

    @Override
    public int getLayoutResId() {
        return R.layout.adapter_record_group_card;
    }

    @Override
    public void onBindViews(View root) {
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doExpandOrUnexpand();
            }
        });
//        tvMatchFirst = (TextView) root.findViewById(R.id.tv_match_first);
        tvMatchName = (TextView) root.findViewById(R.id.tv_match_name);
        tvMatchLevel = (TextView) root.findViewById(R.id.tv_match_level);
        tvMatchDate = (TextView) root.findViewById(R.id.tv_match_date);
        tvMatchRound = (TextView) root.findViewById(R.id.tv_match_round);
        ivWinnerCup = (ImageView) root.findViewById(R.id.iv_winner_cup);
        ivMatch = (ImageView) root.findViewById(R.id.iv_match);
    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        DebugLog.e("position=" + position);
        HeaderItem item = (HeaderItem) model;
        for (int i = 0; i < item.getChildItemList().size(); i ++) {
            item.getChildItemList().get(i).setYearPosition(item.getYearPosition());
            item.getChildItemList().get(i).setHeaderPosition(position);
        }

        Record record = item.getRecord();
        tvMatchName.setText(record.getMatch());
        // champion
        if (Constants.RECORD_MATCH_ROUNDS[0].equals(record.getRound())
                && MultiUserManager.USER_DB_FLAG.equals(record.getWinner())) {
            ivWinnerCup.setVisibility(View.VISIBLE);
            tvMatchRound.setVisibility(View.GONE);
        }
        else {
            ivWinnerCup.setVisibility(View.GONE);
            tvMatchRound.setVisibility(View.VISIBLE);
            tvMatchRound.setText(record.getRound());
        }
        tvMatchDate.setText(record.getStrDate());
        tvMatchLevel.setText(record.getLevel());
        String court = record.getCourt();
//        tvMatchFirst.setText(String.valueOf(record.getMatch().charAt(0)));
//        if (Constants.RECORD_MATCH_COURTS[1].equals(court)) {
//            tvMatchFirst.setBackgroundResource(R.drawable.shape_oval_clay);
//        }
//        else if (Constants.RECORD_MATCH_COURTS[2].equals(court)) {
//            tvMatchFirst.setBackgroundResource(R.drawable.shape_oval_grass);
//        }
//        else if (Constants.RECORD_MATCH_COURTS[3].equals(court)) {
//            tvMatchFirst.setBackgroundResource(R.drawable.shape_oval_inhard);
//        }
//        else {
//            tvMatchFirst.setBackgroundResource(R.drawable.shape_oval_hard);
//        }

        Glide.with(KApplication.getInstance())
                .load(ImageFactory.getMatchHeadPath(record.getMatch(), court))
                .apply(GlideOptions.getDefaultMatchOptions())
                .into(ivMatch);
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onExpansionToggled(boolean expanded) {

    }
}
