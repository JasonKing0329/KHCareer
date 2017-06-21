package com.king.khcareer.glory.title;

import com.zaihuishou.expandablerecycleradapter.model.ExpandableListItem;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/21 10:33
 */
public class HeaderItem implements ExpandableListItem {

    private HeaderBean headerBean;
    public boolean mExpanded = false;

    public HeaderBean getHeaderBean() {
        return headerBean;
    }

    public void setHeaderBean(HeaderBean headerBean) {
        this.headerBean = headerBean;
    }

    @Override
    public List<?> getChildItemList() {
        return headerBean.getItemList();
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
