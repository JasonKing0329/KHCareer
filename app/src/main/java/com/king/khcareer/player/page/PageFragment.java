package com.king.khcareer.player.page;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;

import com.king.khcareer.base.BaseFragment;
import com.king.khcareer.base.CustomDialog;
import com.king.khcareer.base.IFragmentHolder;
import com.king.khcareer.common.multiuser.MultiUser;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.match.GloryMatchDialog;
import com.king.khcareer.match.page.MatchPageActivity;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.mytennis.glory.GloryController;
import com.king.mytennis.view.R;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 描述: fragment to present records of competitor
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/20 16:05
 */
public class PageFragment extends BaseFragment implements IPageCallback {

    private static final String KEY_TAB_NAME = "tab_name";
    private static final String KEY_USER_ID = "user_id";

    @BindView(R.id.rv_records)
    RecyclerView rvRecords;

    private PageRecordAdapter adapter;

    private IPageHolder holder;

    public static PageFragment newInstance(String tabName, String userId) {
        Bundle args = new Bundle();
        args.putString(KEY_TAB_NAME, tabName);
        args.putString(KEY_USER_ID, userId);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void onBindHolder(IFragmentHolder context) {
        holder = (IPageHolder) context;
    }

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_player_page;
    }

    @Override
    protected void onCreate(View view) {
        ButterKnife.bind(this, view);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvRecords.setLayoutManager(manager);

        String tabName = getArguments().getString(KEY_TAB_NAME);
        holder.getPresenter().createRecords(tabName, this);
    }

    @Override
    public void onDataLoaded(List<Object> list) {
        final String userId = getArguments().getString(KEY_USER_ID);
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
            public void onClickRecord(final Record record) {

                GloryMatchDialog dialog = new GloryMatchDialog(getActivity(), new CustomDialog.OnCustomDialogActionListener() {

                    @Override
                    public boolean onSave(Object object) {
                        return false;
                    }

                    @Override
                    public void onLoadData(HashMap<String, Object> data) {
                        List<Record> list = new GloryController().loadMatchRecord(record.getMatch(), record.getStrDate(), userId);
                        data.put(CustomDialog.OnCustomDialogActionListener.DATA_TYPE, list);
                        data.put(GloryMatchDialog.USER_ID, userId);
                    }

                    @Override
                    public boolean onCancel() {
                        return false;
                    }
                });
                dialog.enableItemLongClick();
                dialog.show();
            }

            @Override
            public void onLongClickRecord(View view, Record record) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), MatchPageActivity.class);
                intent.putExtra(MatchPageActivity.KEY_MATCH_NAME, record.getMatch());
                intent.putExtra(MatchPageActivity.KEY_USER_ID, getArguments().getString(KEY_USER_ID));
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity()
                        , Pair.create(view.findViewById(R.id.iv_match),getString(R.string.anim_match_page_head)));
                startActivity(intent, transitionActivityOptions.toBundle());
            }
        });
        rvRecords.setAdapter(adapter);
    }
}
