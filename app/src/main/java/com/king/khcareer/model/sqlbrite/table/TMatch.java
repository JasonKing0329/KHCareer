package com.king.khcareer.model.sqlbrite.table;

import android.database.Cursor;

import com.king.khcareer.model.sql.pubdata.bean.MatchBean;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/8/29 16:38
 */
public class TMatch {
    // 表名
    public static final String TABLE_NAME = "_match";

    // 表字段
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_COURT = "court";
    public static final String COLUMN_REGION = "region";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_WEEK = "week";
    public static final String COLUMN_MONTH = "month";

    public static final Function<Cursor, MatchBean> MAPPER_TABLE = new Function<Cursor, MatchBean>() {

        @Override
        public MatchBean apply(@NonNull Cursor cursor) throws Exception {
            return parseMatchBean(cursor);
        }
    };

    public static MatchBean parseMatchBean(Cursor cursor) {
        MatchBean bean = new MatchBean();
        bean.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        bean.setCountry(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COUNTRY)));
        bean.setCity(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CITY)));
        bean.setLevel(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LEVEL)));
        bean.setCourt(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURT)));
        bean.setRegion(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REGION)));
        bean.setWeek(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WEEK)));
        bean.setMonth(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MONTH)));
        return bean;
    }

}
