package com.king.khcareer.rank;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.khcareer.score.ScoreEditDialog;
import com.king.khcareer.base.BaseActivity;
import com.king.khcareer.base.CustomDialog;
import com.king.mytennis.view.R;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/22 19:07
 */
public class RankManageActivity extends BaseActivity implements View.OnClickListener, IRankParent {

    @BindView(R.id.view7_actionbar_back)
    ImageView ivBack;
    @BindView(R.id.view7_actionbar_title)
    TextView tvTitle;
    @BindView(R.id.view7_actionbar_menu)
    ImageView ivMenu;
    @BindView(R.id.group_chart_container)
    ViewGroup groupChartContainer;
    @BindView(R.id.rank_manage_list)
    RecyclerView rvRankList;

    private RankChartFragment ftChart;

    private RankPresenter rankPresenter;

    private RankItemAdapter rankItemAdapter;

    private List<RankFinalBean> rankList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rank_manage);
        ButterKnife.bind(this);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("Rank");
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRankList.setLayoutManager(manager);
        rvRankList.setItemAnimator(new DefaultItemAnimator());
        initChartFragment();

        initData();
    }

    private void initChartFragment() {
        ftChart = new RankChartFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.group_chart_container, ftChart, "RankChartFragment");
        ft.commit();
    }

    private void initData() {
        rankPresenter = new RankPresenter();
        rankList = rankPresenter.getRankList();
        rankItemAdapter = new RankItemAdapter(rankList);
        rvRankList.setAdapter(rankItemAdapter);
        rankItemAdapter.setOnRankActionListener(new RankItemAdapter.OnRankActionListener() {
            @Override
            public void onDeleteRank(int position) {
                rankPresenter.deleteRank(rankList.get(position));
                tagUpdated();
                refreshRanks();
            }

            @Override
            public void onEditRank(int position) {
                updateRank(rankList.get(position));
            }
        });
    }

    /**
     * 标志更新过数据
     */
    private void tagUpdated() {
        setResult(RESULT_OK);
    }

    @OnClick({R.id.view7_actionbar_back, R.id.iv_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view7_actionbar_back:
                // 加入了转场动画，必须用onBackPressed，finish无效果
                onBackPressed();
                break;
            case R.id.iv_add:
                addRank();
                break;
        }
    }

    private void addRank() {
        ScoreEditDialog dialog = new ScoreEditDialog(this, new CustomDialog.OnCustomDialogActionListener() {
            @Override
            public boolean onSave(Object object) {
                RankFinalBean bean = (RankFinalBean) object;
                rankPresenter.saveRankFinal(bean);
                tagUpdated();
                refreshRanks();
                return false;
            }

            @Override
            public boolean onCancel() {
                return false;
            }

            @Override
            public void onLoadData(HashMap<String, Object> data) {
                data.put(ScoreEditDialog.KEY_MODE, ScoreEditDialog.MODE_YEAR_RANK);
            }
        });
        dialog.show();
    }

    private void updateRank(final RankFinalBean rankFinalBean) {
        ScoreEditDialog dialog = new ScoreEditDialog(this, new CustomDialog.OnCustomDialogActionListener() {
            @Override
            public boolean onSave(Object object) {
                RankFinalBean bean = (RankFinalBean) object;
                rankPresenter.saveRankFinal(bean);
                tagUpdated();
                refreshRanks();
                return false;
            }

            @Override
            public boolean onCancel() {
                return false;
            }

            @Override
            public void onLoadData(HashMap<String, Object> data) {
                data.put(ScoreEditDialog.KEY_MODE, ScoreEditDialog.MODE_YEAR_RANK);
                data.put(ScoreEditDialog.KEY_INIT_RANK_FINAL_BEAN, rankFinalBean);
            }
        });
        dialog.show();
    }

    private void refreshRanks() {
        rankList = rankPresenter.getRankList();
        rankItemAdapter.setList(rankList);
        rankItemAdapter.notifyDataSetChanged();
        ftChart.refreshRanks(rankList);
    }

    @Override
    public RankPresenter getPresenter() {
        return rankPresenter;
    }
}
