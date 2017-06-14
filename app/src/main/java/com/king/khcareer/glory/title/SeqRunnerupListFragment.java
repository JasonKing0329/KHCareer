package com.king.khcareer.glory.title;

import android.support.v7.widget.RecyclerView;

import com.king.khcareer.model.sql.player.bean.Record;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/12 16:16
 */
public class SeqRunnerupListFragment extends AbsGloryListFragment {
    @Override
    protected RecyclerView.Adapter getListAdapter() {
        List<Record> recordList = gloryHolder.getGloryTitle().getRunnerUpList();
        SeqListAdapter adapter = new SeqListAdapter(recordList);
        adapter.setShowCompetitor(true);
        adapter.setOnRecordItemListener(new OnRecordItemListener() {
            @Override
            public void onClickRecord(Record record) {
                showGloryMatchDialog(record);
            }

        });
        return adapter;
    }
}
