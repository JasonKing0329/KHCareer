package com.king.mytennis.player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.mytennis.pubdata.PubDataProvider;
import com.king.mytennis.pubdata.bean.PlayerBean;
import com.king.mytennis.view.BaseActivity;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;
import com.king.mytennis.view.publicview.SideBar;
import com.king.mytennis.view.settings.SettingProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.king.mytennis.pubdata.PubDataProvider.VIRTUAL_PLAYER;

/**
 * 描述: _player 表的DAO操作
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/20 11:40
 */
public class PlayerManageActivity extends BaseActivity implements View.OnClickListener, IPlayerView
    , PlayerItemAdapter.OnPlayerItemClickListener, SideBar.OnTouchingLetterChangedListener{

    public static final String KEY_START_MODE = "key_start_mode";
    public static final int START_MODE_SELECT = 1;

    private boolean isSelectMode;

    private ViewGroup groupNormal;
    private ViewGroup groupConfirm;

    private SideBar indexSideBar;
    private TextView indexPopupView;

    private ImageView ivSort;
    private PopupMenu popSort;

    private ImageView ivChart;

    private RecyclerView rvList;
    private PlayerItemAdapter playerItemAdapter;

    private List<PlayerBean> playerList;
    private Map<Character, Integer> playerIndexMap;

    private PlayerEditDialog playerEditDialog;

    // 编辑模式
    private boolean isEditMode;
    // 删除模式
    private boolean isDeleteMode;

    private PlayerBean mEditBean;
    private PlayerPresenter mPresenter;

    private boolean isIndexCreated;

    private PlayerChartDialog chartDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_match_manage);

        int mode = getIntent().getIntExtra(KEY_START_MODE, 0);
        if (mode == START_MODE_SELECT) {
            isSelectMode = true;
        }

        mPresenter = new PlayerPresenter(this);

        findViewById(R.id.view7_actionbar_menu).setVisibility(View.GONE);
        ImageView backView = (ImageView) findViewById(R.id.view7_actionbar_back);
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(this);
        groupConfirm = (ViewGroup) findViewById(R.id.view7_actionbar_action_confirm);
        groupNormal = (ViewGroup) findViewById(R.id.view7_actionbar_action_normal);
        ivSort = (ImageView) findViewById(R.id.view7_actionbar_sort);
        ivChart = (ImageView) findViewById(R.id.view7_actionbar_chart);
        groupNormal.setVisibility(View.VISIBLE);
        findViewById(R.id.view7_actionbar_edit_group).setVisibility(View.VISIBLE);

        rvList = (RecyclerView) findViewById(R.id.match_manage_list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(manager);
        rvList.setItemAnimator(new DefaultItemAnimator());

        ((TextView) findViewById(R.id.view7_actionbar_title)).setText(getString(R.string.player_manage_title));

        indexSideBar = (SideBar) findViewById(R.id.manage_sidebar);
        indexPopupView = (TextView) findViewById(R.id.manage_indexview_popup);
        indexSideBar.setVisibility(View.VISIBLE);
        indexSideBar.setOnTouchingLetterChangedListener(this);
        indexSideBar.setTextView(indexPopupView);

        findViewById(R.id.view7_actionbar_add).setOnClickListener(this);
        findViewById(R.id.view7_actionbar_edit).setOnClickListener(this);
        findViewById(R.id.view7_actionbar_delete).setOnClickListener(this);
        findViewById(R.id.view7_actionbar_done).setOnClickListener(this);
        findViewById(R.id.view7_actionbar_close).setOnClickListener(this);
        ivSort.setOnClickListener(this);
        ivChart.setOnClickListener(this);

        loadDatas();
    }

    private void loadDatas() {
        showProgress(null);
        mPresenter.loadPlayerList(this);
    }

    @Override
    public void onLoadPlayerList(List<PlayerBean> list) {
        playerList = list;
        if (playerItemAdapter == null) {
            playerItemAdapter = new PlayerItemAdapter(playerList);
            playerItemAdapter.setOnPlayerItemClickListener(this);

            rvList.setAdapter(playerItemAdapter);
        }
        else {
            playerItemAdapter.setList(playerList);
            playerItemAdapter.notifyDataSetChanged();
        }

        createIndex();

        showChartIcon();

        dismissProgress();
    }

    private void showChartIcon() {
        if (SettingProperty.getPlayerSortMode(this) == SettingProperty.VALUE_SORT_PLAYER_CONSTELLATION) {
            ivChart.setVisibility(View.VISIBLE);
        }
        else {
            ivChart.setVisibility(View.VISIBLE);
        }
    }

    private void createIndex() {
        int sortMode = SettingProperty.getPlayerSortMode(this);
        if (sortMode == SettingProperty.VALUE_SORT_PLAYER_NAME || sortMode == SettingProperty.VALUE_SORT_PLAYER_NAME_ENG) {
            if (playerList == null) {
                return;
            }
            indexSideBar.clear();
            playerIndexMap = new HashMap<>();
            // player list查询出来已经是升序的
            for (int i = PubDataProvider.VIRTUAL_PLAYER; i < playerList.size(); i ++) {
                String targetText;
                if (sortMode == SettingProperty.VALUE_SORT_PLAYER_NAME) {
                    targetText = playerList.get(i).getNamePinyin();
                }
                else {
                    targetText = playerList.get(i).getNameEng();
                    // 没有录入英文名的排在最后
                    if (TextUtils.isEmpty(targetText)) {
                        targetText = "ZZZZZZZZ";
                    }
                }
                char first = targetText.charAt(0);
                Integer index = playerIndexMap.get(first);
                if (index == null) {
                    playerIndexMap.put(first, i);
                    indexSideBar.addIndex(String.valueOf(first));
                }
            }
            indexSideBar.setVisibility(View.VISIBLE);
            indexSideBar.invalidate();
            isIndexCreated = true;
        }
        else {
            indexSideBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view7_actionbar_back:
                finish();
                break;
            case R.id.view7_actionbar_sort:
                showSortPopup();
                break;
            case R.id.view7_actionbar_chart:
                showChartDialog();
                break;
            case R.id.view7_actionbar_add:
                mEditBean = null;
                openMatchEditDialog();
                break;
            case R.id.view7_actionbar_edit:
                isEditMode = true;
                updateActionbarStatus(true);
                break;
            case R.id.view7_actionbar_delete:
                isDeleteMode = true;
                updateActionbarStatus(true);
                playerItemAdapter.setSelectMode(true);
                playerItemAdapter.notifyDataSetChanged();
                break;
            case R.id.view7_actionbar_done:
                if (isEditMode) {

                }
                else if (isDeleteMode) {
                    deletePlayerItems();
                }
                updateActionbarStatus(false);
                break;
            case R.id.view7_actionbar_close:
                updateActionbarStatus(false);
                break;
        }
    }

    private void showChartDialog() {
        chartDialog = new PlayerChartDialog(this, new CustomDialog.OnCustomDialogActionListener() {
            @Override
            public boolean onSave(Object object) {
                return false;
            }

            @Override
            public boolean onCancel() {
                return false;
            }

            @Override
            public void onLoadData(HashMap<String, Object> data) {
                data.put("data", playerList);
            }
        });
        chartDialog.setTitle("Constellation");
        chartDialog.show();
    }

    private void showSortPopup() {
        if (popSort == null) {
            popSort = new PopupMenu(this, ivSort);
            popSort.getMenuInflater().inflate(R.menu.sort_player, popSort.getMenu());
            popSort.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    // 目前只有默认的汉语名称下支持index导航，其他排序模式下隐藏导航
                    showProgress(null);
                    switch (item.getItemId()) {
                        case R.id.menu_sort_name:
                            mPresenter.sortPlayer(PlayerManageActivity.this, SettingProperty.VALUE_SORT_PLAYER_NAME);
                            break;
                        case R.id.menu_sort_name_eng:
                            mPresenter.sortPlayer(PlayerManageActivity.this, SettingProperty.VALUE_SORT_PLAYER_NAME_ENG);
                            break;
                        case R.id.menu_sort_country:
                            mPresenter.sortPlayer(PlayerManageActivity.this, SettingProperty.VALUE_SORT_PLAYER_COUNTRY);
                            break;
                        case R.id.menu_sort_age:
                            mPresenter.sortPlayer(PlayerManageActivity.this, SettingProperty.VALUE_SORT_PLAYER_AGE);
                            break;
                        case R.id.menu_sort_constellation:
                            mPresenter.sortPlayer(PlayerManageActivity.this, SettingProperty.VALUE_SORT_PLAYER_CONSTELLATION);
                            break;
                    }
                    return false;
                }
            });
        }
        popSort.show();
    }

    @Override
    public void onSortFinished() {
        playerItemAdapter.notifyDataSetChanged();
        createIndex();

        showChartIcon();

        dismissProgress();
    }

    public void updateActionbarStatus(boolean editMode) {
        if (editMode) {
            groupConfirm.setVisibility(View.VISIBLE);
            groupNormal.setVisibility(View.GONE);
        }
        else {
            groupConfirm.setVisibility(View.GONE);
            groupNormal.setVisibility(View.VISIBLE);
            if (isDeleteMode) {
                playerItemAdapter.setSelectMode(false);
                playerItemAdapter.notifyDataSetChanged();
            }
            isEditMode = false;
            isDeleteMode = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (isEditMode || isDeleteMode) {
            updateActionbarStatus(false);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onPlayerItemClick(int position) {
        // top 4 player is
        mEditBean = playerList.get(position);
        if (isEditMode) {
            if (position < VIRTUAL_PLAYER) {
                return;
            }
            openMatchEditDialog();
        }
        else {
            if (isSelectMode) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent();
                bundle.putString("name", mEditBean.getNameChn());
                bundle.putString("name_eng", mEditBean.getNameEng());
                bundle.putString("country", mEditBean.getCountry());
                bundle.putString("birthday", mEditBean.getBirthday());
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
            else {
                Intent intent = new Intent().setClass(this, PlayerCommonActivity.class);
                intent.putExtra(PlayerCommonActivity.KEY_PLAYER, mEditBean.getNameChn());
                startActivity(intent);
            }
        }
    }

    private void openMatchEditDialog() {
        if (playerEditDialog == null) {
            playerEditDialog = new PlayerEditDialog(this, new CustomDialog.OnCustomDialogActionListener() {
                @Override
                public boolean onSave(Object object) {
                    PlayerBean bean = (PlayerBean) object;
                    if (mEditBean == null) {
                        mEditBean = new PlayerBean();
                    }
                    mEditBean.setNameChn(bean.getNameChn());
                    mEditBean.setNamePinyin(bean.getNamePinyin());
                    mEditBean.setNameEng(bean.getNameEng());
                    mEditBean.setCountry(bean.getCountry());
                    mEditBean.setCity(bean.getCity());
                    mEditBean.setBirthday(bean.getBirthday());
                    if (mEditBean.getId() == 0) {
                        mPresenter.insertPlayer(bean);
                    }
                    else {
                        mPresenter.updatePlayer(mEditBean);
                    }
                    refreshList();
                    return true;
                }

                @Override
                public boolean onCancel() {
                    return false;
                }

                @Override
                public void onLoadData(HashMap<String, Object> data) {
                    data.put("bean", mEditBean);
                }
            });
            playerEditDialog.setTitle("Edit match");
        }
        playerEditDialog.show();
    }

    private void refreshList() {
        mPresenter.loadPlayerList(this);
    }

    private void deletePlayerItems() {
        List<PlayerBean> list = playerItemAdapter.getSelectedList();
        for (PlayerBean bean:list) {
            mPresenter.deletePlayer(bean);
        }
        refreshList();
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        int selection = playerIndexMap.get(s.charAt(0));
        rvList.smoothScrollToPosition(selection);
    }

}
