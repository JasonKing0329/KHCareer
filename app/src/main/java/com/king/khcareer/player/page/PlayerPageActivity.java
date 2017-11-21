package com.king.khcareer.player.page;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.king.khcareer.base.BaseActivity;
import com.king.khcareer.common.image.glide.GlideOptions;
import com.king.khcareer.pubview.CircleImageView;
import com.king.mytennis.view.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述: collapse toolbar + viewpager style
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/20 14:11
 */
public class PlayerPageActivity extends BaseActivity implements IPageView, IPageHolder {

    public static final String KEY_USER_ID = "key_user_id";
    public static final String KEY_COMPETITOR_NAME = "key_competitor_name";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_player)
    CircleImageView ivPlayer;
    @BindView(R.id.tv_info)
    TextView tvInfo;
    @BindView(R.id.tv_country)
    TextView tvCountry;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    private PagePresenter presenter;

    private PageAdapter pageAdapter;

    /**
     * 支持对任意user的competitor页面查询
     */
    private String userId;

    @Override
    protected boolean applyCommonTheme() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_page);
        ButterKnife.bind(this);

        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        tabLayout.removeAllTabs();
        init();
    }

    private void init() {
        presenter = new PagePresenter(this);

        initViews();

        initPlayerBasics();

        userId = getIntent().getStringExtra(KEY_USER_ID);
        initFragments();
    }

    private void initViews() {
        Drawable[] drawables = tvCountry.getCompoundDrawables();
        if (drawables[0] != null) {
            drawables[0].setColorFilter(getResources().getColor(R.color.icon_grey), PorterDuff.Mode.SRC_IN);
        }
        // 不用公共的icon，这样会使其他界面引用该资源颜色也被下面的代码修改
//        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_filterrable);
        toolbar.getNavigationIcon().setColorFilter(
                getResources().getColor(R.color.grey), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initPlayerBasics() {
        String name = getIntent().getStringExtra(KEY_COMPETITOR_NAME);
        if (!TextUtils.isEmpty(name)) {
            presenter.loadPlayerByChnName(name);
        }
        presenter.loadPlayerInfor();
    }

    @Override
    public void showPlayerInfo(String engName, String info, String imagePath, String country) {
        collapsingToolbar.setTitle(engName);
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.grey));
        tvInfo.setText(info);
        Glide.with(this)
                .load(imagePath)
                .apply(GlideOptions.getDefaultPlayerOptions())
                .into(ivPlayer);
        tvCountry.setText(country);
    }

    @Override
    public void showError(String s) {
        showConfirmMessage(s, null);
    }

    @Override
    public void onTabLoaded(List<TabBean> list) {
        pageAdapter = new PageAdapter(getSupportFragmentManager());
        for (TabBean bean:list) {
            TabLayout.Tab shotsTab = tabLayout.newTab();
            TabCustomView shotsTabCustomView = new TabCustomView(this);
            shotsTab.setCustomView(shotsTabCustomView);
            shotsTabCustomView.setCount(bean.win + "-" + bean.lose);
            shotsTabCustomView.setContentCategory(bean.name);
            tabLayout.addTab(shotsTab);

            PageFragment fragment = PageFragment.newInstance(bean.name, userId);
            pageAdapter.addFragment(fragment);
        }
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewpager));

        viewpager.setAdapter(pageAdapter);
    }

    private void initFragments() {
        presenter.loadRecords(userId);
    }

    @Override
    public PagePresenter getPresenter() {
        return presenter;
    }
}
