package com.king.khcareer.model.sqlbrite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 描述: sqlite open helper
 * <p/>作者：景阳
 * <p/>创建时间: 2017/8/29 11:36
 */
public class UserHelper extends SQLiteOpenHelper {

    private String dbName;

    /**
     * 数据库版本号
     */
    private static final int DATABASE_VERSION = 1;

    public UserHelper(Context context, String dbName) {
        this(context, null, DATABASE_VERSION, null, dbName);
    }

    public UserHelper(Context context, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler, String name) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
