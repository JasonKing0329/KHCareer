package com.king.mytennis.match;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.king.mytennis.interfc.DatabaseAccess;
import com.king.mytennis.model.DatabaseStruct;
import com.king.mytennis.model.Record;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: sql事件均为mytennis_public数据库的数据操作
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/20 11:19
 */
public class MatchSqlModel {

    private DatabaseAccess sqlite;

    public MatchSqlModel(Context context) {
        sqlite = new MatchSqliteDB(context);
    }

    public List<MatchSeqBean> queryMatchSeqList() {

        List<MatchSeqBean> list = new ArrayList<>();
        SQLiteDatabase db = sqlite.getSQLHelper().getWritableDatabase();
        Cursor cursor=db.query(
                DatabaseStruct.TABLE_MATCH_SEQ, DatabaseStruct.TABLE_MATCH_SEQ_COL
                , null, null, null, null, "sequence");
        while (cursor.moveToNext()){
            MatchSeqBean bean = parseMatchBean(cursor);
            list.add(bean);
        }
        db.close();
        return list;
    }

    private MatchSeqBean parseMatchBean(Cursor cursor) {
        MatchSeqBean bean = new MatchSeqBean();
        bean.setId(cursor.getInt(0));
        bean.setName(cursor.getString(1));
        bean.setSequence(cursor.getInt(2));
        return bean;
    }

    public void insertMatchSeq(MatchSeqBean bean) {
        sqlite.insert(DatabaseStruct.TABLE_MATCH_SEQ, new String[]{"name", "sequence"}, new String[] {
                bean.getName(), String.valueOf(bean.getSequence())
        });

        // 插入后获取自增的新ID
        SQLiteDatabase db = sqlite.getSQLHelper().getWritableDatabase();
        Cursor cursor=db.query(
                DatabaseStruct.TABLE_SEQUENCE, new String[] {"seq"}
                , "name=?", new String[] {DatabaseStruct.TABLE_MATCH_SEQ}, null, null, null);
        if (cursor.moveToNext()){
            bean.setId(cursor.getInt(0));
        }
        db.close();
    }

    public void deleteMatchSeq(MatchSeqBean bean) {
        sqlite.delete(DatabaseStruct.TABLE_MATCH_SEQ, "_id=?", new String[] {
                String.valueOf(bean.getId())
        });
    }

    public void updateMatchSeq(MatchSeqBean bean) {
        sqlite.update(DatabaseStruct.TABLE_MATCH_SEQ, "_id=?", new String[] {String.valueOf(bean.getId())}, new String[]{"name", "sequence"}, new String[] {
                bean.getName(), String.valueOf(bean.getSequence())
        });
    }

    public MatchSeqBean getMatchSeqBeanByName(String name) {
        MatchSeqBean bean = null;
        SQLiteDatabase db = sqlite.getSQLHelper().getWritableDatabase();
        Cursor cursor=db.query(
                DatabaseStruct.TABLE_MATCH_SEQ, DatabaseStruct.TABLE_MATCH_SEQ_COL
                , "name=?", new String[]{name}, null, null, null);
        if (cursor.moveToNext()){
            bean = parseMatchBean(cursor);
        }
        db.close();
        return bean;
    }

}
