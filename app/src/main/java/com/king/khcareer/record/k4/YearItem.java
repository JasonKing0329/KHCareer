package com.king.khcareer.record.k4;

import com.zaihuishou.expandablerecycleradapter.model.ExpandableListItem;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/21 16:11
 */
public class YearItem implements ExpandableListItem {

    private String year;
    private List<HeaderItem> list;
    public boolean mExpanded = false;

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    public List<HeaderItem> getChildItemList() {
        return list;
    }

    public void setChildItemList(List<HeaderItem> list) {
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
