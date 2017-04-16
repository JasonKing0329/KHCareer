package com.king.khcareer.record.editor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.king.khcareer.common.helper.BackgroundProvider;
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
public class RecordEditorActivity extends BaseActivity implements IEditorHolder, View.OnClickListener {

    private final int REQUEST_CHANGE_MATCH = 101;
    private final int REQUEST_CHANGE_PLAYER = 102;

    private ViewGroup groupPlayer, groupMatch;
    private TextView nextPageView, previousPageView, doneView, continueView;

    private RelativeLayout bkLayout;
    private TextView titleView;

    private PlayerEditPage playerEditPage;
    private MatchEditPage matchEditPage;

    @Override
    protected void onDestroy() {
        MatchCache.clear();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_record_editor);

        initParentView();

        showPlayerView();

        super.onCreate(savedInstanceState);
    }

    private void initParentView() {
        groupMatch = (ViewGroup) findViewById(R.id.editor_layout_match);
        groupPlayer = (ViewGroup) findViewById(R.id.editor_layout_player);
        titleView = (TextView) findViewById(R.id.editor_title);
        nextPageView = (TextView) findViewById(R.id.next_indicator);
        previousPageView = (TextView) findViewById(R.id.previous_indicator);
        doneView = (TextView) findViewById(R.id.done_indicator);
        continueView = (TextView) findViewById(R.id.continue_indicator);
        bkLayout = (RelativeLayout) findViewById(R.id.layout_editor);

        Bitmap bitmap = new BackgroundProvider().loadBackgound();
        if (bitmap == null) {
            bkLayout.setBackground(null);
        }
        else {
            bkLayout.setBackground(new BitmapDrawable(bitmap));
        }

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
        }
        groupPlayer.setVisibility(View.GONE);
        groupMatch.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v == nextPageView) {
            titleView.setText(getResources().getString(R.string.match_infor));
            previousPageView.setVisibility(View.VISIBLE);
            nextPageView.setVisibility(View.GONE);
            doneView.setVisibility(View.VISIBLE);
            showMatchView();
        }
        else if (v == previousPageView) {
            titleView.setText(getResources().getString(R.string.player_infor));
            previousPageView.setVisibility(View.GONE);
            nextPageView.setVisibility(View.VISIBLE);
            doneView.setVisibility(View.GONE);
            showPlayerView();
        }
        else if (v == doneView) {
            if (continueView.getVisibility() == View.GONE) {
                if (insert()) {
                    Toast.makeText(this, R.string.insert_done, Toast.LENGTH_LONG).show();
                    continueView.setVisibility(View.VISIBLE);
                }
            }
            else {
                finish();
            }
        }
        else if (v == continueView) {
            continueInsert();
        }
    }

    private void continueInsert() {
        continueView.setVisibility(View.GONE);
        titleView.setText(getResources().getString(R.string.player_infor));
        previousPageView.setVisibility(View.GONE);
        nextPageView.setVisibility(View.VISIBLE);
        doneView.setVisibility(View.GONE);
        playerEditPage.reset();
        matchEditPage.reset();
        showPlayerView();
    }

    /**
     * 执行添加
     * @return
     */
    private boolean insert() {

        Record record=new Record();
        String errorMsg = playerEditPage.fillRecord(record);
        if (errorMsg == null) {
            errorMsg = matchEditPage.fillRecord(record);
        }
        if (errorMsg != null) {
            Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            return false;
        }
        RecordService service = new RecordService();
        return service.insert(record);
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

}
