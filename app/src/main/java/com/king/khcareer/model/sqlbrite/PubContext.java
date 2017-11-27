package com.king.khcareer.model.sqlbrite;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.utils.DebugLog;

import java.io.File;

/**
 * 复写ContextWrapper的openOrCreateDatabase，更改为从指定目录加载数据库
 */
public class PubContext extends ContextWrapper {
    public PubContext(Context base) {
        super(base);
    }

    /**
     * 获得数据库路径，如果不存在，则创建对象对象
     *
     * @param name
     */
    @Override
    public File getDatabasePath(String name) {
//        return super.getDatabasePath(name);

        String dbDir = Configuration.CONF_DIR;
        return getFilePath(dbDir, name);
    }


    private File getFilePath(String dbDir, String name) {
        File file = null;
        makeRootDirectory(dbDir);
        try {
            file = new File(dbDir + "/" + name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;


    }

    private void makeRootDirectory(String dbPath) {
        File file = null;
        try {
            file = new File(dbPath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }


    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        DebugLog.e(name);
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory,
                                               DatabaseErrorHandler errorHandler) {
        DebugLog.e(name);
        SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
        return result;
    }

}