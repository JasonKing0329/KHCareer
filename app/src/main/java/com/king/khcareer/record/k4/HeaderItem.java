package com.king.khcareer.record.k4;

import com.king.khcareer.model.sql.player.bean.Record;
import com.zaihuishou.expandablerecycleradapter.model.ExpandableListItem;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/21 16:11
 */
public class HeaderItem implements ExpandableListItem {

    private Record record;
    private List<RecordItem> list;
    public boolean mExpanded = false;

    private int yearPosition;

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
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

    public int getYearPosition() {
        return yearPosition;
    }

    public void setYearPosition(int yearPosition) {
        this.yearPosition = yearPosition;
    }
}
