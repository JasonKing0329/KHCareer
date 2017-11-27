package com.king.khcareer.model.sqlbrite.table;

import android.database.Cursor;

import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/8/29 16:38
 */
public class TPlayer {
    // 表名
    public static final String TABLE_NAME = "_player";

    // 表字段
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME_ENG = "name_eng";
    public static final String COLUMN_NAME_CHN = "name_chn";
    public static final String COLUMN_NAME_PY = "name_pinyin";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_BIRTHDAY = "birthday";

    public static final Function<Cursor, PlayerBean> MAPPER_TABLE = new Function<Cursor, PlayerBean>() {

        @Override
        public PlayerBean apply(@NonNull Cursor cursor) throws Exception {
            return parsePlayerBean(cursor);
        }
    };

    public static PlayerBean parsePlayerBean(Cursor cursor) {
        PlayerBean bean = new PlayerBean();
        bean.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        bean.setNameEng(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_ENG)));
        bean.setNameChn(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_CHN)));
        bean.setNamePinyin(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_PY)));
        bean.setCountry(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY)));
        bean.setCity(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITY)));
        bean.setBirthday(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIRTHDAY)));
        return bean;
    }

}
