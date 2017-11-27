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
public class PubHelper extends SQLiteOpenHelper {

    /**
     * 数据库名称
     */
    public static final String DB_NAME = "mytennis_public.db";
    /**
     * 数据库版本号
     */
    private static final int DATABASE_VERSION = 2;

    public PubHelper(Context context) {
        this(context, null, DATABASE_VERSION, null);
    }

    public PubHelper(Context context, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, DB_NAME, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
