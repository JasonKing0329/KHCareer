package com.king.khcareer.home.k4;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.king.khcareer.common.config.Constants;
import com.king.khcareer.glory.GloryActivity;
import com.king.khcareer.player.h2hlist.H2hListActivity;
import com.king.khcareer.record.k4.RecordActivity;
import com.king.khcareer.utils.DebugLog;
import com.king.khcareer.utils.SeasonManager;
import com.king.khcareer.match.manage.MatchManageActivity;
import com.king.khcareer.match.gallery.UserMatchActivity;
import com.king.khcareer.match.gallery.UserMatchBean;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.multiuser.MultiUser;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.player.manage.PlayerManageActivity;
import com.king.khcareer.score.ScoreActivity;
import com.king.khcareer.rank.RankChartFragment;
import com.king.khcareer.rank.RankManageActivity;
import com.king.khcareer.common.image.ImageUtil;
import com.king.khcareer.common.helper.MenuService;
import com.king.khcareer.utils.DensityUtil;
import com.king.khcareer.base.BaseActivity;
import com.king.khcareer.home.classic.ManagerActivity;
import com.king.mytennis.view.R;
import com.king.khcareer.record.editor.RecordEditorActivity;
import com.king.khcareer.settings.k4.SettingActivityK4;
import com.king.khcareer.home.v7.V7MainActivity;
import com.king.khcareer.common.helper.BasicOperation;
import com.king.khcareer.player.swipecard.SwipeCardActivity;
import com.king.khcareer.pubview.CircleImageView;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements IHomeView, OnBMClickListener, IHomeHeaderHolder {

    private final int REQUEST_RANK = 101;
    private final int REQUEST_EDITOR = 102;
    private final int REQUEST_RECORD_LIST = 103;

    private HomeHeadAdapter headAdapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ctl_toolbar)
    CollapsingToolbarLayout ctlToolbar;
    @BindView(R.id.viewpager_head)
    ViewPager viewpagerHead;
    @BindView(R.id.iv_record_bk)
    RoundedImageView ivRecordBk;
    @BindView(R.id.tv_match_round)
    TextView tvMatchRound;
    @BindView(R.id.tv_match_name)
    TextView tvMatchName;
    @BindView(R.id.group_record)
    RelativeLayout groupRecord;
    @BindView(R.id.iv_player1)
    RoundedImageView ivPlayer1;
    @BindView(R.id.iv_player2)
    RoundedImageView ivPlayer2;
    @BindView(R.id.iv_player3)
    RoundedImageView ivPlayer3;
    @BindView(R.id.tv_player_name1)
    TextView tvPlayerName1;
    @BindView(R.id.tv_player_name2)
    TextView tvPlayerName2;
    @BindView(R.id.tv_player_name3)
    TextView tvPlayerName3;
    @BindView(R.id.group_player)
    LinearLayout groupPlayer;
    @BindView(R.id.group_add)
    LinearLayout groupAdd;
    @BindView(R.id.dsv_match)
    DiscreteScrollView dsvMatch;
    @BindView(R.id.group_glory)
    LinearLayout groupGlory;
    @BindView(R.id.group_h2h)
    LinearLayout groupH2h;
    @BindView(R.id.iv_user_head)
    CircleImageView ivUserHead;
    @BindView(R.id.group_nav_player)
    ViewGroup groupNavPlayer;
    @BindView(R.id.group_nav_match)
    ViewGroup groupNavMatch;
    @BindView(R.id.group_nav_load)
    ViewGroup groupLoad;
    @BindView(R.id.group_nav_setting)
    ViewGroup groupSetting;
    @BindView(R.id.tv_nav_classic)
    TextView tvNavClassic;
    @BindView(R.id.tv_nav_scrollcard)
    TextView tvNavScrollcard;
    @BindView(R.id.tv_nav_v7)
    TextView tvNavV7;
    @BindView(R.id.scroll_home)
    NestedScrollView scrollHome;
    // v4.3.2弃用
