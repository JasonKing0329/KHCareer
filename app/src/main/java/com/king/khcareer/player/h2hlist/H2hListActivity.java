package com.king.khcareer.player.h2hlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.king.khcareer.base.BaseActivity;
import com.king.khcareer.base.CustomDialog;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.glory.ChartManager;
import com.king.khcareer.model.sql.player.bean.H2hParentBean;
import com.king.khcareer.player.timeline.PlayerActivity;
import com.king.khcareer.pubview.SideBar;
import com.king.khcareer.utils.AnimUtil;
import com.king.mytennis.view.R;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceAlignmentEnum;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/4/30 0030.
 */

public class H2hListActivity extends BaseActivity implements IH2hListView, OnItemMenuListener
        , OnBMClickListener, SideBar.OnTouchingLetterChangedListener {

    @BindView(R.id.group_root)
    ViewGroup groupRoot;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_total_player)
    TextView tvTotalPlayer;
    @BindView(R.id.rv_h2h_list)
    RecyclerView rvH2hList;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bmb_menu)
    BoomMenuButton bmbMenu;
    @BindView(R.id.piechart)
    PieChart pieChart;
    @BindView(R.id.tv_career)
    TextView tvCareer;
    @BindView(R.id.tv_win)
    TextView tvWin;
    @BindView(R.id.tv_season)
    TextView tvSeason;
    @BindView(R.id.tv_lose)
    TextView tvLose;
    @BindView(R.id.tv_conclude)
    TextView tvConclude;
    @BindView(R.id.ctl_toolbar)
    CollapsingToolbarLayout ctlToolbar;
    @BindView(R.id.sidebar)
    SideBar sideBar;
    @BindView(R.id.tv_index)
    TextView tvIndex;

    private H2hPresenter h2hPresenter;
    private H2hListAdapter h2hAdapter;

    private SortDialog sortDialog;
    private FilterDialog filterDialog;

    private ChartManager chartManager;
    private H2hListPageData pageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_h2h_list);
        ButterKnife.bind(this);

        initToolbar();
        initBoomButton();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvH2hList.setLayoutManager(manager);

        sideBar.setOnTouchingLetterChangedListener(this);
        sideBar.setTextView(tvIndex);
        // 底部栏控制sidebar显示
        tvConclude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (h2hPresenter.getSortType() == SortDialog.SORT_TYPE_NAME) {
                    if (sideBar.getVisibility() == View.VISIBLE) {
                        sideBar.setVisibility(View.GONE);
                    }
                    else {
                        sideBar.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        chartManager = new ChartManager(this);
        h2hPresenter = new H2hPresenter(this);
        h2hPresenter.loadDatas();

        groupRoot.post(new Runnable() {
            @Override
            public void run() {
                AnimUtil.startFullCircleRevealAnimation(H2hListActivity.this, groupRoot, null);
            }
        });
    }

    private void initToolbar() {
        toolbar.setTitle(MultiUserManager.getInstance().getCurrentUser().getFullName());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ivHead.setImageResource(MultiUserManager.getInstance().getCurrentUser().getFlagImageResId());
    }

    private void initBoomButton() {
        int radius = bmbMenu.getContext().getResources().getDimensionPixelSize(R.dimen.boom_menu_btn_radius);
        bmbMenu.setButtonEnum(ButtonEnum.SimpleCircle);
        bmbMenu.setButtonRadius(radius);
        bmbMenu.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_1);
        bmbMenu.setButtonPlaceEnum(ButtonPlaceEnum.Vertical);
        bmbMenu.setButtonPlaceAlignmentEnum(ButtonPlaceAlignmentEnum.BR);
        bmbMenu.setButtonRightMargin(bmbMenu.getContext().getResources().getDimensionPixelSize(R.dimen.home_pop_menu_right));
        bmbMenu.setButtonBottomMargin(bmbMenu.getContext().getResources().getDimensionPixelSize(R.dimen.home_pop_menu_bottom));
        bmbMenu.setButtonVerticalMargin(bmbMenu.getContext().getResources().getDimensionPixelSize(R.dimen.boom_menu_btn_margin_ver));
        bmbMenu.addBuilder(new SimpleCircleButton.Builder()
                .normalImageRes(R.drawable.ic_sort_white_24dp)
                .buttonRadius(radius)
                .listener(this));
        bmbMenu.addBuilder(new SimpleCircleButton.Builder()
                .normalImageRes(R.drawable.ic_filter_list_white_24dp)
                .buttonRadius(radius)
                .listener(this));
        bmbMenu.addBuilder(new SimpleCircleButton.Builder()
                .normalImageRes(R.drawable.ic_refresh_white_24dp)
                .buttonRadius(radius)
                .listener(this));

    }

    @Override
    public void onDataLoaded(H2hListPageData data) {

        this.pageData = data;
        tvTotalPlayer.setText(String.valueOf(data.getHeaderList().size()));

        if (h2hAdapter == null) {
            h2hAdapter = new H2hListAdapter(this, data.getHeaderList(), this);
            rvH2hList.setAdapter(h2hAdapter);
        } else {
            h2hAdapter.updateData(data.getHeaderList());
            h2hAdapter.notifyDataSetChanged();
        }
        updateCurrentWinLose();

        tvCareer.setSelected(true);
        tvWin.setSelected(true);
        showChart();

        updateSideBar();
    }

    private void updateSideBar() {
        // 只有按name排序创建并显示side bar
        if (h2hPresenter.getSortType() == SortDialog.SORT_TYPE_NAME) {
            sideBar.setVisibility(View.VISIBLE);
            h2hAdapter.updateSideBar(sideBar);
        }
        else {
            sideBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTouchingLetterChanged(String letter) {
        rvH2hList.scrollToPosition(h2hAdapter.getIndexPosition(letter));
    }

    @Override
    public void onSortFinished() {
        h2hAdapter.updateData(pageData.getShowList());
        h2hAdapter.notifyDataSetChanged();
        updateSideBar();
    }

    @Override
    public void onFilterFinished() {
        h2hAdapter.updateData(pageData.getShowList());
        h2hAdapter.notifyDataSetChanged();
        updateCurrentWinLose();
        updateSideBar();
    }

    private void updateCurrentWinLose() {
        int[] winlose = h2hAdapter.getWinLose();
        tvConclude.setText("Win " + winlose[0] + "  Lose " + winlose[1]);
    }

    @Override
    public void onItemClicked(H2hParentBean item) {

        Intent intent = new Intent();
        intent.setClass(this, PlayerActivity.class);
        intent.putExtra(PlayerActivity.KEY_COMPETITOR_NAME, item.getPlayer());
        startActivity(intent);
    }

    @Override
    public void onBoomButtonClick(int index) {

        switch (index) {
            case 0:
                showSortDialog();
                break;
            case 1:
                showFilterDialog();
                break;
            case 2:
                h2hPresenter.loadDatas();
                break;
        }
    }

    private void showSortDialog() {
        if (sortDialog == null) {
            sortDialog = new SortDialog(this, new CustomDialog.OnCustomDialogActionListener() {
                @Override
                public boolean onSave(Object object) {
                    Map<String, Object> map = (Map<String, Object>) object;
                    int sortType = (int) map.get("type");
                    int sortOrder = (int) map.get("order");
                    h2hPresenter.sortDatas(sortType, sortOrder);
                    return false;
                }

                @Override
                public boolean onCancel() {
                    return false;
                }

                @Override
                public void onLoadData(HashMap<String, Object> data) {

                }
            });
            sortDialog.setTitle(getString(R.string.sort_title));
        }
        sortDialog.show();
    }

    private void showFilterDialog() {
        if (filterDialog == null) {
            filterDialog = new FilterDialog(this, null);
            filterDialog.setFilterCallback(new FilterDialog.FilterCallback() {
                @Override
                public void onFilterNothing() {
                    h2hPresenter.filterNothing();
                }

                @Override
                public void onFilterCountry(String country) {
                    h2hPresenter.filterCountry(country);
                }

                @Override
                public void onFilterRank(int min, int max) {

                }

                @Override
                public void onFilterCount(int min, int max) {
                    h2hPresenter.filterCount(min, max);
                }

                @Override
                public void onFilterWin(int min, int max) {
                    h2hPresenter.filterWin(min, max);
                }

                @Override
                public void onFilterLose(int min, int max) {
                    h2hPresenter.filterLose(min, max);
                }

                @Override
                public void onFilterDeltaWin(int min, int max) {
                    h2hPresenter.filterOdds(min, max);
                }
            });
            filterDialog.setTitle(getString(R.string.filter_title));
        }
        filterDialog.show();
    }

    @OnClick({R.id.tv_career, R.id.tv_win, R.id.tv_season, R.id.tv_lose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_career:
                tvCareer.setSelected(true);
                tvSeason.setSelected(false);
                showChart();
                break;
            case R.id.tv_win:
                tvWin.setSelected(true);
                tvLose.setSelected(false);
                showChart();
                break;
            case R.id.tv_season:
                tvCareer.setSelected(false);
                tvSeason.setSelected(true);
                showChart();
                break;
            case R.id.tv_lose:
                tvWin.setSelected(false);
                tvLose.setSelected(true);
                showChart();
                break;
        }
    }

    private void showChart() {
        float[] values = new float[pageData.getChartContents().length];
        Integer[] targetValues;
        if (tvCareer.isSelected()) {
            if (tvWin.isSelected()) {
                targetValues = pageData.getCareerChartWinValues();
            }
            else {
                targetValues = pageData.getCareerChartLoseValues();
            }
        }
        else {
            if (tvWin.isSelected()) {
                targetValues = pageData.getSeasonChartWinValues();
            }
            else {
                targetValues = pageData.getSeasonChartLoseValues();
            }
        }
        for (int i = 0; i < values.length; i++) {
            values[i] = targetValues[i];
        }
        chartManager.showH2hChart(pieChart, pageData.getChartContents(), values);
    }
}
