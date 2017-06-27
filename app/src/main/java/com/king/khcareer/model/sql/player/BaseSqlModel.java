package com.king.khcareer.model.sql.player;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.player.bean.Record;

/**
 * 描述: 公用的一些数据库业务
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/27 13:51
 */
public class BaseSqlModel {

    protected Cursor getCursor(String sql, String[] args) {
        MySQLHelper sqlHelper = MySQLHelper.getInstance(MultiUserManager.getInstance().getCurrentUser());
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        return db.rawQuery(sql, args);
    }

    protected Record parseRecord(Cursor cursor) {
        Record record = new Record();
        record.setId(cursor.getInt(DatabaseStruct.COL_ID));
        record.setCompetitor(cursor.getString(DatabaseStruct.COL_competitor));
        record.setCptCountry(cursor.getString(DatabaseStruct.COL_competitor_country));
        record.setCourt(cursor.getString(DatabaseStruct.COL_court));
        record.setLongDate(Long.parseLong(cursor.getString(DatabaseStruct.COL_date_long)));
        record.setStrDate(cursor.getString(DatabaseStruct.COL_date_str));
        record.setWinner(cursor.getString(DatabaseStruct.COL_iswinner));
        record.setLevel(cursor.getString(DatabaseStruct.COL_level));
        record.setMatch(cursor.getString(DatabaseStruct.COL_match));
        record.setCity(cursor.getString(DatabaseStruct.COL_match_city));
        record.setMatchCountry(cursor.getString(DatabaseStruct.COL_match_country));
        record.setRank(Integer.parseInt(cursor.getString(DatabaseStruct.COL_rankp1)));
        record.setCptRank(Integer.parseInt(cursor.getString(DatabaseStruct.COL_rank)));
        record.setSeed(Integer.parseInt(cursor.getString(DatabaseStruct.COL_seedp1)));
        record.setCptSeed(Integer.parseInt(cursor.getString(DatabaseStruct.COL_seed)));
        record.setRegion(cursor.getString(DatabaseStruct.COL_region));
        record.setRound(cursor.getString(DatabaseStruct.COL_round));
        record.setScore(cursor.getString(DatabaseStruct.COL_score));
        return record;
    }

}
