package com.king.khcareer.record.k4;

/**
 * Created by Administrator on 2017/4/22 0022.
 */

public interface OnItemMenuListener {
    void onUpdateRecord(RecordItem record);
    void onDeleteRecord(RecordItem record);
    void onAllDetail(RecordItem record);
    void onListDetail(RecordItem record);

    void onItemClicked(RecordItem curRecordItem);
}
