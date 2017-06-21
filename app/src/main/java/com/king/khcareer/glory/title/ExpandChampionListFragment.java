package com.king.khcareer.glory.title;

import android.support.v7.widget.RecyclerView;

import com.king.khcareer.model.sql.player.bean.Record;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/12 16:17
 */
public class ExpandChampionListFragment extends AbsGloryListFragment {

    @Override
    protected RecyclerView.Adapter getListAdapter() {
        List<Record> recordList = gloryHolder.getGloryTitle().getChampionList();
        List<HeaderItem> headerList = gloryHolder.getPresenter().getHeaderList(recordList, groupMode);
        KeyExpandAdapter adapter = new KeyExpandAdapter(headerList, this, false, false, false);
        return adapter;
    }

}
