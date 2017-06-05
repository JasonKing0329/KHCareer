package com.king.khcareer.glory;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.khcareer.base.BaseActivity;
import com.king.mytennis.view.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/5/25 16:42
 */
public class GloryActivity extends BaseActivity implements IGloryHolder {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_glory);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        pagerAdapter = new GloryPageAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new ChampionFragment(), "Champions");
        pagerAdapter.addFragment(new RunnerUpFragment(), "Runner-ups");
        pagerAdapter.addFragment(new GsFragment(), "GS");
        viewpager.setAdapter(pagerAdapter);

        tabLayout.addTab(tabLayout.newTab().setText("Champions"));
        tabLayout.addTab(tabLayout.newTab().setText("Runner-ups"));
        tabLayout.addTab(tabLayout.newTab().setText("GS"));
        tabLayout.setupWithViewPager(viewpager);
    }
}
