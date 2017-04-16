package com.king.khcareer.match.manage;

import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/13 16:05
 */
public class MatchCache {

    private static MatchNameBean instance;

    public static void putMatchNameBean(MatchNameBean bean) {
        instance = bean;
    }

    public static MatchNameBean getInstance() {
        return instance;
    }

    public static void clear() {
        instance = null;
    }

}
