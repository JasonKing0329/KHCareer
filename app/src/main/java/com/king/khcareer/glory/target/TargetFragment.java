package com.king.khcareer.glory.target;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.glory.BaseGloryPageFragment;
import com.king.khcareer.glory.title.OnRecordItemListener;
import com.king.khcareer.glory.title.SeqListAdapter;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.settings.SettingProperty;
import com.king.mytennis.view.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/19 10:09
 */
public class TargetFragment extends BaseGloryPageFragment implements OnRecordItemListener {

    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_win)
    TextView tvWin;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    Unbinder unbinder;

    private SeqListAdapter adapter;

    @Override
    protected int getContentLayoutRes() {
        return R.layout.fragment_glory_target;
    }

    @Override
    protected void onCreate(View view) {
        unbinder = ButterKnife.bind(this, view);
        tvAll.setSelected(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(manager);

        tvAll.setText("All(" + gloryHolder.getGloryTitle().getCareerMatch() + ")");
        tvWin.setText("Win(" + gloryHolder.getGloryTitle().getCareerWin() + ")");
        if (SettingProperty.isGloryTargetWin(getActivity())) {
            tvWin.setSelected(true);
            tvAll.setSelected(false);
            initList(gloryHolder.getGloryTitle().getTargetWinList());
        }
        else {
            tvWin.setSelected(false);
            tvAll.setSelected(true);
            initList(gloryHolder.getGloryTitle().getTargetList());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_all, R.id.tv_win})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_all:
                SettingProperty.setGloryTargetWin(getActivity(), false);
                tvAll.setSelected(true);
                tvWin.setSelected(false);

                refreshList(gloryHolder.getGloryTitle().getTargetList());
                break;
            case R.id.tv_win:
                SettingProperty.setGloryTargetWin(getActivity(), true);
                tvAll.setSelected(false);
                tvWin.setSelected(true);

                refreshList(gloryHolder.getGloryTitle().getTargetWinList());
                break;
        }
    }

    /**
     * 倒序显示，不改变原数据顺序
     * @param targetList
     */
    private void initList(List<Record> targetList) {
        List<String> titleList = new ArrayList<>();
        for (int i = 0; i < targetList.size(); i ++) {
            titleList.add(Constants.GLORY_TARGET_FACTOR * (i + 1) + "th");
        }
        Collections.reverse(titleList);

        List<Record> newList = new ArrayList<>();
        newList.addAll(targetList);
        Collections.reverse(newList);

        adapter = new SeqListAdapter(newList);
        adapter.setShowTitle(true);
        adapter.setShowCompetitor(true);
        adapter.setHideSequence(true);
        adapter.setShowLose(true);
        adapter.setTitleList(titleList);
        adapter.setOnRecordItemListener(this);
        rvList.setAdapter(adapter);
    }

    private void refreshList(List<Record> targetList) {
        List<String> titleList = new ArrayList<>();
        for (int i = 0; i < targetList.size(); i ++) {
            titleList.add(Constants.GLORY_TARGET_FACTOR * (i + 1) + "th");
        }
        Collections.reverse(titleList);

        List<Record> newList = new ArrayList<>();
        newList.addAll(targetList);
        Collections.reverse(newList);

        adapter.setTitleList(titleList);
        adapter.setRecordList(newList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClickRecord(Record record) {
        showGloryMatchDialog(record);
    }
}
