package com.king.khcareer.glory;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.king.khcareer.base.BaseActivity;
import com.king.khcareer.common.config.Constants;
import com.king.khcareer.common.image.ImageUtil;
import com.king.khcareer.glory.bean.GloryTitle;
import com.king.khcareer.glory.gs.GsFragment;
import com.king.khcareer.glory.gs.MasterFragment;
import com.king.khcareer.glory.target.TargetFragment;
import com.king.khcareer.glory.title.ChampionFragment;
import com.king.khcareer.glory.title.RunnerUpFragment;
import com.king.khcareer.settings.SettingProperty;
import com.king.khcareer.utils.SeasonManager;
import com.king.mytennis.view.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/25 16:42
 */
public class GloryActivity extends BaseActivity implements IGloryHolder, IGloryView, Toolbar.OnMenuItemClickListener {

    private final String[] titles = new String[]{
            "Champions", "Runner-ups", "Grand Slam", "ATP1000", "Target"
    };
    private final int PAGE_CHAMPION = 0;
    private final int PAGE_RUNNERUP = 1;
    private final int PAGE_GS = 2;
    private final int PAGE_ATP1000 = 3;
    private final int PAGE_TARGET = 4;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.tv_career_total)
    TextView tvCareerTotal;
    @BindView(R.id.tv_season_total)
    TextView tvSeasonTotal;
    @BindView(R.id.group_title)
    ViewGroup groupTitle;
    @BindView(R.id.piechart)
    PieChart piechart;
    @BindView(R.id.tv_career)
    TextView tvCareer;
    @BindView(R.id.tv_season)
    TextView tvSeason;
    @BindView(R.id.group_career)
    LinearLayout groupCareer;
    @BindView(R.id.group_season)
    LinearLayout groupSeason;
    @BindView(R.id.tv_career_gs)
    TextView tvCareerGs;
    @BindView(R.id.tv_season_gs)
    TextView tvSeasonGs;
    @BindView(R.id.tv_career_ao)
    TextView tvCareerAo;
    @BindView(R.id.tv_career_fo)
    TextView tvCareerFo;
    @BindView(R.id.tv_career_wo)
    TextView tvCareerWo;
    @BindView(R.id.tv_career_uo)
    TextView tvCareerUo;
    @BindView(R.id.group_gs)
    RelativeLayout groupGs;
    @BindView(R.id.tv_career_master)
    TextView tvCareerMaster;
    @BindView(R.id.tv_season_master)
    TextView tvSeasonMaster;
    @BindView(R.id.group_master)
    RelativeLayout groupMaster;

    private ViewGroup groupCurHead;

    private GloryPageAdapter pagerAdapter;
    private GloryPresenter presenter;
    private GloryTitle gloryTitle;

    private ChartManager chartManager;

    private boolean isLevelChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_glory);
        ButterKnife.bind(this);

        presenter = new GloryPresenter(this);
        chartManager = new ChartManager(this);
        initView();

        showProgress(null);

        ImageUtil.initImageLoader(this, R.drawable.default_img);
        presenter.loadDatas();
    }

    private void initView() {
        // top head image
        updateSeasonStyle();

        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_more_vert_white_24dp));
        toolbar.setOnMenuItemClickListener(this);

        setCareerFocus(true);
    }

    private void initFragments() {
        pagerAdapter = new GloryPageAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new ChampionFragment(), titles[PAGE_CHAMPION]);
        pagerAdapter.addFragment(new RunnerUpFragment(), titles[PAGE_RUNNERUP]);
        pagerAdapter.addFragment(new GsFragment(), titles[PAGE_GS]);
        pagerAdapter.addFragment(new MasterFragment(), titles[PAGE_ATP1000]);
        pagerAdapter.addFragment(new TargetFragment(), titles[PAGE_TARGET]);
        viewpager.setAdapter(pagerAdapter);

        tabLayout.addTab(tabLayout.newTab().setText(titles[PAGE_CHAMPION]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[PAGE_RUNNERUP]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[PAGE_GS]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[PAGE_ATP1000]));
        tabLayout.addTab(tabLayout.newTab().setText(titles[PAGE_TARGET]));
        tabLayout.setupWithViewPager(viewpager);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                SettingProperty.setGloryPageIndex(GloryActivity.this, position);
                updatePubPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        int page = SettingProperty.getGloryPageIndex(this);
        if (page < pagerAdapter.getCount()) {
            viewpager.setCurrentItem(page);
        }
    }

    private void updatePubPage(int position) {
        if (groupCurHead != null) {
            groupCurHead.setVisibility(View.GONE);
        }
        switch (position) {
            case PAGE_CHAMPION:
                onShowChampionPage();
                break;
            case PAGE_RUNNERUP:
                onShowRunnerUpPage();
                break;
            case PAGE_GS:
                onShowGsPage();
                break;
            case PAGE_ATP1000:
                onShowAtp1000Page();
                break;
            case PAGE_TARGET:
                toolbar.getMenu().clear();
                toolbar.inflateMenu(R.menu.glory_none);
                break;
        }
    }

    private void onShowAtp1000Page() {
        groupCurHead = groupMaster;
        groupMaster.setVisibility(View.VISIBLE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.glory_none);
        tvCareerMaster.setText("Win " + gloryTitle.getMaster1000().getCareerWin() + "  Lose " + gloryTitle.getMaster1000().getCareerLose());
        tvSeasonMaster.setText("Win " + gloryTitle.getMaster1000().getSeasonWin() + "  Lose " + gloryTitle.getMaster1000().getSeasonLose());
    }

    private void onShowGsPage() {
        groupCurHead = groupGs;
        groupGs.setVisibility(View.VISIBLE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.glory_none);
        tvCareerGs.setText("Win " + gloryTitle.getGs().getCareerWin() + "  Lose " + gloryTitle.getGs().getCareerLose());
        tvSeasonGs.setText("Win " + gloryTitle.getGs().getSeasonWin() + "  Lose " + gloryTitle.getGs().getSeasonLose());
        tvCareerAo.setText("Win " + gloryTitle.getGs().getAoWin() + "  Lose " + gloryTitle.getGs().getAoLose());
        tvCareerFo.setText("Win " + gloryTitle.getGs().getFoWin() + "  Lose " + gloryTitle.getGs().getFoLose());
        tvCareerWo.setText("Win " + gloryTitle.getGs().getWoWin() + "  Lose " + gloryTitle.getGs().getWoLose());
        tvCareerUo.setText("Win " + gloryTitle.getGs().getUoWin() + "  Lose " + gloryTitle.getGs().getUoLose());
    }

    private void onShowRunnerUpPage() {
        groupCurHead = groupTitle;
        groupTitle.setVisibility(View.VISIBLE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.glory_list);
        tvCareerTotal.setText(String.valueOf(getGloryTitle().getRunnerupTitle().getCareerTotal()));
        tvSeasonTotal.setText(String.valueOf(getGloryTitle().getRunnerupTitle().getYearTotal()));
        if (Constants.GROUP_BY_COURT == SettingProperty.getGloryRunnerupGroupMode(this)) {
            chartManager.showCourtChart(piechart, gloryTitle, false, false);
            isLevelChart = false;
        } else {
            chartManager.showLevelChart(piechart, gloryTitle, false, false);
            isLevelChart = true;
        }
        setCareerFocus(true);
    }

    private void onShowChampionPage() {
        groupCurHead = groupTitle;
        groupTitle.setVisibility(View.VISIBLE);
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.glory_list);
        tvCareerTotal.setText(String.valueOf(getGloryTitle().getChampionTitle().getCareerTotal()));
        tvSeasonTotal.setText(String.valueOf(getGloryTitle().getChampionTitle().getYearTotal()));
        if (Constants.GROUP_BY_COURT == SettingProperty.getGloryChampionGroupMode(this)) {
            chartManager.showCourtChart(piechart, gloryTitle, true, false);
            isLevelChart = false;
        } else {
            chartManager.showLevelChart(piechart, gloryTitle, true, false);
            isLevelChart = true;
        }
        setCareerFocus(true);
    }

    @Override
    public void onGloryTitleLoaded(GloryTitle data) {

        gloryTitle = data;
        updatePubPage(SettingProperty.getGloryPageIndex(this));

        initFragments();

        dismissProgress();
    }

    @Override
    public GloryTitle getGloryTitle() {
        return gloryTitle;
    }

    @Override
    public GloryPresenter getPresenter() {
        return presenter;
    }

    private void updateSeasonStyle() {
        SeasonManager.SeasonEnum type = SeasonManager.getSeasonType();
        if (type == SeasonManager.SeasonEnum.CLAY) {
            ivHead.setImageResource(R.drawable.nav_header_mon);
        } else if (type == SeasonManager.SeasonEnum.GRASS) {
            ivHead.setImageResource(R.drawable.nav_header_win);
        } else if (type == SeasonManager.SeasonEnum.INHARD) {
            ivHead.setImageResource(R.drawable.nav_header_sydney);
        } else {
            ivHead.setImageResource(R.drawable.nav_header_iw);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Fragment fragment = pagerAdapter.getItem(viewpager.getCurrentItem());
        switch (item.getItemId()) {
            case R.id.menu_group_by_all:
                if (fragment instanceof ChampionFragment) {
                    ((ChampionFragment) fragment).groupBy(Constants.GROUP_BY_ALL);
                } else if (fragment instanceof RunnerUpFragment) {
                    ((RunnerUpFragment) fragment).groupBy(Constants.GROUP_BY_ALL);
                }
                break;
            case R.id.menu_group_by_court:
                if (fragment instanceof ChampionFragment) {
                    ((ChampionFragment) fragment).groupBy(Constants.GROUP_BY_COURT);
                    chartManager.showCourtChart(piechart, gloryTitle, true, false);
                } else if (fragment instanceof RunnerUpFragment) {
                    ((RunnerUpFragment) fragment).groupBy(Constants.GROUP_BY_COURT);
                    chartManager.showCourtChart(piechart, gloryTitle, false, false);
                }
                isLevelChart = false;
                break;
            case R.id.menu_group_by_level:
                if (fragment instanceof ChampionFragment) {
                    ((ChampionFragment) fragment).groupBy(Constants.GROUP_BY_LEVEL);
                    chartManager.showLevelChart(piechart, gloryTitle, true, false);
                } else if (fragment instanceof RunnerUpFragment) {
                    ((RunnerUpFragment) fragment).groupBy(Constants.GROUP_BY_LEVEL);
                    chartManager.showLevelChart(piechart, gloryTitle, false, false);
                }
                isLevelChart = true;
                break;
            case R.id.menu_group_by_year:
                if (fragment instanceof ChampionFragment) {
                    ((ChampionFragment) fragment).groupBy(Constants.GROUP_BY_YEAR);
                } else if (fragment instanceof RunnerUpFragment) {
                    ((RunnerUpFragment) fragment).groupBy(Constants.GROUP_BY_YEAR);
                }
                break;
        }
        return true;
    }

    @OnClick({R.id.group_career, R.id.group_season})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.group_career:
                if (isLevelChart) {
                    chartManager.showLevelChart(piechart, gloryTitle
                            , pagerAdapter.getItem(viewpager.getCurrentItem()) instanceof ChampionFragment
                            , false);
                } else {
                    chartManager.showCourtChart(piechart, gloryTitle
                            , pagerAdapter.getItem(viewpager.getCurrentItem()) instanceof ChampionFragment
                            , false);
                }
                setCareerFocus(true);
                break;
            case R.id.group_season:
                if (isLevelChart) {
                    chartManager.showLevelChart(piechart, gloryTitle
                            , pagerAdapter.getItem(viewpager.getCurrentItem()) instanceof ChampionFragment
                            , true);
                } else {
                    chartManager.showCourtChart(piechart, gloryTitle
                            , pagerAdapter.getItem(viewpager.getCurrentItem()) instanceof ChampionFragment
                            , true);
                }
                setCareerFocus(false);
                break;
        }
    }

    private void setCareerFocus(boolean isFocus) {
        if (isFocus) {
            tvCareer.setTextColor(getResources().getColor(R.color.tab_actionbar_text_focus));
            tvCareerTotal.setTextColor(getResources().getColor(R.color.tab_actionbar_text_focus));
            tvSeason.setTextColor(getResources().getColor(R.color.white));
            tvSeasonTotal.setTextColor(getResources().getColor(R.color.white));
        } else {
            tvSeason.setTextColor(getResources().getColor(R.color.tab_actionbar_text_focus));
            tvSeasonTotal.setTextColor(getResources().getColor(R.color.tab_actionbar_text_focus));
            tvCareer.setTextColor(getResources().getColor(R.color.white));
            tvCareerTotal.setTextColor(getResources().getColor(R.color.white));
        }
    }
}
