package com.king.khcareer.player.h2hlist;

import com.king.khcareer.model.sql.player.bean.H2hParentBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/30 0030.
 */

public interface IH2hListView {
    void onDataLoaded(H2hListPageData data);

    void onSortFinished();

    void onFiltFinished(List<H2hParentBean> list);
}
