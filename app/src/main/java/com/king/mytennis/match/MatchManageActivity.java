package com.king.mytennis.match;

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
import com.king.mytennis.view.BaseActivity;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;

import java.util.HashMap;
import java.util.List;

/**
 * 描述: match_seq 表的DAO操作
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/20 11:40
 */
public class MatchManageActivity extends BaseActivity implements View.OnClickListener
    , MatchItemAdapter.OnMatchItemClickListener{

    public static final String KEY_START_MODE = "key_start_mode";
    public static final int START_MODE_SELECT = 1;

    private boolean isSelectMode;

    private ViewGroup groupNormal;
    private ViewGroup groupConfirm;

    private RecyclerView rvList;
    private MatchItemAdapter matchItemAdapter;

    private List<MatchNameBean> matchList;

    private MatchEditDialog matchEditDialog;

    // 编辑模式
    private boolean isEditMode;
    // 删除模式
    private boolean isDeleteMode;

    private MatchNameBean mEditBean;
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

        ((TextView) findViewById(R.id.view7_actionbar_title)).setText(getString(R.string.match_manage_title));

        findViewById(R.id.view7_actionbar_add).setOnClickListener(this);
        findViewById(R.id.view7_actionbar_edit).setOnClickListener(this);
        findViewById(R.id.view7_actionbar_delete).setOnClickListener(this);
        findViewById(R.id.view7_actionbar_done).setOnClickListener(this);
        findViewById(R.id.view7_actionbar_close).setOnClickListener(this);

        loadDatas();
    }

    private void loadDatas() {
        pubDataProvider = new PubDataProvider();
        matchList = pubDataProvider.getMatchList();
        matchItemAdapter = new MatchItemAdapter(matchList);
        matchItemAdapter.setOnMatchItemClickListener(this);

        rvList.setAdapter(matchItemAdapter);
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
                matchItemAdapter.setSelectMode(true);
                matchItemAdapter.notifyDataSetChanged();
                break;
            case R.id.view7_actionbar_done:
                if (isEditMode) {

                }
                else if (isDeleteMode) {
                    deleteMatchItems();
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
                matchItemAdapter.setSelectMode(false);
                matchItemAdapter.notifyDataSetChanged();
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
    public void onMatchItemClick(int position) {
        mEditBean = matchList.get(position);
        if (isEditMode) {
            openMatchEditDialog();
        }
        else {
            if (isSelectMode) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent();
                bundle.putString("name", mEditBean.getName());
                bundle.putString("country", mEditBean.getMatchBean().getCountry());
                bundle.putString("level", mEditBean.getMatchBean().getLevel());
                bundle.putString("court", mEditBean.getMatchBean().getCourt());
                bundle.putString("region", mEditBean.getMatchBean().getRegion());
                bundle.putString("city", mEditBean.getMatchBean().getCity());
                bundle.putInt("month", mEditBean.getMatchBean().getMonth());
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    private void openMatchEditDialog() {
        if (matchEditDialog == null) {
            matchEditDialog = new MatchEditDialog(this, new CustomDialog.OnCustomDialogActionListener() {
                @Override
                public boolean onSave(Object object) {
                    MatchNameBean bean = (MatchNameBean) object;
                    if (mEditBean == null) {
                        mEditBean = new MatchNameBean();
                    }
                    else {
                        bean.getMatchBean().setId(mEditBean.getMatchId());
                    }
                    mEditBean.setName(bean.getName());
                    mEditBean.setMatchBean(bean.getMatchBean());
                    if (mEditBean.getId() == 0) {
                        pubDataProvider.insertMatch(bean);
                    }
                    else {
                        pubDataProvider.updateMatch(mEditBean);
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
            matchEditDialog.setTitle("Edit match");
        }
        matchEditDialog.show();
    }

    private void refreshList() {
        matchList = pubDataProvider.getMatchList();
        matchItemAdapter.setList(matchList);
        matchItemAdapter.notifyDataSetChanged();
    }

    private void deleteMatchItems() {
        List<MatchNameBean> list = matchItemAdapter.getSelectedList();
        for (MatchNameBean bean:list) {
            pubDataProvider.deleteMatchName(bean);
        }
        refreshList();
    }
}
