package com.king.khcareer.model.sqlbrite.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.king.khcareer.model.sql.player.bean.MatchIdBean;
import com.king.khcareer.model.sql.pubdata.bean.MatchBean;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;
import com.king.khcareer.model.sqlbrite.table.TMatch;
import com.king.khcareer.model.sqlbrite.table.TMatchName;
import com.squareup.sqlbrite2.BriteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/27 11:06
 */
public class MatchDao {

    private BriteDatabase db;
    private SequenceDao sequenceDao;

    public MatchDao(BriteDatabase db) {
        this.db = db;
        sequenceDao = new SequenceDao();
    }

    /**
     * order by week
     * @param level
     * @return
     */
    public List<MatchNameBean> queryMatchList(String level) {

        List<MatchNameBean> list = new ArrayList<>();
        StringBuffer buffer = new StringBuffer("SELECT m.*, mn._id as %s, mn.name FROM ");
        buffer.append(TMatch.TABLE_NAME).append(" m, ").append(TMatchName.TABLE_NAME)
                .append(" mn WHERE m._id = mn.match_id");
        if (level != null) {
            buffer.append(" AND m.level=?");
        }
        buffer.append(" ORDER BY m.week ASC");

        String sql = String.format(buffer.toString(), TMatchName.COLUMN_UNION_ID);
        String[] args = new String[]{};
        if (level != null) {
            args = new String[]{level};
        }

        Cursor cursor = null;
        try {
            cursor = db.query(sql, args);
            while (cursor.moveToNext()) {
                list.add(TMatchName.parseFullMatchBean(cursor));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public void insertMatchBean(MatchBean bean) {
        ContentValues values = new ContentValues();
        values.put(TMatch.COLUMN_CITY, bean.getCity());
        values.put(TMatch.COLUMN_COUNTRY, bean.getCountry());
        values.put(TMatch.COLUMN_COURT, bean.getCourt());
        values.put(TMatch.COLUMN_REGION, bean.getRegion());
        values.put(TMatch.COLUMN_LEVEL, bean.getLevel());
        values.put(TMatch.COLUMN_MONTH, bean.getMonth());
        values.put(TMatch.COLUMN_WEEK, bean.getWeek());
        db.insert(TMatch.TABLE_NAME, values);
    }

    public void insertMatchNameBean(MatchNameBean bean) {
        ContentValues values = new ContentValues();
        values.put(TMatchName.COLUMN_MATCH_ID, bean.getMatchId());
        values.put(TMatchName.COLUMN_NAME, bean.getName());
        db.insert(TMatchName.TABLE_NAME, values);
    }

    public void updateMatchBean(MatchBean bean) {
        ContentValues values = new ContentValues();
        values.put(TMatch.COLUMN_CITY, bean.getCity());
        values.put(TMatch.COLUMN_COUNTRY, bean.getCountry());
        values.put(TMatch.COLUMN_COURT, bean.getCourt());
        values.put(TMatch.COLUMN_REGION, bean.getRegion());
        values.put(TMatch.COLUMN_LEVEL, bean.getLevel());
        values.put(TMatch.COLUMN_MONTH, bean.getMonth());
        values.put(TMatch.COLUMN_WEEK, bean.getWeek());
        db.update(TMatch.TABLE_NAME, values, TMatch.COLUMN_ID + "=?", new String[]{String.valueOf(bean.getId())});
    }

    public void updateMatchNameBean(MatchNameBean bean) {
        ContentValues values = new ContentValues();
        values.put(TMatchName.COLUMN_MATCH_ID, bean.getMatchId());
        values.put(TMatchName.COLUMN_NAME, bean.getName());
        db.update(TMatchName.TABLE_NAME, values, TMatchName.COLUMN_ID + "=?", new String[]{String.valueOf(bean.getId())});
    }

    public void deleteMatch(int matchId) {
        db.delete(TMatch.TABLE_NAME, TMatch.COLUMN_ID + "=?", new String[]{String.valueOf(matchId)});
    }

    public void deleteMatchName(int id) {
        db.delete(TMatchName.TABLE_NAME, TMatchName.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteMatchNameByMatchId(int matchId) {
        db.delete(TMatchName.TABLE_NAME, TMatchName.COLUMN_MATCH_ID + "=?", new String[]{String.valueOf(matchId)});
    }

    public int queryLastMatchBeanSequence() {
        return sequenceDao.queryTableSequence(db, TMatch.TABLE_NAME);
    }

    public int queryLastMatchNameBeanSequence() {
        return sequenceDao.queryTableSequence(db, TMatchName.TABLE_NAME);
    }

    public MatchNameBean queryMatchByName(String name) {
        String sql = "SELECT m.*, mn._id as %s, mn.name FROM " +  TMatch.TABLE_NAME + " m, "
                + TMatchName.TABLE_NAME + " mn WHERE m._id = mn.match_id AND mn.name=?";
        sql = String.format(sql, TMatchName.COLUMN_UNION_ID);
        String[] args = new String[]{name};

        Cursor cursor = null;
        try {
            cursor = db.query(sql, args);
            if (cursor.moveToNext()) {
                return TMatchName.parseFullMatchBean(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public List<MatchNameBean> queryMatchNameList(int matchId) {

        List<MatchNameBean> list = new ArrayList<>();
        String sql = "SELECT * FROM " +  TMatchName.TABLE_NAME + " WHERE match_id = ?";
        String[] args = new String[]{String.valueOf(matchId)};

        Cursor cursor = null;
        try {
            cursor = db.query(sql, args);
            while (cursor.moveToNext()) {
                list.add(TMatchName.parseMatchNameBean(cursor));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public Map<Integer, MatchIdBean> loadMasterIdMap() {
        Map<Integer, MatchIdBean> map = new HashMap<>();
        String sql = "select a._id, a.week, b.name from _match a, _match_name b where a._id=b.match_id and a.level = 'ATP1000' order by a.week";
        String[] args = new String[]{};

        Cursor cursor = null;
        try {
            cursor = db.query(sql, args);
            while (cursor.moveToNext()) {
                MatchIdBean bean = map.get(cursor.getInt(0));
                if (bean == null) {
                    bean = new MatchIdBean();
                    bean.setId(cursor.getInt(0));
                    bean.setWeek(cursor.getInt(1));
                    bean.setNames(new ArrayList<String>());
                    map.put(bean.getId(), bean);
                }
                bean.getNames().add(cursor.getString(2));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return map;
    }

    public List<MatchBean> getMatchDbList() {
        List<MatchBean> list = new ArrayList<>();
        String sql = "SELECT * FROM " +  TMatch.TABLE_NAME;

        Cursor cursor = null;
        try {
            cursor = db.query(sql, new String[]{});
            while (cursor.moveToNext()) {
                list.add(TMatch.parseMatchBean(cursor));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public List<MatchNameBean> getMatchNameDbList() {
        List<MatchNameBean> list = new ArrayList<>();
        String sql = "SELECT * FROM " +  TMatchName.TABLE_NAME;

        Cursor cursor = null;
        try {
            cursor = db.query(sql, new String[]{});
            while (cursor.moveToNext()) {
                list.add(TMatchName.parseMatchNameBean(cursor));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }
}
