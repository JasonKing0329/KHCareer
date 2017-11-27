package com.king.khcareer.model.sqlbrite.dao;

import android.content.ContentValues;
import android.database.Cursor;

import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.khcareer.model.sqlbrite.table.TPlayer;
import com.squareup.sqlbrite2.BriteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/27 11:06
 */
public class PlayerDao {

    private BriteDatabase db;

    public PlayerDao(BriteDatabase db) {
        this.db = db;
    }

    public List<PlayerBean> queryPlayerList(boolean orderByPinyin) {
        List<PlayerBean> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TPlayer.TABLE_NAME;
        if (orderByPinyin) {
            sql = sql.concat(" ORDER BY name_pinyin ASC");
        }
        String[] args = new String[]{};
        Cursor cursor = null;
        try {
            cursor = db.query(sql, args);
            while (cursor.moveToNext()) {
                list.add(TPlayer.parsePlayerBean(cursor));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    public void inserPlayerBean(PlayerBean bean) {
        ContentValues values = new ContentValues();
        values.put(TPlayer.COLUMN_BIRTHDAY, bean.getBirthday());
        values.put(TPlayer.COLUMN_CITY, bean.getCity());
        values.put(TPlayer.COLUMN_COUNTRY, bean.getCountry());
        values.put(TPlayer.COLUMN_NAME_CHN, bean.getNameChn());
        values.put(TPlayer.COLUMN_NAME_ENG, bean.getNameEng());
        values.put(TPlayer.COLUMN_NAME_PY, bean.getNamePinyin());
        db.insert(TPlayer.TABLE_NAME, values);
    }

    public void updatePlayerBean(PlayerBean bean) {
        ContentValues values = new ContentValues();
        values.put(TPlayer.COLUMN_BIRTHDAY, bean.getBirthday());
        values.put(TPlayer.COLUMN_CITY, bean.getCity());
        values.put(TPlayer.COLUMN_COUNTRY, bean.getCountry());
        values.put(TPlayer.COLUMN_NAME_CHN, bean.getNameChn());
        values.put(TPlayer.COLUMN_NAME_ENG, bean.getNameEng());
        values.put(TPlayer.COLUMN_NAME_PY, bean.getNamePinyin());
        db.update(TPlayer.TABLE_NAME, values, TPlayer.COLUMN_ID + "=?", new String[]{String.valueOf(bean.getId())});
    }

    public void deletePlayer(int playerId) {
        db.delete(TPlayer.TABLE_NAME, TPlayer.COLUMN_ID + "=?", new String[]{String.valueOf(playerId)});
    }

    public PlayerBean queryPlayerByChnName(String name) {
        String sql = "SELECT * FROM " + TPlayer.TABLE_NAME + " WHERE " + TPlayer.COLUMN_NAME_CHN + "=?";
        String[] args = new String[]{name};
        Cursor cursor = null;
        try {
            cursor = db.query(sql, args);
            if (cursor.moveToNext()) {
                return TPlayer.parsePlayerBean(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }
}