//    @BindView(R.id.bkView)
//    GradientBkView bkView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.iv_nav_image)
    ImageView ivNavImage;
    @BindView(R.id.bmb_menu)
    BoomMenuButton bmbMenu;

    private MenuService menuService;

    private HomePresenter presenter;

    private HomeMatchAdapter matchAdapter;

    private List<UserMatchBean> matchList;

    private RankChartFragment ftChart;

    private BoomMenuHome boomMenuHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        ImageUtil.initImageLoader(this);
        menuService = new MenuService();
        presenter = new HomePresenter(this);
        boomMenuHome = new BoomMenuHome(bmbMenu);

        initAppBar();
        initBoomMenu();
        initPlayerBasic();
        initNavView();
        initContent();
        initData();
    }

    /**
     * 不用老theme
     *
     * @return
     */
    @Override
    protected boolean applyCommonTheme() {
        return false;
    }

    private void initAppBar() {
        toolbar.setTitle(MultiUserManager.getInstance().getCurrentUser().getFullName());
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void initBoomMenu() {
        SeasonManager.SeasonEnum type = SeasonManager.getSeasonType();
        if (type == SeasonManager.SeasonEnum.CLAY) {
            boomMenuHome.init(BoomMenuHome.CLAY, this);
        }
        else if (type == SeasonManager.SeasonEnum.GRASS) {
            boomMenuHome.init(BoomMenuHome.GRASS, this);
        }
        else if (type == SeasonManager.SeasonEnum.INHARD) {
            boomMenuHome.init(BoomMenuHome.INHARD, this);
        }
        else {
            boomMenuHome.init(BoomMenuHome.HARD, this);
        }
    }

    private void initPlayerBasic() {
        headAdapter = new HomeHeadAdapter(getSupportFragmentManager());
        MultiUser[] users = MultiUserManager.getInstance().getUsers();
        MultiUser curUser = MultiUserManager.getInstance().getCurrentUser();
        int index = 0;
        for (int i = 0; i < users.length; i ++) {
            MultiUser user = users[i];
            HomeHeadFragment fragment = HomeHeadFragment.newInstance(user.getId());
            headAdapter.addFragment(fragment);

            if (curUser.getId().equals(user.getId())) {
                index = i;
            }
        }
        viewpagerHead.setAdapter(headAdapter);
        viewpagerHead.setCurrentItem(index);

        viewpagerHead.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onUserChanged(MultiUserManager.getInstance().getUsers()[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initNavView() {
        ImageUtil.load("file://" + ImageFactory.getPlayerHeadPath(MultiUserManager.getInstance().getCurrentUser().getFullName()), ivUserHead, R.drawable.icon_list);
        ivUserHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUserSelector();
            }
        });
    }

    private void initContent() {

        // init match gallery
        dsvMatch.setItemTransitionTimeMillis(200);
        dsvMatch.setItemTransformer(new ScaleTransformer.Builder()
                .setMinScale(0.9f)
                .build());

        initRankChart();
    }

    private void initRankChart() {
        ftChart = new RankChartFragment();
        ftChart.setOnChartGroupClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRankManageActivity();
            }
        });
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.group_chart, ftChart, "RankChartFragment");
        ft.commit();
    }

    private void showUserSelector() {
        final MultiUser[] users = MultiUserManager.getInstance().getUsers();
        String[] names = new String[users.length];
        for (int i = 0; i < users.length; i++) {
            names[i] = users[i].getFullName();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(null).setItems(names, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                viewpagerHead.setCurrentItem(index);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        }).show();
    }

    /**
     * 与数据库有关的数据初始化
     */
    private void initData() {

        refreshHomeData();
    }

    private void refreshHomeData() {
        presenter.loadHomeDatas();
    }

    @Override
    public void onHomeDataLoaded(HomeData data) {

        startRevealView(500);

        ImageUtil.load("file://" + ImageFactory.getMatchHeadPath(data.getRecordMatch(), data.getRecordCourt()), ivRecordBk
                , R.drawable.default_img);
        tvMatchName.setText(data.getRecordMatch() + "(" + data.getRecordCountry() + ")");
        tvMatchRound.setText(data.getRecordRound());
        ImageUtil.load("file://" + ImageFactory.getPlayerHeadPath(data.getPlayerName1()), ivPlayer1
                , R.drawable.glory_rank);
        tvPlayerName1.setText((data.isWinner1() ? "" : "(lose)") + data.getPlayerName1());
        ImageUtil.load("file://" + ImageFactory.getPlayerHeadPath(data.getPlayerName2()), ivPlayer2
                , R.drawable.glory_rank);
        tvPlayerName2.setText((data.isWinner2() ? "" : "(lose)") + data.getPlayerName2());
        ImageUtil.load("file://" + ImageFactory.getPlayerHeadPath(data.getPlayerName3()), ivPlayer3
                , R.drawable.glory_rank);
        tvPlayerName3.setText((data.isWinner3() ? "" : "(lose)") + data.getPlayerName3());

        matchList = data.getMatchList();

        if (matchAdapter == null) {
            matchAdapter = new HomeMatchAdapter(data.getMatchList());
            dsvMatch.setAdapter(matchAdapter);
            matchAdapter.setItemOnclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startMatchActivity();
                }
            });
        } else {
            matchAdapter.setDatas(data.getMatchList());
            matchAdapter.notifyDataSetChanged();
        }

        // 定位到与当前周最近赛事
        focusToLatestWeek(data.getMatchList());

        // 按照当前月份的赛季属性更改相应的视图
        updateSeasonStyle();
    }

    private void updateSeasonStyle() {
        SeasonManager.SeasonEnum type = SeasonManager.getSeasonType();
        if (type == SeasonManager.SeasonEnum.CLAY) {
            ivNavImage.setImageResource(R.drawable.nav_header_mon);
        }
        else if (type == SeasonManager.SeasonEnum.GRASS) {
            ivNavImage.setImageResource(R.drawable.nav_header_win);
        }
        else if (type == SeasonManager.SeasonEnum.INHARD) {
            ivNavImage.setImageResource(R.drawable.nav_header_sydney);
        }
        else {
            ivNavImage.setImageResource(R.drawable.nav_header_iw);
        }
    }

    private void focusToLatestWeek(List<UserMatchBean> matchList) {
        final int position = presenter.findLatestWeekItem(matchList);
        dsvMatch.post(new Runnable() {
            @Override
            public void run() {
                dsvMatch.scrollToPosition(position);
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void onUserChanged(MultiUser user) {
        MultiUserManager.getInstance().setCurrentUser(user);
        MultiUserManager.getInstance().saveToPreference(this, user);
        ImageUtil.load("file://" + ImageFactory.getPlayerHeadPath(user.getFullName()), ivUserHead, R.drawable.icon_list);
        ctlToolbar.setTitle(user.getFullName());

        initData();
        ftChart.onUserChanged();
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_home_save:
                executeSave();
                break;
            case R.id.menu_home_saveas:
                executeSaveAs();
                break;
            case R.id.menu_home_exit:
                executeExit();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBoomButtonClick(int index) {
        switch (index) {
            case 0:
                executeSave();
                break;
            case 1:
                executeSaveAs();
                break;
            case 2:
                executeExit();
                break;
            case 3:
                DebugLog.e("3");
                scrollHome.scrollTo(0, 0);
                break;
        }
    }

    private void executeExit() {
        finish();
    }

    private void executeSaveAs() {
        BasicOperation.showSaveAsDialog(this, null);
    }

    private void executeSave() {
        String folder = menuService.saveDatabases();
        String message = getString(R.string.save_db_success);
        message = message.replace("%s", folder);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @OnClick({R.id.group_nav, R.id.group_record, R.id.group_player, R.id.group_add, R.id.group_glory, R.id.group_h2h})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_nav:
                // 无事件，只是夺取nav group的点击事件，不让其往下渗透
                break;
            case R.id.group_record:
                startRecordLineActivity();
                break;
            case R.id.group_player:
                startPlayerActivity();
                break;
            case R.id.group_add:
                startRecordEditorActivity();
                break;
            case R.id.group_glory:
                startGloryActivity();
                break;
            case R.id.group_h2h:
                startPlayerH2hActivity();
                break;
        }
    }

    @OnClick({R.id.tv_nav_classic, R.id.tv_nav_scrollcard, R.id.tv_nav_v7, R.id.group_nav_player, R.id.group_nav_match, R.id.group_nav_load, R.id.group_nav_setting})
    public void onClickNav(View view) {
        switch (view.getId()) {
            case R.id.group_nav_player:
                startPlayerManageActivity();
                break;
            case R.id.group_nav_match:
                startMatchManageActivity();
                break;
            case R.id.group_nav_load:
                BasicOperation.showLoadFromDialog(this, new BasicOperation.DialogCallback() {

                    @Override
                    public void onOk(Object result) {
                        Toast.makeText(HomeActivity.this, R.string.loading_ok, Toast.LENGTH_SHORT).show();
                        initData();
                    }

                    @Override
                    public void onCancel(Object result) {

                    }
                });
                break;
            case R.id.group_nav_setting:
                startSettingActivity();
                break;
            case R.id.tv_nav_classic:
                startClassicActivity();
                break;
            case R.id.tv_nav_scrollcard:
                startScrollCardActivity();
                break;
            case R.id.tv_nav_v7:
                startView7Activity();
                break;
        }
    }

    private void startRankManageActivity() {
        Intent intent = new Intent().setClass(this, RankManageActivity.class);
        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this
                , Pair.create(findViewById(R.id.group_chart),getString(R.string.anim_home_rank)));
        startActivityForResult(intent, REQUEST_RANK, transitionActivityOptions.toBundle());

    }

    private void startRecordEditorActivity() {
        Intent intent = new Intent().setClass(this, RecordEditorActivity.class);
        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this
                , Pair.create(findViewById(R.id.group_add),getString(R.string.anim_home_add)));
        startActivityForResult(intent, REQUEST_EDITOR, transitionActivityOptions.toBundle());
    }

    private void startScoreActivity() {
        Intent intent = new Intent().setClass(this, ScoreActivity.class);
        startActivity(intent);
    }

    private void startGloryActivity() {
        Intent intent = new Intent().setClass(this, GloryActivity.class);
        startActivity(intent);
    }

    private void startPlayerActivity() {
        Intent intent = new Intent().setClass(this, SwipeCardActivity.class);
        intent.putExtra(SwipeCardActivity.KEY_INIT_MODE, SwipeCardActivity.INIT_PLAYER);
        ActivityOptions transitionActivityOptions = ActivityOptions.makeScaleUpAnimation(groupPlayer, 0, 0, 200, 200);
        startActivity(intent, transitionActivityOptions.toBundle());
    }

    private void startMatchActivity() {
        Intent intent = new Intent().setClass(this, UserMatchActivity.class);
        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this
                , Pair.create(findViewById(R.id.dsv_match),getString(R.string.anim_home_match)));
        startActivity(intent, transitionActivityOptions.toBundle());
    }

    private void startRecordLineActivity() {
        Intent intent = new Intent().setClass(this, RecordActivity.class);
        ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(this
                , Pair.create(findViewById(R.id.iv_record_bk),getString(R.string.anim_home_date)));
        startActivityForResult(intent, REQUEST_RECORD_LIST, transitionActivityOptions.toBundle());
    }

    private void startPlayerH2hActivity() {
        Intent intent = new Intent().setClass(this, H2hListActivity.class);
        startActivity(intent);
    }

    private void startPlayerManageActivity() {
        Intent intent = new Intent().setClass(this, PlayerManageActivity.class);
        ActivityOptions transitionActivityOptions = ActivityOptions.makeScaleUpAnimation(groupNavPlayer, 0, 0, 100, 100);
        startActivity(intent, transitionActivityOptions.toBundle());
    }

    private void startMatchManageActivity() {
        Intent intent = new Intent().setClass(this, MatchManageActivity.class);
        ActivityOptions transitionActivityOptions = ActivityOptions.makeScaleUpAnimation(groupNavMatch, 0, 0, 100, 100);
        startActivity(intent, transitionActivityOptions.toBundle());
    }

    private void startSettingActivity() {
        Intent intent = new Intent().setClass(this, SettingActivityK4.class);
        ActivityOptions transitionActivityOptions = ActivityOptions.makeScaleUpAnimation(findViewById(R.id.group_nav_setting), 0, 0, 100, 100);
        startActivity(intent, transitionActivityOptions.toBundle());
    }

    private void startClassicActivity() {
        Intent intent = new Intent().setClass(this, ManagerActivity.class);
        startActivity(intent);
        finish();
    }

    private void startScrollCardActivity() {
        Intent intent = new Intent().setClass(this, com.king.khcareer.home.v6.ManagerActivity.class);
        startActivity(intent);
        finish();
    }

    private void startView7Activity() {
        Intent intent = new Intent().setClass(this, V7MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RANK) {
            // 更新rank chart
            if (resultCode == RESULT_OK) {
                ftChart.refreshRanks(null);
            }
        }
        else if (requestCode == REQUEST_EDITOR) {
            // 增加了记录，刷新home数据
            if (resultCode == Constants.FLAG_RECORD_UPDATE) {
                refreshHomeData();
            }
        }
        else if (requestCode == REQUEST_RECORD_LIST) {
            // 删除了记录，刷新home数据
            if (resultCode == Constants.FLAG_RECORD_UPDATE) {
                refreshHomeData();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClickScoreHead() {
        startScoreActivity();
    }

    private void startRevealView(int animTime) {
        // centerX和centerY实是相对于view的
        Animator anim = ViewAnimationUtils.createCircularReveal(scrollHome, scrollHome.getWidth() / 2
                , 0, 0, (float) scrollHome.getHeight());
        anim.setDuration(animTime);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.start();
    }

}
