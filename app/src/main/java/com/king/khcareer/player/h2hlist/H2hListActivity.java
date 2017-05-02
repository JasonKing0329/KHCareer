package com.king.khcareer.player.h2hlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.khcareer.base.BaseActivity;
import com.king.khcareer.base.CustomDialog;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.match.timeline.MatchActivity;
import com.king.mytennis.view.R;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceAlignmentEnum;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/30 0030.
 */

public class H2hListActivity extends BaseActivity implements IH2hListView, OnItemMenuListener
    , OnBMClickListener{

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_total_player)
    TextView tvTotalPlayer;
    @BindView(R.id.tv_to_top10)
    TextView tvToTop10;
    @BindView(R.id.rv_h2h_list)
    RecyclerView rvH2hList;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bmb_menu)
    BoomMenuButton bmbMenu;

    private H2hPresenter h2hPresenter;
    private H2hListAdapter h2hAdapter;

    private SortDialog sortDialog;
    private FilterDialog filterDialog;

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

        h2hPresenter = new H2hPresenter(this);
        h2hPresenter.loadDatas();
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

        tvTotalPlayer.setText(String.valueOf(data.getCompetitors()));
        tvToTop10.setText(data.getTop10Win() + "-" + data.getTop10Lose());

        if (h2hAdapter == null) {
            h2hAdapter = new H2hListAdapter(data.getHeaderList(), this);
            rvH2hList.setAdapter(h2hAdapter);
        }
        else {
            h2hAdapter.updateData(data.getHeaderList());
            h2hAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSortFinished() {
        h2hAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFiltFinished(List<HeaderItem> list) {
        h2hAdapter.updateData(list);
        h2hAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(RecordItem item) {

        Intent intent = new Intent().setClass(this, MatchActivity.class);
        intent.putExtra(MatchActivity.KEY_MATCH_NAME, item.getRecord().getMatch());
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
                    h2hPresenter.filterRank(min, max);
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
                    h2hPresenter.filterDeltaWin(min, max);
                }
            });
            filterDialog.setTitle(getString(R.string.filter_title));
        }
        filterDialog.show();
    }
}
