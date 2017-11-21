package com.king.khcareer.match.page;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.king.khcareer.base.BaseActivity;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.image.glide.GlideOptions;
import com.king.khcareer.common.multiuser.MultiUser;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.player.page.PlayerPageActivity;
import com.king.khcareer.pubview.CircleImageView;
import com.king.mytennis.view.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/21 14:06
 */
public class MatchPageActivity extends BaseActivity implements IPageView {

    public static final String KEY_MATCH_NAME = "key_match_name";
    public static final String KEY_USER_ID = "key_user_id";

    @BindView(R.id.iv_match)
    ImageView ivMatch;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.tv_match)
    TextView tvMatch;
    @BindView(R.id.tv_winlose)
    TextView tvWinlose;
    @BindView(R.id.iv_match_thumb)
    CircleImageView ivMatchThumb;
    @BindView(R.id.tv_place)
    TextView tvPlace;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.fab_like)
    FloatingActionButton fabLike;
    @BindView(R.id.rv_records)
    RecyclerView rvRecords;

    private PagePresenter presenter;

    private PageRecordAdapter adapter;

    private String userId;

    @Override
    protected boolean applyCommonTheme() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_page);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        init();
    }

    private void init() {
        presenter = new PagePresenter(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_filterrable);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.dark_grey), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecords.setLayoutManager(manager);

        String match = getIntent().getStringExtra(KEY_MATCH_NAME);
        presenter.loadMatchInfo(match);

        userId = getIntent().getStringExtra(KEY_USER_ID);
        presenter.loadRecords(userId);
    }

    @Override
    public void showMatchInfo(String name, String country, String city, String level, String court) {
        tvMatch.setText(name);
        tvPlace.setText(country + "/" + city);
        tvLevel.setText(level + "/" + court);

        Glide.with(this)
                .load(ImageFactory.getMatchHeadPath(name, court))
                .apply(GlideOptions.getDefaultMatchOptions())
                .into(ivMatch);

        Glide.with(this)
                .load(ImageFactory.getMatchHeadPath(name, court))
                .apply(GlideOptions.getDefaultMatchOptions())
                .into(ivMatchThumb);
    }

    @Override
    public void showError(String msg) {
        showConfirmMessage(msg, null);
    }

    @Override
    public void onRecordsLoaded(List<Object> list, int win, int lose) {
        tvWinlose.setText(win + "胜" + lose + "负");
        MultiUser user;
        if (userId == null) {
            user = MultiUserManager.getInstance().getCurrentUser();
        }
        else {
            user = MultiUserManager.getInstance().getUser(userId);
        }
        adapter = new PageRecordAdapter(user, list);
        adapter.setOnItemClickListener(new PageRecordAdapter.OnItemClickListener() {
            @Override
            public void onClickRecord(Record record) {
                Intent intent = new Intent().setClass(MatchPageActivity.this, PlayerPageActivity.class);
                intent.putExtra(PlayerPageActivity.KEY_COMPETITOR_NAME, record.getCompetitor());
                startActivity(intent);
            }
        });
        rvRecords.setAdapter(adapter);
    }
}
