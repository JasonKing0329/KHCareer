package com.king.khcareer.record.k4;

import android.view.View;
import android.widget.TextView;

import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.pubview.CircleImageView;
import com.king.mytennis.view.R;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractAdapterItem;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/21 16:15
 */
public class RecordItemAdapter extends AbstractAdapterItem {

    private CircleImageView ivPlayer;
    private TextView tvPlayer;
    private TextView tvRankSeed;
    private TextView tvRound;
    private TextView tvScore;

    @Override
    public int getLayoutResId() {
        return R.layout.adapter_record_sub_item;
    }

    @Override
    public void onBindViews(View root) {
        ivPlayer = (CircleImageView) root.findViewById(R.id.iv_player);
        tvPlayer = (TextView) root.findViewById(R.id.tv_player);
        tvRankSeed = (TextView) root.findViewById(R.id.tv_rank_seed);
        tvRound = (TextView) root.findViewById(R.id.tv_round);
        tvScore = (TextView) root.findViewById(R.id.tv_score);
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(Object model, int position) {

        Record record = ((RecordItem) model).getRecord();
        tvPlayer.setText(record.getCompetitor());
        tvRankSeed.setText("(".concat(String.valueOf(record.getCptRank())).concat("/")
            .concat(String.valueOf(record.getCptSeed()).concat(")")));
        tvRound.setText(record.getLevel());
        tvScore.setText(record.getScore());
    }
}
