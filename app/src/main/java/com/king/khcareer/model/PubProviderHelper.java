package com.king.khcareer.model;

import com.king.khcareer.base.KApplication;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sqlbrite.PubContext;
import com.king.khcareer.model.sqlbrite.SqlBriteProvider;
import com.king.khcareer.utils.DebugLog;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/10/25 10:48
 */
public class PubProviderHelper {

    private static PubDataProvider instance;

    public static void createProvider() {
        DebugLog.e("");
        instance = new SqlBriteProvider(new PubContext(KApplication.getInstance()));
    }

    public static PubDataProvider getProvider() {
        if (instance == null) {
            synchronized (PubProviderHelper.class) {
                createProvider();
            }
        }
        return instance;
    }

    public static void close() {
        if (instance != null) {
            DebugLog.e("");
            instance.close();
            instance = null;
        }
    }
}
