package com.king.mytennis.match;

import android.content.Context;

import com.king.mytennis.model.MySQLHelper;
import com.king.mytennis.model.SQLiteDB;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/20 16:05
 */
public class MatchSqliteDB extends SQLiteDB {

    public MatchSqliteDB(Context context) {
        super(context);
    }

    @Override
    protected void initSqlHelper(Context context) {
        sqlHelper = MySQLHelper.getPublicInstance(context);
    }

}
