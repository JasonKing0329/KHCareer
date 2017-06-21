package com.king.khcareer.glory.title;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.image.ImageUtil;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.mytennis.view.R;
import com.zaihuishou.expandablerecycleradapter.viewholder.AbstractExpandableAdapterItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/21 10:05
 */
public class SubItemAdapter extends AbstractExpandableAdapterItem implements View.OnClickListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.group_item)
    ViewGroup groupItem;
    @BindView(R.id.group_competitor)
    ViewGroup groupCompetitor;
    @BindView(R.id.iv_match)
    RoundedImageView ivMatch;
    @BindView(R.id.iv_competitor)
    CircularImageView ivCompetitor;
    @BindView(R.id.tv_seq)
    TextView tvSeq;
    @BindView(R.id.tv_lose)
    TextView tvLose;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_competitor)
    TextView tvCompetitor;
    @BindView(R.id.tv_score)
    TextView tvScore;
    
    private SubItem subItem;

    private boolean showCompetitor;
    private boolean hideSequence;
    private boolean showLose;
    private OnRecordItemListener onRecordItemListener;

    public SubItemAdapter(boolean showCompetitor, boolean hideSequence, boolean showLose
            , OnRecordItemListener onRecordItemListener) {
        this.showCompetitor = showCompetitor;
        this.hideSequence = hideSequence;
        this.showLose = showLose;
        this.onRecordItemListener = onRecordItemListener;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.adapter_glory_list_item;
    }

    @Override
    public void onBindViews(View root) {
        ButterKnife.bind(this, root);
    }

    @Override
    public void onSetViews() {

    }

    @Override
    public void onUpdateViews(Object model, int position) {
        super.onUpdateViews(model, position);
        subItem = (SubItem) model;
        Record record = subItem.getRecord();
        tvCity.setText(record.getMatchCountry() + "/" + record.getCity());
        tvLevel.setText(record.getLevel());
        tvName.setText(record.getMatch());
        tvYear.setText(record.getStrDate());

        // list是倒序排列的
        if (hideSequence) {
            tvSeq.setVisibility(View.GONE);
        }
        else {
            tvSeq.setVisibility(View.VISIBLE);
            tvSeq.setText(String.valueOf(subItem.getGroupCount() - subItem.getItemPosition()));
        }

        ImageUtil.load("file://" + ImageFactory.getMatchHeadPath(record.getMatch(), record.getCourt()), ivMatch);

        groupItem.setTag(position);
        groupItem.setOnClickListener(this);

        if (showCompetitor) {
            groupCompetitor.setVisibility(View.VISIBLE);
            tvCompetitor.setText(record.getCompetitor() + "(" + record.getCptCountry() + ")");
            tvScore.setText(record.getScore());
            ImageUtil.load("file://" + ImageFactory.getPlayerHeadPath(record.getCompetitor()), ivCompetitor);
        }
        else {
            groupCompetitor.setVisibility(View.GONE);
        }

//        if (showTitle) {
//            tvTitle.setVisibility(View.VISIBLE);
//            tvTitle.setText(titleList.get(position));
//        }
//        else {
//            tvTitle.setVisibility(View.GONE);
//        }
        tvTitle.setVisibility(View.GONE);

        if (showLose && !record.getWinner().equals(MultiUserManager.USER_DB_FLAG)) {
            tvLose.setVisibility(View.VISIBLE);
        }
        else {
            tvLose.setVisibility(View.GONE);
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {

    }

    @Override
    public void onClick(View v) {
        if (onRecordItemListener != null) {
            onRecordItemListener.onClickRecord(subItem.getRecord());
        }
    }
}
