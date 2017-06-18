package com.king.khcareer.model.sql.player;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.player.bean.MatchResultBean;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.player.bean.KeyValueCountBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017/6/18 0018.
 */

public class GloryModel {

    public List<MatchResultBean> getGsResultList() {
        List<MatchResultBean> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DatabaseStruct.VIEW_GS_RESULT;
        Cursor cursor = getCursor(sql, null);
        while (cursor.moveToNext()) {
            MatchResultBean bean = parseMatchResult(cursor);
            list.add(bean);
        }
        
        return list;
    }

    public List<MatchResultBean> getMasterResultList() {
        List<MatchResultBean> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DatabaseStruct.VIEW_MASTER_RESULT;
        Cursor cursor = getCursor(sql, null);
        while (cursor.moveToNext()) {
            MatchResultBean bean = parseMatchResult(cursor);
            list.add(bean);
        }
        
        return list;
    }

    public List<Record> getChampionRecords() {
        List<Record> list = new ArrayList<>();
        String sql = "SELECT * FROM record WHERE round='Final' AND iswinner='_user' ORDER BY id DESC";
        Cursor cursor = getCursor(sql, null);
        while (cursor.moveToNext()) {
            Record bean = parseRecord(cursor);
            list.add(bean);
        }
        
        return list;
    }

    public List<Record> getRunnerupRecords() {
        List<Record> list = new ArrayList<>();
        String sql = "SELECT * FROM record WHERE round='Final' AND iswinner!='_user' ORDER BY id DESC";
        Cursor cursor = getCursor(sql, null);
        while (cursor.moveToNext()) {
            Record bean = parseRecord(cursor);
            list.add(bean);
        }

        return list;
    }

    /**
     * count title by match level
     * @return
     */
    public List<KeyValueCountBean> getTitleCountByLevel(boolean isCurrentYear) {
        List<KeyValueCountBean> list = new ArrayList<>();
        String sql;
        if (isCurrentYear) {
            sql = "SELECT level AS key,count(*) as value FROM record WHERE round='Final' AND iswinner='_user' AND date_str LIKE '" + Calendar.getInstance().get(Calendar.YEAR) + "%' GROUP BY level";
        }
        else {
            sql = "SELECT level AS key,count(*) as value FROM record WHERE round='Final' AND iswinner='_user' GROUP BY level";
        }
        Cursor cursor = getCursor(sql, null);
        while (cursor.moveToNext()) {
            KeyValueCountBean bean = parseKeyValueCount(cursor);
            list.add(bean);
        }

        return list;
    }

    /**
     * 获取第N条的记录（N按照factor从0开始叠加）
     * @param factor
     * @param isWinner
     * @return
     */
    public List<Record> getTargetRecords(int factor, boolean isWinner) {

        String sql = "SELECT COUNT(*) FROM record";
        if (isWinner) {
            sql = sql + " WHERE iswinner='_user'";
        }
        Cursor cursor = getCursor(sql, null);
        cursor.moveToNext();
        int total = cursor.getInt(0);
        int count = 0;
        List<Record> list = new ArrayList<>();
        count += (factor - 1);
        while (count < total) {
            if (isWinner) {
                sql = "SELECT * FROM record LIMIT " + count + ",1";
            }
            else {
                sql = "SELECT * FROM record WHERE iswinner='_user' LIMIT " + count + ",1";
            }
            cursor = getCursor(sql, null);
            while (cursor.moveToNext()) {
                Record bean = parseRecord(cursor);
                list.add(bean);
            }
            count += factor;
        }
        return list;
    }

    public List<Record> loadMatchRecord(String match, String date) {
        List<Record> list = new ArrayList<>();
        String[] args = new String[] {match, date};
        String sql = "SELECT * FROM record WHERE match=? AND date_str=? ORDER BY id DESC";
        Cursor cursor = getCursor(sql, args);
        while (cursor.moveToNext()) {
            Record bean = parseRecord(cursor);
            list.add(bean);
        }

        return list;
    }

    private MatchResultBean parseMatchResult(Cursor cursor) {
        MatchResultBean bean = new MatchResultBean();
        bean.setMatch(cursor.getString(cursor.getColumnIndex("match")));
        bean.setCourt(cursor.getString(cursor.getColumnIndex("court")));
        bean.setDate(cursor.getString(cursor.getColumnIndex("date_str")));
        bean.setResult(cursor.getString(cursor.getColumnIndex("result")));
        return bean;
    }

    private Record parseRecord(Cursor cursor) {
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

    private KeyValueCountBean parseKeyValueCount(Cursor cursor) {
        KeyValueCountBean bean = new KeyValueCountBean();
        bean.setKey(cursor.getString(cursor.getColumnIndex("key")));
        bean.setValue(cursor.getInt(cursor.getColumnIndex("value")));
        return bean;
    }

    private Cursor getCursor(String sql, String[] args) {
        MySQLHelper sqlHelper = MySQLHelper.getInstance(MultiUserManager.getInstance().getCurrentUser());
        SQLiteDatabase db = sqlHelper.getReadableDatabase();
        return db.rawQuery(sql, args);
    }
}
