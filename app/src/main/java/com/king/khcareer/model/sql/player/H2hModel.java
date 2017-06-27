package com.king.khcareer.model.sql.player;

import android.database.Cursor;

import com.king.khcareer.model.sql.player.bean.H2hParentBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 描述: head to head
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/27 13:54
 */
public class H2hModel extends BaseSqlModel {

    public List<H2hParentBean> queryH2hList() {
        List<H2hParentBean> list = new ArrayList<>();
        String sql = "SELECT * FROM h2hview";
        Cursor cursor = getCursor(sql, null);
        while (cursor.moveToNext()) {
            H2hParentBean bean = parseH2hParentBean(cursor);
            list.add(bean);
        }
        return list;
    }

    public List<H2hParentBean> queryH2hListByCountry(String country) {
        List<H2hParentBean> list = new ArrayList<>();
        String sql = "SELECT * FROM h2hview WHERE competitor_country=?";
        Cursor cursor = getCursor(sql, new String[]{country});
        while (cursor.moveToNext()) {
            H2hParentBean bean = parseH2hParentBean(cursor);
            list.add(bean);
        }
        return list;
    }

    public List<H2hParentBean> queryH2hListByTotal(int min, int max) {
        List<H2hParentBean> list = new ArrayList<>();
        // args的方法在这里只对text类型的字段有用(queryH2hListByCountry)，integer类型的字段不管用，查不到数据，可能是底层的原因
        // 后面的by win/lose/odds也是如此
//        String sql = "SELECT * FROM h2hview WHERE total BETWEEN ? AND ?";
//        args = new String[]{String.valueOf(min), String.valueOf(max)};
        String sql = "SELECT * FROM h2hview WHERE total BETWEEN " + min + " AND " + max;
        Cursor cursor = getCursor(sql, null);
        while (cursor.moveToNext()) {
            H2hParentBean bean = parseH2hParentBean(cursor);
            list.add(bean);
        }
        return list;
    }

    public List<H2hParentBean> queryH2hListByWin(int min, int max) {
        List<H2hParentBean> list = new ArrayList<>();
        String sql = "SELECT * FROM h2hview WHERE win BETWEEN " + min + " AND " + max;
        Cursor cursor = getCursor(sql, null);
        while (cursor.moveToNext()) {
            H2hParentBean bean = parseH2hParentBean(cursor);
            list.add(bean);
        }
        return list;
    }

    public List<H2hParentBean> queryH2hListByLose(int min, int max) {
        List<H2hParentBean> list = new ArrayList<>();
        String sql = "SELECT * FROM h2hview WHERE lose BETWEEN " + min + " AND " + max;
        Cursor cursor = getCursor(sql, null);
        while (cursor.moveToNext()) {
            H2hParentBean bean = parseH2hParentBean(cursor);
            list.add(bean);
        }
        return list;
    }

    public List<H2hParentBean> queryH2hListByOdds(int min, int max) {
        List<H2hParentBean> list = new ArrayList<>();
        String sql = "SELECT * FROM h2hview WHERE win - lose BETWEEN " + min + " AND " + max;
        Cursor cursor = getCursor(sql, null);
        while (cursor.moveToNext()) {
            H2hParentBean bean = parseH2hParentBean(cursor);
            list.add(bean);
        }
        return list;
    }

    /**
     * count top10 win lose
     * @return
     */
    public Integer[] getTopTenCount(boolean isThisYear) {
        String sql = "select sum(case when iswinner='_user' then 1 else 0 end) as win " +
                ", sum(case when iswinner='_user' then 0 else 1 end) as lose from record where rank>=1 and rank <=10";
        if (isThisYear) {
            sql = new StringBuffer(sql).append(" AND date_str LIKE '").append(Calendar.getInstance().get(Calendar.YEAR)).append("%'").toString();
        }
        Integer[] count = new Integer[2];
        Cursor cursor = getCursor(sql, null);
        if (cursor.moveToNext()) {
            count[0] = cursor.getInt(0);
            count[1] = cursor.getInt(1);
        }

        return count;
    }

    /**
     * count top10 win lose
     * @return
     */
    public Integer[] getTotalCount(boolean isWinner, boolean isThisYear) {
        String winnerArg = "iswinner='_user'";
        if (!isWinner) {
            winnerArg = "iswinner!='_user'";
        }
        String sql = "select sum(case when rank between 1 and 10 and " + winnerArg + " then 1 else 0 end) as top10" +
                "  ,sum(case when rank between 11 and 20 and " + winnerArg + " then 1 else 0 end) as top20" +
                "  ,sum(case when rank between 21 and 50 and " + winnerArg + " then 1 else 0 end) as top50" +
                "  ,sum(case when rank between 51 and 100 and " + winnerArg + " then 1 else 0 end) as top100" +
                "  ,sum(case when (rank > 100 or rank=0) and " + winnerArg + " then 1 else 0 end) as outof100" +
                "  FROM record";
        if (isThisYear) {
            sql = new StringBuffer(sql).append(" WHERE date_str LIKE '").append(Calendar.getInstance().get(Calendar.YEAR)).append("%'").toString();
        }
        Integer[] count = new Integer[5];
        Cursor cursor = getCursor(sql, null);
        if (cursor.moveToNext()) {
            count[0] = cursor.getInt(0);
            count[1] = cursor.getInt(1);
            count[2] = cursor.getInt(2);
            count[3] = cursor.getInt(3);
            count[4] = cursor.getInt(4);
        }

        return count;
    }

    private H2hParentBean parseH2hParentBean(Cursor cursor) {
        H2hParentBean bean = new H2hParentBean();
        bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
        bean.setPlayer(cursor.getString(cursor.getColumnIndex("competitor")));
        bean.setCountry(cursor.getString(cursor.getColumnIndex("competitor_country")));
        bean.setTotal(cursor.getInt(cursor.getColumnIndex("total")));
        bean.setWin(cursor.getInt(cursor.getColumnIndex("win")));
        bean.setLose(cursor.getInt(cursor.getColumnIndex("lose")));
        return bean;
    }
}
