package com.king.khcareer.player.h2hlist;

/**
 * Created by Administrator on 2017/4/30 0030.
 */

public interface IH2hListView {
    void onDataLoaded(H2hListPageData data);

    void onSortFinished();

    void onFilterFinished();
}
