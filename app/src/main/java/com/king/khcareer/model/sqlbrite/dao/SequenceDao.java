package com.king.khcareer.model.sqlbrite.dao;

import android.database.Cursor;

import com.king.khcareer.model.sqlbrite.table.TSequence;
import com.squareup.sqlbrite2.BriteDatabase;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/27 11:56
 */
public class SequenceDao {

    public int queryTableSequence(BriteDatabase db, String table) {
        String sql = "SELECT seq FROM %s WHERE %s=?";
        sql = String.format(sql, TSequence.TABLE_NAME, TSequence.COLUMN_NAME);
        String[] args = new String[]{table};
        Cursor cursor = null;
        try {
            cursor = db.query(sql, args);
            if (cursor.moveToNext()) {
                int seq = cursor.getInt(0);
                return seq;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }
}
