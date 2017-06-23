package com.king.khcareer.model.sql.player;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.player.bean.MatchResultBean;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.player.bean.KeyValueCountBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.level;
import static android.R.id.list;

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
    public List<KeyValueCountBean> getTitleCountByLevel(boolean isCurrentYear, boolean isWinner) {
        List<KeyValueCountBean> list = new ArrayList<>();
        StringBuffer buffer = new StringBuffer("SELECT level AS key,count(*) as value FROM record WHERE round='Final'");
        if (isWinner) {
            buffer.append(" AND iswinner='_user'");
        }
        else {
            buffer.append(" AND iswinner!='_user'");
        }
        if (isCurrentYear) {
            buffer.append(" AND date_str LIKE '").append(Calendar.getInstance().get(Calendar.YEAR)).append("%'");
        }
        buffer.append(" GROUP BY level");
        Cursor cursor = getCursor(buffer.toString(), null);
        while (cursor.moveToNext()) {
            KeyValueCountBean bean = parseKeyValueCount(cursor);
            list.add(bean);
        }

        return list;
    }

    /**
     * count title by match court
     * @return
     */
    public List<KeyValueCountBean> getTitleCountByCourt(boolean isCurrentYear, boolean isWinner) {
        List<KeyValueCountBean> list = new ArrayList<>();
        StringBuffer buffer = new StringBuffer("SELECT court AS key,count(*) as value FROM record WHERE round='Final'");
        if (isWinner) {
            buffer.append(" AND iswinner='_user'");
        }
        else {
            buffer.append(" AND iswinner!='_user'");
        }
        if (isCurrentYear) {
            buffer.append(" AND date_str LIKE '").append(Calendar.getInstance().get(Calendar.YEAR)).append("%'");
        }
        buffer.append(" GROUP BY court");
        Cursor cursor = getCursor(buffer.toString(), null);
        while (cursor.moveToNext()) {
            KeyValueCountBean bean = parseKeyValueCount(cursor);
            list.add(bean);
        }

        return list;
    }

    /**
     * count title by match year
     * @return
     */
    public List<KeyValueCountBean> getTitleCountByYear(boolean isCurrentYear, boolean isWinner) {
        List<KeyValueCountBean> list = new ArrayList<>();
        StringBuffer buffer = new StringBuffer("SELECT substr(date_str, 1, 4) AS key,count(*) as value FROM record WHERE round='Final'");
        if (isWinner) {
            buffer.append(" AND iswinner='_user'");
        }
        else {
            buffer.append(" AND iswinner!='_user'");
        }
        if (isCurrentYear) {
            buffer.append(" AND date_str LIKE '").append(Calendar.getInstance().get(Calendar.YEAR)).append("%'");
        }
        buffer.append(" GROUP BY substr(date_str, 1, 4)");
        Cursor cursor = getCursor(buffer.toString(), null);
        while (cursor.moveToNext()) {
            KeyValueCountBean bean = parseKeyValueCount(cursor);
            list.add(bean);
        }

        return list;
    }

    /**
     * count gs win lose
     * @return
     */
    public Map<String, Integer[]> getGsWinLose() {
        Map<String, Integer[]> map = new HashMap<>();
        String sql = "select match, sum(case when iswinner='_user' then 1 else 0 end) as win " +
                ", sum(case when iswinner='_user' then 0 else 1 end) as lose " +
                "from record where level='Grand Slam' group by match";
        Cursor cursor = getCursor(sql, null);
        while (cursor.moveToNext()) {
            String key = cursor.getString(0);
            Integer[] count = new Integer[2];
            count[0] = cursor.getInt(1);
            count[1] = cursor.getInt(2);
            map.put(key, count);
        }

        return map;
    }

    /**
     * count gs win lose
     * @return
     */
    public Integer[] getGsCount(boolean isThisYear) {
        String sql = "select sum(case when iswinner='_user' then 1 else 0 end) as win " +
                ", sum(case when iswinner='_user' then 0 else 1 end) as lose from record where level='Grand Slam'";
        if (isThisYear) {
            sql = new StringBuffer(sql).append(" AND date_str LIKE '").append(Calendar.getInstance().get(Calendar.YEAR)).append("%'").toString();
        }
        Integer[] count = new Integer[2];
        Cursor cursor = getCursor(sql, null);
        if (cursor.moveToNext()) {
            count[0] = cursor.getInt(0);
            count[1] = cursor.getInt(1);
        }

        return count;
    }

    /**
     * count atp1000 win lose
     * @return
     */
    public Integer[] getAtp1000Count(boolean isThisYear) {
        String sql = "select sum(case when iswinner='_user' then 1 else 0 end) as win " +
                ", sum(case when iswinner='_user' then 0 else 1 end) as lose from record where level='ATP1000'";
        if (isThisYear) {
            sql = new StringBuffer(sql).append(" AND date_str LIKE '").append(Calendar.getInstance().get(Calendar.YEAR)).append("%'").toString();
        }
        Integer[] count = new Integer[2];
        Cursor cursor = getCursor(sql, null);
        if (cursor.moveToNext()) {
            count[0] = cursor.getInt(0);
            count[1] = cursor.getInt(1);
        }

        return count;
    }

    /**
     * 获取第N条的记录（N按照factor从0开始叠加）
     * @param factor
     * @param isWinner
     * @return
     */
    public List<Record> getTargetRecords(int factor, boolean isWinner) {

        int total = getMatchNumber(isWinner, false);
        int count = 0;
        List<Record> list = new ArrayList<>();
        count += (factor - 1);
        String sql;
        while (count < total) {
            if (isWinner) {
                sql = "SELECT * FROM record WHERE iswinner='_user' LIMIT " + count + ",1";
            }
            else {
                sql = "SELECT * FROM record LIMIT " + count + ",1";
            }
            Cursor cursor = getCursor(sql, null);
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

    public int getMatchNumber(boolean isWinner, boolean isThisYear) {
        String sql = "SELECT COUNT(*) FROM record WHERE 1=1";
        if (isWinner) {
            sql = sql + " AND iswinner='_user'";
        }
        if (isThisYear) {
            sql = sql + " AND date_str LIKE '" + Calendar.getInstance().get(Calendar.YEAR) + "%'";
        }
        Cursor cursor = getCursor(sql, null);
        cursor.moveToNext();
        int total = cursor.getInt(0);
        return total;
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
