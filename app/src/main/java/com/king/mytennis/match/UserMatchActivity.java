package com.king.mytennis.match;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.king.mytennis.view.BaseActivity;
import com.king.mytennis.view.R;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/15 9:42
 */
public class UserMatchActivity extends BaseActivity implements DiscreteScrollView.CurrentItemChangeListener {

    @BindView(R.id.match_name)
    TextView tvMatch;
    @BindView(R.id.match_place)
    TextView tvPlace;
    @BindView(R.id.match_gallery)
    DiscreteScrollView dsvMatch;

    private UserMatchPresenter mPresenter;
    private UserMatchAdapter userMatchAdapter;
    private List<UserMatchBean> matchList;

    private int nMatchIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_match);

        ButterKnife.bind(this);

        mPresenter = new UserMatchPresenter(this);

        initMatchGallery();

    }

    private void initMatchGallery() {
        dsvMatch.setCurrentItemChangeListener(this);
        dsvMatch.setItemTransitionTimeMillis(200);
        dsvMatch.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.8f)
                .build());
        matchList = mPresenter.getMatchList();

        userMatchAdapter = new UserMatchAdapter(this, matchList);
        dsvMatch.setAdapter(userMatchAdapter);

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
    }

    private void onItemChanged(int position) {
        nMatchIndex = position;
        UserMatchBean bean = matchList.get(position);
        tvMatch.setText(bean.getNameBean().getName());
        tvPlace.setText(bean.getNameBean().getMatchBean().getCountry()
            + "/" + bean.getNameBean().getMatchBean().getCity());
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
