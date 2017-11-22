package com.king.khcareer.home.k4;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.king.khcareer.base.BaseFragment;
import com.king.khcareer.base.IFragmentHolder;
import com.king.khcareer.common.multiuser.MultiUser;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.score.IScorePageView;
import com.king.khcareer.score.ScorePageData;
import com.king.khcareer.score.ScorePresenter;
import com.king.khcareer.utils.DensityUtil;
import com.king.mytennis.view.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/14 15:41
 */
public class HomeHeadFragment extends BaseFragment {

    private static final String BUNDLE_USERID = "userId";

    @BindView(R.id.iv_flag_bg)
    ImageView ivFlagBg;
    @BindView(R.id.tv_country)
    TextView tvCountry;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.tv_match_number)
    TextView tvMatchNumber;
    @BindView(R.id.tv_player)
    TextView tvPlayer;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.tv_rank)
    TextView tvRank;
    @BindView(R.id.group_player_basic)
    RelativeLayout groupPlayerBasic;

    private IHomeHeaderHolder holder;
    private ScorePresenter scorePrensenter;

    public static HomeHeadFragment newInstance(String userId) {

        Bundle args = new Bundle();
        args.putString(BUNDLE_USERID, userId);
        HomeHeadFragment fragment = new HomeHeadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onBindHolder(IFragmentHolder context) {
        if (context instanceof IHomeHeaderHolder) {
            holder = (IHomeHeaderHolder) context;
        }
    }

    @Override
    protected int getContentLayoutRes() {
        return R.layout.layout_player_basic;
    }

    @Override
    protected void onCreate(View view) {

        ButterKnife.bind(this, view);

        tvPlayer.setVisibility(View.INVISIBLE);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tvRank.getLayoutParams();
        params.bottomMargin = params.bottomMargin + DensityUtil.dip2px(getActivity(), 20);

        String userId = getArguments().getString(BUNDLE_USERID);

        MultiUser user = MultiUserManager.getInstance().getUser(userId);

        scorePrensenter = new ScorePresenter(user);

        ivFlagBg.setImageResource(user.getFlagImageResId());
        tvPlayer.setText(user.getFullName());
        tvCountry.setText(user.getCountry());
        tvBirthday.setText(user.getBirthday());
        tvHeight.setText(user.getHeight() + "  " + user.getWeight());

        load52WeekScore();
    }

    private void onScoreLoaded(int score, int rank) {
        tvTotal.setText(String.valueOf(score));
        tvRank.setText(String.valueOf(rank));
    }

    private void load52WeekScore() {
        scorePrensenter.setScorePageView(new IScorePageView() {
            @Override
            public void onPageDataLoaded(ScorePageData data) {
                onScoreLoaded(data.getCountScore(), scorePrensenter.loadRank().getRank());
            }
        });
        scorePrensenter.query52WeekRecords();
    }

    @OnClick(R.id.group_player_basic)
    public void onViewClicked() {
        holder.onClickScoreHead();
    }

    public void onRankChanged() {
        tvRank.setText(String.valueOf(scorePrensenter.loadRank().getRank()));
    }
}
