package com.king.khcareer.player.manage;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.khcareer.model.PubProviderHelper;
import com.king.khcareer.player.page.PlayerPageActivity;
import com.king.lib.tool.ui.RippleFactory;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.multiuser.MultiUser;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.khcareer.common.image.ImageUtil;
import com.king.khcareer.utils.ConstellationUtil;
import com.king.khcareer.base.BaseActivity;
import com.king.mytennis.view.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述: player h2h for all users
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/14 11:05
 */
public class PlayerCommonActivity extends BaseActivity implements ICommonView {

    public static final String KEY_PLAYER = "common_player";

    @BindView(R.id.common_image)
    ImageView ivImage;

    @BindView(R.id.common_head_name)
    TextView tvPlayerName;

    @BindView(R.id.common_head_name_eng)
    TextView tvPlayerEngName;

    @BindView(R.id.common_head_birthday)
    TextView tvPlayerBirthday;

    @BindView(R.id.common_head_place)
    TextView tvPlayerPlace;

    @BindView(R.id.common_group_king)
    ViewGroup groupKing;

    @BindView(R.id.common_group_flamenco)
    ViewGroup groupFlamenco;

    @BindView(R.id.common_group_henry)
    ViewGroup groupHenry;

    @BindView(R.id.common_group_qi)
    ViewGroup groupQi;

    @BindView(R.id.common_king_h2h)
    TextView tvKingH2h;

    @BindView(R.id.common_flamenco_h2h)
    TextView tvFlamencoH2h;

    @BindView(R.id.common_henry_h2h)
    TextView tvHenryH2h;

    @BindView(R.id.common_qi_h2h)
    TextView tvQiH2h;

    private PubDataProvider pubDataProvider;

    private PlayerBean playerBean;

    private CommonPresenter mPresenter;

    private Map<MultiUser, com.king.khcareer.player.timeline.PlayerBean> pbMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_common);

        ButterKnife.bind(this);

        pubDataProvider = PubProviderHelper.getProvider();
        mPresenter = new CommonPresenter(this);
        pbMap = new HashMap<>();

        String player = getIntent().getStringExtra(KEY_PLAYER);
        playerBean = pubDataProvider.getPlayerByChnName(player);

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
        tvPlayerName.setText(playerBean.getNameChn());

        if (TextUtils.isEmpty(playerBean.getNameEng()) || playerBean.getNameChn().equals(playerBean.getNameEng())) {
            tvPlayerEngName.setVisibility(View.GONE);
        }
        else {
            tvPlayerEngName.setText(playerBean.getNameEng());
        }
        tvPlayerPlace.setText(playerBean.getCountry());

        if (TextUtils.isEmpty(playerBean.getBirthday())) {
            tvPlayerBirthday.setVisibility(View.GONE);
        }
        else {
            String constel = "";
            try {
                constel = ConstellationUtil.getConstellationChn(playerBean.getBirthday());
            } catch (ConstellationUtil.ConstellationParseException e) {
                e.printStackTrace();
            }
            String birthday = playerBean.getBirthday();
            if (!TextUtils.isEmpty(constel)) {
                birthday = birthday.concat("(").concat(constel).concat(")");
            }
            tvPlayerBirthday.setText(birthday);
        }

        ImageUtil.load("file://" + ImageFactory.getDetailPlayerPath(playerBean.getNameChn()), ivImage, R.drawable.view7_folder_cover_player);

        loadH2hs();
    }

    private void loadH2hs() {
        mPresenter.loadH2hs(playerBean.getNameChn());
    }

    @Override
    public void onUserH2hLoaded(MultiUser user, int win, int lose) {

        // 组装详情页需要的bean
        com.king.khcareer.player.timeline.PlayerBean pb = new com.king.khcareer.player.timeline.PlayerBean();
        pb.setCountry(playerBean.getCountry());
        pb.setName(playerBean.getNameChn());
        pb.setWin(win);
        pb.setLose(lose);
        pbMap.put(user, pb);

        // 显示各个user对应的记录，保存进入详情页需要的bean
        if (user == MultiUserManager.getInstance().getUserKing()) {
            tvKingH2h.setText(win + " - " + lose);
        }
        else if (user == MultiUserManager.getInstance().getUserFlamenco()) {
            tvFlamencoH2h.setText(win + " - " + lose);
        }
        else if (user == MultiUserManager.getInstance().getUserHenry()) {
            tvHenryH2h.setText(win + " - " + lose);
        }
        else if (user == MultiUserManager.getInstance().getUserQi()) {
            tvQiH2h.setText(win + " - " + lose);
        }
    }

    @OnClick({R.id.common_group_king, R.id.common_group_flamenco, R.id.common_group_henry, R.id.common_group_qi})
    public void onGroupClick(View view) {
        String userId = null;
        switch (view.getId()) {
            case R.id.common_group_king:
                userId = MultiUserManager.getInstance().getUserKing().getId();
                break;
            case R.id.common_group_flamenco:
                userId = MultiUserManager.getInstance().getUserFlamenco().getId();
                break;
            case R.id.common_group_henry:
                userId = MultiUserManager.getInstance().getUserHenry().getId();
                break;
            case R.id.common_group_qi:
                userId = MultiUserManager.getInstance().getUserQi().getId();
                break;
        }
        Intent intent = new Intent().setClass(this, PlayerPageActivity.class);
        intent.putExtra(PlayerPageActivity.KEY_USER_ID, userId);
        intent.putExtra(PlayerPageActivity.KEY_COMPETITOR_NAME, playerBean.getNameChn());
        startActivity(intent);
    }
}
