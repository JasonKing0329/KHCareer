package com.king.khcareer.match.gallery;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.base.BaseActivity;
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

    public static final String KEY_START_POSITION = "start_position";

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
        userMatchAdapter.setMatchTextView(tvMatch, tvPlace);
        dsvMatch.setAdapter(userMatchAdapter);

        // 定位到最近的赛事或者intent指定的赛事
        focusToLatestWeek();
    }

    private void focusToLatestWeek() {

        String pos = getIntent().getStringExtra(KEY_START_POSITION);
        final int position;
        if (TextUtils.isEmpty(pos)) {
            position = mPresenter.findLatestWeekItem(matchList);
        }
        else {
            position = Integer.parseInt(pos);
        }
        dsvMatch.scrollToPosition(position);

        // 必须post，因为在GradientBkView里的相关计算getWidth()和getHeight()还等于0，渐变颜色的相关区域跟其有关
        // 不post的话会造成只有一种颜色
        vMatchBk.post(new Runnable() {
            @Override
            public void run() {
                scrollManager.initPosition(position);
            }
        });
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
        // 加入了转场动画，必须用onBackPressed，finish无效果
        onBackPressed();
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
