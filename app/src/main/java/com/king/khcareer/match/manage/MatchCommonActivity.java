package com.king.khcareer.match.manage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.king.khcareer.base.BaseActivity;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.image.glide.GlideOptions;
import com.king.khcareer.common.multiuser.MultiUser;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.match.timeline.MatchActivity;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;
import com.king.lib.tool.ui.RippleFactory;
import com.king.mytennis.view.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述: match win-lose for all users
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/14 11:05
 */
public class MatchCommonActivity extends BaseActivity implements ICommonView {

    public static final String KEY_MATCH = "common_match";

    @BindView(R.id.iv_match)
    ImageView ivMatch;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_country)
    TextView tvCountry;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.tv_court)
    TextView tvCourt;
    @BindView(R.id.tv_king_name)
    TextView tvKingName;
    @BindView(R.id.tv_king_h2h)
    TextView tvKingH2h;
    @BindView(R.id.group_king)
    LinearLayout groupKing;
    @BindView(R.id.tv_flamenco_name)
    TextView tvFlamencoName;
    @BindView(R.id.tv_flamenco_h2h)
    TextView tvFlamencoH2h;
    @BindView(R.id.group_flamenco)
    LinearLayout groupFlamenco;
    @BindView(R.id.tv_henry_name)
    TextView tvHenryName;
    @BindView(R.id.tv_henry_h2h)
    TextView tvHenryH2h;
    @BindView(R.id.group_henry)
    LinearLayout groupHenry;
    @BindView(R.id.tv_qi_name)
    TextView tvQiName;
    @BindView(R.id.tv_qi_h2h)
    TextView tvQiH2h;
    @BindView(R.id.group_qi)
    LinearLayout groupQi;
    @BindView(R.id.tv_king_year)
    TextView tvKingYear;
    @BindView(R.id.tv_flamenco_year)
    TextView tvFlamencoYear;
    @BindView(R.id.tv_henry_year)
    TextView tvHenryYear;
    @BindView(R.id.tv_qi_year)
    TextView tvQiYear;

    private MatchNameBean matchNameBean;

    private CommonPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_common);

        ButterKnife.bind(this);

        mPresenter = new CommonPresenter(this);

        String match = getIntent().getStringExtra(KEY_MATCH);
        matchNameBean = mPresenter.getMatchNameBean(match);

        initView();
        initData();
    }

    private void initView() {
        groupKing.setBackground(RippleFactory.getRippleBackground(getResources().getColor(R.color.mview_layout_insert_bk)
                , getResources().getColor(R.color.ripple_material_dark)));
        groupFlamenco.setBackground(RippleFactory.getRippleBackground(getResources().getColor(R.color.mview_layout_search_bk)
                , getResources().getColor(R.color.ripple_material_dark)));
        groupHenry.setBackground(RippleFactory.getRippleBackground(getResources().getColor(R.color.mview_layout_h2h_bk)
                , getResources().getColor(R.color.ripple_material_dark)));
        groupQi.setBackground(RippleFactory.getRippleBackground(getResources().getColor(R.color.mview_layout_rank_bk)
                , getResources().getColor(R.color.ripple_material_dark)));
    }

    void initData() {
        tvName.setText(matchNameBean.getName());
        tvCountry.setText(matchNameBean.getMatchBean().getCountry() + "/" + matchNameBean.getMatchBean().getCity());
        tvLevel.setText(matchNameBean.getMatchBean().getLevel());
        tvCourt.setText(matchNameBean.getMatchBean().getCourt());

        Glide.with(this)
                .load("file://" + ImageFactory.getMatchHeadPath(matchNameBean.getName(), matchNameBean.getMatchBean().getCourt()))
                .apply(GlideOptions.getDefaultMatchOptions())
                .into(ivMatch);

        loadH2hs();
    }

    private void loadH2hs() {
        mPresenter.loadHistory(matchNameBean.getName());
    }

    @Override
    public void onMatchHistoryLoaded(MultiUser user, int win, int lose, String years) {

        // 显示各个user对应的记录，保存进入详情页需要的bean
        if (user == MultiUserManager.getInstance().getUserKing()) {
            tvKingH2h.setText(win + " - " + lose);
            tvKingYear.setText(years);
        } else if (user == MultiUserManager.getInstance().getUserFlamenco()) {
            tvFlamencoH2h.setText(win + " - " + lose);
            tvFlamencoYear.setText(years);
        } else if (user == MultiUserManager.getInstance().getUserHenry()) {
            tvHenryH2h.setText(win + " - " + lose);
            tvHenryYear.setText(years);
        } else if (user == MultiUserManager.getInstance().getUserQi()) {
            tvQiH2h.setText(win + " - " + lose);
            tvQiYear.setText(years);
        }
    }

    @OnClick({R.id.group_king, R.id.group_flamenco, R.id.group_henry, R.id.group_qi})
    public void onGroupClick(View view) {
        String userId = null;
        switch (view.getId()) {
            case R.id.group_king:
                userId = MultiUserManager.getInstance().getUserKing().getId();
                break;
            case R.id.group_flamenco:
                userId = MultiUserManager.getInstance().getUserFlamenco().getId();
                break;
            case R.id.group_henry:
                userId = MultiUserManager.getInstance().getUserHenry().getId();
                break;
            case R.id.group_qi:
                userId = MultiUserManager.getInstance().getUserQi().getId();
                break;
        }
        Intent intent = new Intent().setClass(this, MatchActivity.class);
        intent.putExtra(MatchActivity.KEY_MATCH_NAME, matchNameBean.getName());
        intent.putExtra(MatchActivity.KEY_USER_ID, userId);
        startActivity(intent);
    }
}
