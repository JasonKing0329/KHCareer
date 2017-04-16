package com.king.khcareer.rank;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.king.khcareer.model.sql.player.interfc.DatabaseAccess;
import com.king.khcareer.model.sql.player.DatabaseStruct;
import com.king.khcareer.model.sql.player.SQLiteDB;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/22 19:14
 */
public class RankFinalDao {

    private DatabaseAccess sqLite;

    public RankFinalDao() {
        sqLite = new SQLiteDB();
    }

    /**
     * 按year升序排列
     * @return
     */
    public List<RankFinalBean> queryRanks() {

        List<RankFinalBean> list = new ArrayList<>();
        SQLiteOpenHelper helper = sqLite.getSQLHelper();
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            Cursor cursor = db.query(
                    DatabaseStruct.TABLE_RANK_FINAL, DatabaseStruct.TTABLE_RANK_FINAL_COL
                    , null, null, null, null, "_year ASC");
            while (cursor.moveToNext()) {
                if (list == null) {
                    list = new ArrayList<>();
                }
                RankFinalBean rank = adaptRank(cursor);
                list.add(rank);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public RankFinalBean queryRankByYear(int year) {

        SQLiteOpenHelper helper = sqLite.getSQLHelper();
        SQLiteDatabase db = helper.getReadableDatabase();
        try {
            Cursor cursor = db.query(
                    DatabaseStruct.TABLE_RANK_FINAL, DatabaseStruct.TTABLE_RANK_FINAL_COL
                    , "year=?", new String[]{String.valueOf(year)}, null, null, null);
            if (cursor.moveToNext()) {
                RankFinalBean rank = adaptRank(cursor);
                return rank;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private RankFinalBean adaptRank(Cursor cursor) {
        RankFinalBean bean = new RankFinalBean();
        bean.setId(cursor.getInt(0));
        bean.setYear(cursor.getInt(1));
        bean.setRank(cursor.getInt(2));
        return bean;
    }

    public void addRank(RankFinalBean bean) {
        String[] cols = new String[]{
                "_year", "_rank"
        };
        String[] values = new String[2];
        values[0] = String.valueOf(bean.getYear());
        values[1] = String.valueOf(bean.getRank());
        sqLite.insert(DatabaseStruct.TABLE_RANK_FINAL, cols, values);
    }

    public void updateRank(RankFinalBean bean) {
        String[] values = new String[3];
        values[0] = String.valueOf(bean.getId());
        values[1] = String.valueOf(bean.getYear());
        values[2] = String.valueOf(bean.getRank());
        sqLite.update(DatabaseStruct.TABLE_RANK_FINAL, "_id=?"
                , new String[]{"" + bean.getId()}, DatabaseStruct.TTABLE_RANK_FINAL_COL, values);
    }

    public void deleteRank(RankFinalBean bean) {
        String[] values = new String[2];
        values[0] = String.valueOf(bean.getYear());
        values[1] = String.valueOf(bean.getRank());
        sqLite.delete(DatabaseStruct.TABLE_RANK_FINAL, "_id=?", new String[]{"" + bean.getId()});
    }

    public void close() {
        sqLite.closeHelper();
    }
}
