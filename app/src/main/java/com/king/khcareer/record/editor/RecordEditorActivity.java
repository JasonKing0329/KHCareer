package com.king.khcareer.record.editor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.king.khcareer.match.manage.MatchCache;
import com.king.khcareer.match.manage.MatchManageActivity;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.player.manage.PlayerManageActivity;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.khcareer.record.RecordService;
import com.king.khcareer.base.BaseActivity;
import com.king.mytennis.view.R;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/13 13:23
 */
public class RecordEditorActivity extends BaseActivity implements IEditorView, IEditorHolder, View.OnClickListener {

    public static final String KEY_RECORD_ID = "record_id";

    private final int REQUEST_CHANGE_MATCH = 101;
    private final int REQUEST_CHANGE_PLAYER = 102;

    private ViewGroup groupPlayer, groupMatch;
    private TextView nextPageView, previousPageView, doneView, continueView;
    private Toolbar toolbar;

    private PlayerEditPage playerEditPage;
    private MatchEditPage matchEditPage;

    private EditorPresenter presenter;

    private Record record;
    private MatchNameBean matchNameBean;

    /**
     * 修改模式
     */
    private boolean isUpdateMode;

    @Override
    protected void onDestroy() {
        MatchCache.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_record_editor);

        presenter = new EditorPresenter(this);

        initParentView();

        showPlayerView();

        String recordId = getIntent().getStringExtra(KEY_RECORD_ID);
        // 修改模式
        if (recordId != null) {
            isUpdateMode = true;
            presenter.loadRecord(Integer.parseInt(recordId));
        }

        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean applyCommonTheme() {
        return false;
    }

    private void initParentView() {
        groupMatch = (ViewGroup) findViewById(R.id.editor_layout_match);
        groupPlayer = (ViewGroup) findViewById(R.id.editor_layout_player);
        nextPageView = (TextView) findViewById(R.id.next_indicator);
        previousPageView = (TextView) findViewById(R.id.previous_indicator);
        doneView = (TextView) findViewById(R.id.done_indicator);
        continueView = (TextView) findViewById(R.id.continue_indicator);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // 只有title必须在setSupportActionBar之前调用，其他都必须在setSupportActionBar之后调用
        toolbar.setTitle(getResources().getString(R.string.player_infor));
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 加入了转场动画，必须用onBackPressed，finish无效果
                onBackPressed();
            }
        });
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        nextPageView.setOnClickListener(this);
        previousPageView.setOnClickListener(this);
        doneView.setOnClickListener(this);
        continueView.setOnClickListener(this);
    }

    private void showPlayerView() {
        if (playerEditPage == null) {
            playerEditPage = new PlayerEditPage(this);
            playerEditPage.initView();
        }
        groupPlayer.setVisibility(View.VISIBLE);
        groupMatch.setVisibility(View.GONE);
    }

    private void showMatchView() {
        if (matchEditPage == null) {
            matchEditPage = new MatchEditPage(this);
            matchEditPage.initView();
            if (matchNameBean == null) {
                matchEditPage.initData();
            }
            else {
                matchEditPage.initWithRecord(record, matchNameBean);
            }
        }
        groupPlayer.setVisibility(View.GONE);
        groupMatch.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v == nextPageView) {
            toolbar.setTitle(getResources().getString(R.string.match_infor));
            previousPageView.setVisibility(View.VISIBLE);
            nextPageView.setVisibility(View.GONE);
            doneView.setVisibility(View.VISIBLE);
            showMatchView();
        } else if (v == previousPageView) {
            toolbar.setTitle(getResources().getString(R.string.player_infor));
            previousPageView.setVisibility(View.GONE);
            nextPageView.setVisibility(View.VISIBLE);
            doneView.setVisibility(View.GONE);
            showPlayerView();
        } else if (v == doneView) {
            // 修改
            if (isUpdateMode) {
                if (update()) {
                    finish();
                }
                else {
                    Toast.makeText(this, R.string.update_failed, Toast.LENGTH_LONG).show();
                }
            }
            // 添加
            else {
                if (continueView.getVisibility() == View.GONE) {
                    if (insert()) {
                        matchEditPage.saveAutoFill();
                        Toast.makeText(this, R.string.insert_done, Toast.LENGTH_LONG).show();
                        continueView.setVisibility(View.VISIBLE);
                    }
                } else {
                    finish();
                }
            }
        } else if (v == continueView) {
            continueInsert();
        }
    }

    private void continueInsert() {
        continueView.setVisibility(View.GONE);
        toolbar.setTitle(getResources().getString(R.string.player_infor));
        previousPageView.setVisibility(View.GONE);
        nextPageView.setVisibility(View.VISIBLE);
        doneView.setVisibility(View.GONE);
        playerEditPage.reset();
        matchEditPage.reset();
        showPlayerView();
    }

    /**
     * 执行修改
     * @return
     */
    private boolean update() {
        String errorMsg = playerEditPage.fillRecord(record);
        if (errorMsg == null) {
            errorMsg = matchEditPage.fillRecord(record);
        }
        if (errorMsg != null) {
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            return false;
        }

        return presenter.updateRecord(record);
    }

    /**
     * 执行添加
     *
     * @return
     */
    private boolean insert() {

        Record record = new Record();
        String errorMsg = playerEditPage.fillRecord(record);
        if (errorMsg == null) {
            errorMsg = matchEditPage.fillRecord(record);
        }
        if (errorMsg != null) {
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            return false;
        }

        return presenter.insertRecord(record);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public String getCompetitor() {
        return playerEditPage.getCompetitor();
    }

    @Override
    public void selectMatch() {
        Intent intent = new Intent().setClass(this, MatchManageActivity.class);
        intent.putExtra(MatchManageActivity.KEY_START_MODE, MatchManageActivity.START_MODE_SELECT);
        startActivityForResult(intent, REQUEST_CHANGE_MATCH);
    }

    @Override
    public void selectPlayer() {
        Intent intent = new Intent().setClass(this, PlayerManageActivity.class);
        intent.putExtra(PlayerManageActivity.KEY_START_MODE, PlayerManageActivity.START_MODE_SELECT);
        startActivityForResult(intent, REQUEST_CHANGE_PLAYER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHANGE_MATCH) {
            if (resultCode == RESULT_OK) {
                MatchNameBean bean = MatchCache.getInstance();
                matchEditPage.onMatchSelected(bean);
            }
        }
        if (requestCode == REQUEST_CHANGE_PLAYER) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                PlayerBean bean = new PlayerBean();
                bean.setNameChn(bundle.getString("name"));
                bean.setBirthday(bundle.getString("birthday"));
                bean.setNameEng(bundle.getString("name_eng"));
                bean.setCountry(bundle.getString("country"));
                playerEditPage.onPlayerSelected(bean);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRecordLoaded(Record record, PlayerBean player, MatchNameBean match) {
        this.record = record;
        this.matchNameBean = match;
        playerEditPage.initWithRecord(record, player);
    }
}
