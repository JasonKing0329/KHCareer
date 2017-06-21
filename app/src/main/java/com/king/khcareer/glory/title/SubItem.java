package com.king.khcareer.glory.title;

import com.king.khcareer.model.sql.player.bean.Record;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/21 10:04
 */
public class SubItem {
    private int headerPosition;
    private int itemPosition;
    private int groupCount;

    private Record record;

    public int getHeaderPosition() {
        return headerPosition;
    }

    public void setHeaderPosition(int headerPosition) {
        this.headerPosition = headerPosition;
    }

    public int getItemPosition() {
        return itemPosition;
    }

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }
}
