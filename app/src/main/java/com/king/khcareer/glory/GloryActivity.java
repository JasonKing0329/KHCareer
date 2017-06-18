package com.king.khcareer.glory;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.khcareer.base.BaseActivity;
import com.king.khcareer.common.image.ImageUtil;
import com.king.khcareer.glory.gs.GsFragment;
import com.king.khcareer.glory.gs.MasterFragment;
import com.king.khcareer.glory.title.SeqChampionListFragment;
import com.king.khcareer.glory.title.SeqRunnerupListFragment;
import com.king.khcareer.utils.SeasonManager;
import com.king.mytennis.view.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/25 16:42
 */
public class GloryActivity extends BaseActivity implements IGloryHolder, IGloryView {

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

        ImageUtil.initImageLoader(this, R.drawable.swipecard_default_img);
        presenter.loadDatas();
    }

    private void initView() {
        // top head image
        updateSeasonStyle();
    }

    private void initFragments() {
        pagerAdapter = new GloryPageAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new SeqChampionListFragment(), "Champions");
        pagerAdapter.addFragment(new SeqRunnerupListFragment(), "Runner-ups");
        pagerAdapter.addFragment(new GsFragment(), "GS");
        pagerAdapter.addFragment(new MasterFragment(), "ATP1000");
        viewpager.setAdapter(pagerAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("Champions"));
        tabLayout.addTab(tabLayout.newTab().setText("Runner-ups"));
        tabLayout.addTab(tabLayout.newTab().setText("GS"));
        tabLayout.addTab(tabLayout.newTab().setText("ATP1000"));
        tabLayout.setupWithViewPager(viewpager);
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

}
