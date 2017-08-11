package com.king.khcareer.record.k4;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.image.ImageUtil;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.pubview.CircleImageView;
import com.king.khcareer.utils.DebugLog;
import com.king.mytennis.view.R;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractExpandableAdapterItem;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/21 16:15
 */
public class RecordItemAdapter extends AbstractExpandableAdapterItem implements View.OnClickListener
    , OnBMClickListener{

    private CircleImageView ivPlayer;
    private TextView tvPlayer;
    private TextView tvRankSeed;
    private TextView tvRound;
    private TextView tvScore;
    private ViewGroup groupRecord;
    private BoomMenuButton bmbMenu;

    private OnItemMenuListener onItemMenuListener;
    
    private RecordItem curRecordItem;

    public RecordItemAdapter(OnItemMenuListener onItemMenuListener) {
        this.onItemMenuListener = onItemMenuListener;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.adapter_record_sub_item;
    }

    @Override
    public void onBindViews(View root) {
        groupRecord = (ViewGroup) root.findViewById(R.id.group_record);
        ivPlayer = (CircleImageView) root.findViewById(R.id.iv_player);
        tvPlayer = (TextView) root.findViewById(R.id.tv_player);
        tvRankSeed = (TextView) root.findViewById(R.id.tv_rank_seed);
        tvRound = (TextView) root.findViewById(R.id.tv_round);
        tvScore = (TextView) root.findViewById(R.id.tv_score);
        bmbMenu = (BoomMenuButton) root.findViewById(R.id.bmb_menu);
    }

    @Override
    public void onSetViews() {
        int padding = bmbMenu.getContext().getResources().getDimensionPixelSize(R.dimen.boom_menu_icon_padding);
        bmbMenu.addBuilder(new HamButton.Builder().normalTextRes(R.string.record_longclick_update)
            .normalImageRes(R.drawable.ic_edit_location_white_24dp)
                .imagePadding(new Rect(padding, padding, padding, padding))
                .listener(this));
        bmbMenu.addBuilder(new HamButton.Builder().normalTextRes(R.string.record_longclick_delete)
                .normalImageRes(R.drawable.ic_delete_white_24dp)
                .imagePadding(new Rect(padding, padding, padding, padding))
                .listener(this));
        bmbMenu.addBuilder(new HamButton.Builder().normalTextRes(R.string.record_longclick_detail)
            .subNormalTextRes(R.string.record_longclick_detail_sub_all)
                .normalImageRes(R.drawable.ic_assignment_ind_white_24dp)
                .imagePadding(new Rect(padding, padding, padding, padding))
                .listener(this));
        bmbMenu.addBuilder(new HamButton.Builder().normalTextRes(R.string.record_longclick_detail)
                .subNormalTextRes(R.string.record_longclick_detail_sub_this)
                .normalImageRes(R.drawable.ic_assignment_white_24dp)
                .imagePadding(new Rect(padding, padding, padding, padding))
                .listener(this));
    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        DebugLog.e("position=" + position);
        curRecordItem = (RecordItem) model;
        curRecordItem.setItemPosition(position);
        Record curRecord = curRecordItem.getRecord();
        tvPlayer.setText(curRecord.getCompetitor());
        tvRankSeed.setText("(".concat(String.valueOf(curRecord.getCptRank())).concat("/")
            .concat(String.valueOf(curRecord.getCptSeed()).concat(")")));
        tvRound.setText(curRecord.getRound());
        if (MultiUserManager.USER_DB_FLAG.equals(curRecord.getWinner())) {
            tvScore.setText(MultiUserManager.getInstance().getCurrentUser().getDisplayName() + "  " + curRecord.getScore());
        }
        else {
            tvScore.setText(curRecord.getCompetitor() + "  " + curRecord.getScore());
        }
        groupRecord.setOnClickListener(this);

        String path = ImageFactory.getPlayerHeadPath(curRecord.getCompetitor());
        ImageUtil.load("file://" + path, ivPlayer, R.drawable.icon_list);
    }

    @Override
    public void onExpansionToggled(boolean expanded) {

    }

    @Override
    public void onClick(View view) {
        onItemMenuListener.onItemClicked(view, curRecordItem);
    }

    @Override
    public void onBoomButtonClick(int index) {
        switch (index) {
            case 0:
                onItemMenuListener.onUpdateRecord(curRecordItem);
                break;
            case 1:
                onItemMenuListener.onDeleteRecord(curRecordItem);
                break;
            case 2:
                onItemMenuListener.onAllDetail(curRecordItem);
                break;
            case 3:
                onItemMenuListener.onListDetail(curRecordItem);
                break;
        }
    }
}
