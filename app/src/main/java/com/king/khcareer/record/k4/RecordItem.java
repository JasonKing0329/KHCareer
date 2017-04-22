package com.king.khcareer.record.k4;

import com.king.khcareer.model.sql.player.bean.Record;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/21 16:31
 */
public class RecordItem {

    private int yearPosition;
    private int headerPosition;
    private int itemPosition;

    private Record record;

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public int getYearPosition() {
        return yearPosition;
    }

    public void setYearPosition(int yearPosition) {
        this.yearPosition = yearPosition;
    }

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
}
