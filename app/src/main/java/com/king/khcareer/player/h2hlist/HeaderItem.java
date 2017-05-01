package com.king.khcareer.player.h2hlist;

import com.zaihuishou.expandablerecycleradapter.model.ExpandableListItem;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/21 16:11
 */
public class HeaderItem implements ExpandableListItem {

    private H2hHeaderBean headerBean;
    private List<RecordItem> list;
    public boolean mExpanded = false;

    public H2hHeaderBean getHeader() {
        return headerBean;
    }

    public void setHeader(H2hHeaderBean record) {
        this.headerBean = record;
    }

    @Override
    public List<RecordItem> getChildItemList() {
        return list;
    }

    public void setChildItemList(List<RecordItem> list) {
        this.list = list;
    }

    @Override
    public boolean isExpanded() {
        return mExpanded;
    }

    @Override
    public void setExpanded(boolean isExpanded) {
        mExpanded = isExpanded;
    }
}
