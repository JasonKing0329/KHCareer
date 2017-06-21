package com.king.khcareer.glory;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.khcareer.base.BaseActivity;
import com.king.khcareer.common.config.Constants;
import com.king.khcareer.common.image.ImageUtil;
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

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/25 16:42
 */
public class GloryActivity extends BaseActivity implements IGloryHolder, IGloryView, Toolbar.OnMenuItemClickListener {

    private final String[] titles = new String[] {
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
    @BindView(R.id.tv_career_title)
    TextView tvCareerTitle;
    @BindView(R.id.tv_career)
    TextView tvCareer;
    @BindView(R.id.tv_season_title)
    TextView tvSeasonTitle;
    @BindView(R.id.tv_season)
    TextView tvSeason;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    private GloryPageAdapter pagerAdapter;
    private GloryPresenter presenter;
    private GloryTitle gloryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_glory);
        ButterKnife.bind(this);

        presenter = new GloryPresenter(this);
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
        // 第一次viewpager.setCurrentItem(0)不会触发onPageSelected
        if (page == 0) {
            toolbar.inflateMenu(R.menu.glory_list);
        }
    }

    private void updatePubPage(int position) {
        switch (position) {
            case PAGE_CHAMPION:
                toolbar.getMenu().clear();
                toolbar.inflateMenu(R.menu.glory_list);
                break;
            case PAGE_RUNNERUP:
                toolbar.getMenu().clear();
                toolbar.inflateMenu(R.menu.glory_list);
                break;
            case PAGE_GS:
                toolbar.getMenu().clear();
                toolbar.inflateMenu(R.menu.glory_none);
                break;
            case PAGE_ATP1000:
                toolbar.getMenu().clear();
                toolbar.inflateMenu(R.menu.glory_none);
                break;
            case PAGE_TARGET:
                toolbar.getMenu().clear();
                toolbar.inflateMenu(R.menu.glory_none);
                break;
        }
    }

    @Override
    public void onGloryTitleLoaded(GloryTitle data) {

        gloryTitle = data;
        tvCareerTitle.setText(String.valueOf(data.getCareerTitle()));
        tvSeasonTitle.setText(String.valueOf(data.getYearTitle()));
        StringBuffer career = new StringBuffer();
        if (data.getCareerGs() > 0) {
            career.append("  ").append("Grand Slam(").append(data.getCareerGs()).append(")");
        }
        if (data.getCareerMasterCup() > 0) {
            career.append("  ").append("Master Cup(").append(data.getCareerMasterCup()).append(")");
        }
        if (data.getCareerAtp1000() > 0) {
            career.append("  ").append("ATP 1000(").append(data.getCareerAtp1000()).append(")");
        }
        if (data.getCareerAtp500() > 0) {
            career.append("  ").append("ATP 500(").append(data.getCareerAtp500()).append(")");
        }
        if (data.getCareerAtp250() > 0) {
            career.append("  ").append("ATP 250(").append(data.getCareerAtp250()).append(")");
        }
        if (data.getCareerOlympics() > 0) {
            career.append("  ").append("Olympics(").append(data.getCareerOlympics()).append(")");
        }
        tvCareer.setText(career.toString());

        StringBuffer year = new StringBuffer();
        if (data.getYearGs() > 0) {
            year.append("  ").append("Grand Slam(").append(data.getYearGs()).append(")");
        }
        if (data.getYearMasterCup() > 0) {
            year.append("  ").append("Master Cup(").append(data.getYearMasterCup()).append(")");
        }
        if (data.getYearAtp1000() > 0) {
            year.append("  ").append("ATP 1000(").append(data.getYearAtp1000()).append(")");
        }
        if (data.getYearAtp500() > 0) {
            year.append("  ").append("ATP 500(").append(data.getYearAtp500()).append(")");
        }
        if (data.getYearAtp250() > 0) {
            year.append("  ").append("ATP 250(").append(data.getYearAtp250()).append(")");
        }
        if (data.getYearOlympics() > 0) {
            year.append("  ").append("Olympics(").append(data.getYearOlympics()).append(")");
        }
        tvSeason.setText(year.toString());

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
        }
        else if (type == SeasonManager.SeasonEnum.GRASS) {
            ivHead.setImageResource(R.drawable.nav_header_win);
        }
        else if (type == SeasonManager.SeasonEnum.INHARD) {
            ivHead.setImageResource(R.drawable.nav_header_sydney);
        }
        else {
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
                }
                else if (fragment instanceof RunnerUpFragment) {
                    ((RunnerUpFragment) fragment).groupBy(Constants.GROUP_BY_ALL);
                }
                break;
            case R.id.menu_group_by_court:
                if (fragment instanceof ChampionFragment) {
                    ((ChampionFragment) fragment).groupBy(Constants.GROUP_BY_COURT);
                }
                else if (fragment instanceof RunnerUpFragment) {
                    ((RunnerUpFragment) fragment).groupBy(Constants.GROUP_BY_COURT);
                }
                break;
            case R.id.menu_group_by_level:
                if (fragment instanceof ChampionFragment) {
                    ((ChampionFragment) fragment).groupBy(Constants.GROUP_BY_LEVEL);
                }
                else if (fragment instanceof RunnerUpFragment) {
                    ((RunnerUpFragment) fragment).groupBy(Constants.GROUP_BY_LEVEL);
                }
                break;
            case R.id.menu_group_by_year:
                if (fragment instanceof ChampionFragment) {
                    ((ChampionFragment) fragment).groupBy(Constants.GROUP_BY_YEAR);
                }
                else if (fragment instanceof RunnerUpFragment) {
                    ((RunnerUpFragment) fragment).groupBy(Constants.GROUP_BY_YEAR);
                }
                break;
        }
        return true;
    }
}
