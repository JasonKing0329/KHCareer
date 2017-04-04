package com.king.mytennis.match;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.king.mytennis.model.Constants;
import com.king.mytennis.view.BaseActivity;
import com.king.mytennis.view.R;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述: 按week排列的横向gallery赛事总览，进入时定位到离当前周数最近的赛事
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/15 9:42
 */
public class UserMatchActivity extends BaseActivity implements DiscreteScrollView.CurrentItemChangeListener {

    @BindView(R.id.match_bk)
    GradientBkView vMatchBk;
    @BindView(R.id.match_name)
    TextView tvMatch;
    @BindView(R.id.match_place)
    TextView tvPlace;
    @BindView(R.id.match_gallery)
    DiscreteScrollView dsvMatch;
    @BindView(R.id.match_month)
    TextView tvMonth;
    @BindView(R.id.match_week)
    TextView tvWeek;
    @BindView(R.id.match_download)
    FloatingActionButton fabDownload;

    private UserMatchPresenter mPresenter;
    private UserMatchAdapter userMatchAdapter;
    private List<UserMatchBean> matchList;

    private int nMatchIndex;
    private MatchScrollManager scrollManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_match);

        ButterKnife.bind(this);
        mPresenter = new UserMatchPresenter(this);
        scrollManager = new MatchScrollManager(this);
        // 绑定滑动过程中联动变化的view
        scrollManager.bindBehaviorView(vMatchBk, fabDownload);

        initMatchGallery();

    }

    private void initMatchGallery() {
        dsvMatch.setCurrentItemChangeListener(this);
        dsvMatch.setItemTransitionTimeMillis(200);
        dsvMatch.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
        dsvMatch.setScrollStateChangeListener(scrollManager);
        matchList = mPresenter.getMatchList();

        // 绑定滑动依据数据
        scrollManager.bindData(matchList);

        userMatchAdapter = new UserMatchAdapter(this, matchList);
        dsvMatch.setAdapter(userMatchAdapter);

        // 定位到最近的赛事
        focusToLatestWeek();
    }

    private void focusToLatestWeek() {
        final int position = mPresenter.findLatestWeekItem(matchList);
        dsvMatch.post(new Runnable() {
            @Override
            public void run() {
                dsvMatch.smoothScrollToPosition(position);
            }
        });
        scrollManager.initPosition(position);
    }

    private void onItemChanged(int position) {
        nMatchIndex = position;
        UserMatchBean bean = matchList.get(position);
        tvMatch.setText(bean.getNameBean().getName());
        tvPlace.setText(bean.getNameBean().getMatchBean().getCountry()
                + "/" + bean.getNameBean().getMatchBean().getCity());
        tvMonth.setText(Constants.MONTH_ENG[bean.getNameBean().getMatchBean().getMonth() - 1]);
        tvWeek.setText("week " + bean.getNameBean().getMatchBean().getWeek());
    }

    @OnClick({R.id.match_back})
    public void onBack() {
        finish();
    }

    @OnClick({R.id.match_refresh})
    public void onRefresh() {
        userMatchAdapter.refreshImage(nMatchIndex);
    }

    @OnClick({R.id.match_download})
    public void onDownload() {
        userMatchAdapter.startDownload(nMatchIndex);
    }

    @OnClick({R.id.match_delete})
    public void onDelete() {
        userMatchAdapter.deleteImage(nMatchIndex);
    }

    @Override
    public void onCurrentItemChanged(RecyclerView.ViewHolder viewHolder, int adapterPosition) {
        onItemChanged(adapterPosition);
    }
}
