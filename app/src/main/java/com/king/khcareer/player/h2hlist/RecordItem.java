package com.king.khcareer.player.h2hlist;

import com.king.khcareer.model.sql.player.bean.Record;

/**
 * Created by Administrator on 2017/4/30 0030.
 */

public class RecordItem {
    private int headerPosition;
    private int itemPosition;

    private Record record;

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
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
