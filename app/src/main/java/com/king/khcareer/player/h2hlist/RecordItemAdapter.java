package com.king.khcareer.player.h2hlist;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.image.ImageUtil;
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
public class RecordItemAdapter extends AbstractExpandableAdapterItem implements View.OnClickListener {

    private ImageView ivMatch;
    private TextView ivMatchName;
    private TextView tvLevel;
    private TextView tvCourt;
    private TextView tvDate;
    private TextView tvRound;
    private TextView tvScore;
    private TextView tvWin;
    private ViewGroup groupRecord;

    private OnItemMenuListener onItemMenuListener;

    private RecordItem curRecordItem;

    private int colorWin;
    private int colorLose;
    private String[] roundArray, roundReferArray;

    public RecordItemAdapter(OnItemMenuListener onItemMenuListener) {
        this.onItemMenuListener = onItemMenuListener;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.adapter_h2hlist_sub_item;
    }

    @Override
    public void onBindViews(View root) {
        groupRecord = (ViewGroup) root.findViewById(R.id.group_record);
        ivMatch = (ImageView) root.findViewById(R.id.iv_match);
        ivMatchName = (TextView) root.findViewById(R.id.tv_match_name);
        tvDate = (TextView) root.findViewById(R.id.tv_date);
        tvRound = (TextView) root.findViewById(R.id.tv_round);
        tvScore = (TextView) root.findViewById(R.id.tv_score);
        tvLevel = (TextView) root.findViewById(R.id.tv_level);
        tvCourt = (TextView) root.findViewById(R.id.tv_court);
        tvWin = (TextView) root.findViewById(R.id.tv_win);

        colorWin = root.getContext().getResources().getColor(R.color.h2hlist_color_win);
        colorLose = root.getContext().getResources().getColor(R.color.h2hlist_color_lose);
        roundArray = Constants.RECORD_MATCH_ROUNDS;
        roundReferArray = Constants.RECORD_MATCH_ROUNDS_SHORT;
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        DebugLog.e("position=" + position);
        curRecordItem = (RecordItem) model;
        curRecordItem.setItemPosition(position);
        Record curRecord = curRecordItem.getRecord();
        ivMatchName.setText(curRecord.getMatch());
        tvCourt.setText(curRecord.getCourt());

        // show short text for round
        for (int i = 0; i < roundArray.length; i ++) {
            if (curRecord.getRound().equals(roundArray[i])) {
                tvRound.setText(roundReferArray[i]);
                break;
            }
        }

        tvScore.setText(curRecord.getScore());
        tvLevel.setText(curRecord.getLevel());
        tvDate.setText(curRecord.getStrDate());
        if (MultiUserManager.USER_DB_FLAG.equals(curRecord.getWinner())) {
            tvWin.setText("W");
            tvWin.setTextColor(colorWin);
        }
        else {
            tvWin.setText("L");
            tvWin.setTextColor(colorLose);
        }
        groupRecord.setOnClickListener(this);

        String path = ImageFactory.getMatchHeadPath(curRecord.getMatch(), curRecord.getCourt());
        ImageUtil.load("file://" + path, ivMatch, R.drawable.image_load_error);
    }

    @Override
    public void onExpansionToggled(boolean expanded) {

    }

    @Override
    public void onClick(View view) {
        onItemMenuListener.onItemClicked(curRecordItem);
    }

}
