package com.king.mytennis.player;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.mytennis.pubdata.PubDataProvider;
import com.king.mytennis.pubdata.bean.MatchNameBean;
import com.king.mytennis.pubdata.bean.PlayerBean;
import com.king.mytennis.view.BaseActivity;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;
import com.king.mytennis.view.publicview.SideBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: _player 表的DAO操作
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/20 11:40
 */
public class PlayerManageActivity extends BaseActivity implements View.OnClickListener
    , PlayerItemAdapter.OnPlayerItemClickListener, SideBar.OnTouchingLetterChangedListener{

    public static final String KEY_START_MODE = "key_start_mode";
    public static final int START_MODE_SELECT = 1;

    // top 4 players are virtual players, forbid to modify
    public static final int FIXED_PLAYER = 4;

    private boolean isSelectMode;

    private ViewGroup groupNormal;
    private ViewGroup groupConfirm;

    private SideBar indexSideBar;
    private TextView indexPopupView;

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
    private PubDataProvider pubDataProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_match_manage);

        int mode = getIntent().getIntExtra(KEY_START_MODE, 0);
        if (mode == START_MODE_SELECT) {
            isSelectMode = true;
        }

        ImageView backView = (ImageView) findViewById(R.id.view7_actionbar_back);
        backView.setVisibility(View.VISIBLE);
        backView.setOnClickListener(this);
        groupConfirm = (ViewGroup) findViewById(R.id.view7_actionbar_action_confirm);
        groupNormal = (ViewGroup) findViewById(R.id.view7_actionbar_action_normal);
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

        loadDatas();
    }

    private void loadDatas() {
        pubDataProvider = new PubDataProvider();
        playerList = pubDataProvider.getPlayerList();
        playerItemAdapter = new PlayerItemAdapter(playerList);
        playerItemAdapter.setOnPlayerItemClickListener(this);

        rvList.setAdapter(playerItemAdapter);
        createIndex();
    }

    private void createIndex() {
        if (playerList == null) {
            return;
        }
        indexSideBar.clear();
        playerIndexMap = new HashMap<>();
        // player list查询出来已经是升序的
        for (int i = FIXED_PLAYER; i < playerList.size(); i ++) {
            char first = playerList.get(i).getNamePinyin().charAt(0);
            Integer index = playerIndexMap.get(first);
            if (index == null) {
                playerIndexMap.put(first, i);
                indexSideBar.addIndex(String.valueOf(first));
            }
        }
        indexSideBar.invalidate();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view7_actionbar_back:
                finish();
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
            if (position < FIXED_PLAYER) {
                return;
            }
            openMatchEditDialog();
        }
        else {
            if (isSelectMode) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent();
                bundle.putString("name", mEditBean.getNameChn());
                bundle.putString("country", mEditBean.getCountry());
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
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
                        pubDataProvider.insertPlayer(bean);
                    }
                    else {
                        pubDataProvider.updatePlayer(mEditBean);
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
        playerList = pubDataProvider.getPlayerList();
        playerItemAdapter.setList(playerList);
        playerItemAdapter.notifyDataSetChanged();

        createIndex();
    }

    private void deletePlayerItems() {
        List<PlayerBean> list = playerItemAdapter.getSelectedList();
        for (PlayerBean bean:list) {
            pubDataProvider.deletePlayer(bean);
        }
        refreshList();
    }

    @Override
    public void onTouchingLetterChanged(String s) {
        int selection = playerIndexMap.get(s.charAt(0));
        rvList.smoothScrollToPosition(selection);
    }
}
