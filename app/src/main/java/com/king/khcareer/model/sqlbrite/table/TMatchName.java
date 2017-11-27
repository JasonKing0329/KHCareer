package com.king.khcareer.model.sqlbrite.table;

import android.database.Cursor;

import com.king.khcareer.model.sql.pubdata.bean.MatchBean;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/8/29 16:38
 */
public class TMatchName {
    // 表名
    public static final String TABLE_NAME = "_match_name";

    // 表字段
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_MATCH_ID = "match_id";

    // 连接查询中的重命名字段
    public static final String COLUMN_UNION_ID = "name_id";

    public static final Function<Cursor, MatchNameBean> MAPPER_TABLE = new Function<Cursor, MatchNameBean>() {

        @Override
        public MatchNameBean apply(@NonNull Cursor cursor) throws Exception {
            return parseMatchNameBean(cursor);
        }
    };

    public static MatchNameBean parseMatchNameBean(Cursor cursor) {
        MatchNameBean bean = new MatchNameBean();
        bean.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
        bean.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
        bean.setMatchId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MATCH_ID)));
        return bean;
    }

    public static MatchNameBean parseFullMatchBean(Cursor cursor) {
        MatchNameBean nameBean = new MatchNameBean();

        MatchBean bean = TMatch.parseMatchBean(cursor);
        nameBean.setMatchBean(bean);

        nameBean.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_UNION_ID)));
        nameBean.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
        nameBean.setMatchId(bean.getId());

        return nameBean;
    }

}
