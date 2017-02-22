package com.king.mytennis.score;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.king.mytennis.interfc.DatabaseAccess;
import com.king.mytennis.match.MatchSeqBean;
import com.king.mytennis.match.MatchSqlModel;
import com.king.mytennis.match.MatchSqliteDB;
import com.king.mytennis.model.DatabaseStruct;
import com.king.mytennis.model.Record;
import com.king.mytennis.model.SQLiteDB;
import com.king.mytennis.utils.DebugLog;
import com.king.mytennis.view.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/20 16:43
 */
public class ScoreModel {

    private DatabaseAccess sqLitePlayer;
    private MatchSqlModel matchSqlModel;

    private String[] arrRound;
    private String[] arrLevel;

    public ScoreModel(Context context) {
        sqLitePlayer = new SQLiteDB(context);
        matchSqlModel = new MatchSqlModel(context);
        arrRound = context.getResources().getStringArray(R.array.spinner_round);
        arrLevel = context.getResources().getStringArray(R.array.spinner_level);
    }

    public List<ScoreBean> queryYearRecords() {
        long minTime = getYearMinTime();
        long maxTime = getMonthMaxTime();
        // 查询出今年初一直到本月的记录
        List<Record> list = new ArrayList<>();
        SQLiteDatabase db = sqLitePlayer.getSQLHelper().getWritableDatabase();
        Cursor cursor=db.query(
                DatabaseStruct.TABLE_RECORD, DatabaseStruct.TABLE_RECORD_COL
                , "date_long>? AND date_long<?", new String[] {String.valueOf(minTime), String.valueOf(maxTime)}, null, null, null);
        while (cursor.moveToNext()){
            Record record = new Record();
            adaptRecord(cursor, record);
            list.add(record);
        }
        db.close();

        // 去掉不在积分周期的记录
        List<ScoreBean> scoreList = distinctOutsideRecord(list);
        return scoreList;
    }

    public List<ScoreBean> query52WeekRecords() {

        long minTime = getLastYearMinTime();
        long maxTime = getMonthMaxTime();

        // 查询出一整年的记录
        List<Record> list = new ArrayList<>();
        SQLiteDatabase db = sqLitePlayer.getSQLHelper().getWritableDatabase();
        Cursor cursor=db.query(
                DatabaseStruct.TABLE_RECORD, DatabaseStruct.TABLE_RECORD_COL
                , "date_long>? AND date_long<?", new String[] {String.valueOf(minTime), String.valueOf(maxTime)}, null, null, null);
        while (cursor.moveToNext()){
            Record record = new Record();
            adaptRecord(cursor, record);
            list.add(record);
        }
        db.close();
        
        // 去掉不在积分周期的记录
        List<ScoreBean> scoreList = distinctOutsideRecord(list);
        return scoreList;
    }

    private List<ScoreBean> distinctOutsideRecord(List<Record> list) {
        List<ScoreBean> scoreList = new ArrayList<>();
        // 去年的本周到今年的上一周为一个积分周期
        int weekOfYear = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) - 1;
        int weekOfLastYear = weekOfYear + 1;

        ScoreBean masterCupBean = null;
        Map<String, ScoreBean> recMap = new HashMap<>();
        for (int i = 0; i < list.size(); i ++) {
            Record record = list.get(i);

            // 大师杯积分不走通用情况
            if (arrLevel[1].equals(record.getLevel())) {
                if (masterCupBean == null) {
                    masterCupBean = new ScoreBean();
                }
                addScoreBean(scoreList, masterCupBean, weekOfLastYear, weekOfYear, record);
            }
            else {
                ScoreBean bean = recMap.get(record.getMatch());
                if (bean == null) {
                    bean = new ScoreBean();
                    recMap.put(record.getMatch(), bean);
                }
                // 出现负场，该项赛事结束
                if (record.getWinner().equals(record.getCompetitor())) {
                    addScoreBean(scoreList, bean, weekOfLastYear, weekOfYear, record);
                }
                // 一直胜场，直到Final也是胜，该项赛事结束
                else if (arrRound[0].equals(record.getRound())) {
                    addScoreBean(scoreList, bean, weekOfLastYear, weekOfYear, record);
                }
                //其他情况表示赛事未结束或者无积分
            }
        }
        if (masterCupBean != null && masterCupBean.getName() != null) {
            scoreList.add(masterCupBean);
        }
        return scoreList;
    }

    private void addScoreBean(List<ScoreBean> scoreList, ScoreBean bean, int weekOfLastYear, int weekOfYear, Record record) {
        MatchSeqBean matchSeqBean = matchSqlModel.getMatchSeqBeanByName(record.getMatch());
        if (matchSeqBean == null) {
            DebugLog.e("Error: No match name '" + record.getMatch() + "' found in table match_seq!");
            return;
        }
        // 如果在积分周期，则记录为score bean
        int week = matchSeqBean.getSequence();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        boolean needAdd = false;
        int recordYear = Integer.parseInt(record.getStrDate().split("-")[0]);
        // 今年的赛事，周数应该小于当前
        if (recordYear == year) {
            if (week <= weekOfYear) {
                needAdd = true;
            }
        }
        // 去年的赛事，周数应该大于去年同期
        else {
            if (week >= weekOfLastYear) {
                needAdd = true;
            }
        }
        if (needAdd) {
            bean.setWeek(week);
            bean.setName(record.getMatch());
            bean.setYear(recordYear);
            bean.setLevel(record.getLevel());
            bean.setCourt(record.getCourt());
            bean.setMatchBean(matchSeqBean);

            // 大师杯积分不走通用情况，这里只累计积分
            if (arrLevel[1].equals(record.getLevel())) {
                // 按照ATP的规则，只有胜才积分，负没有分
                if (!record.getWinner().equals(record.getCompetitor())) {
                    if (arrRound[7].equals(record.getRound())) {// group, 一场200
                        bean.setScore(bean.getScore() + 200);
                    }
                    if (arrRound[1].equals(record.getRound())) {// semi final胜多加400
                        bean.setScore(bean.getScore() + 400);
                    }
                    if (arrRound[0].equals(record.getRound())) {// final胜多加500
                        bean.setScore(bean.getScore() + 500);
                    }
                }
            }
            else {
                bean.setScore(getMatchScore(record));
                scoreList.add(bean);
            }
        }
    }

    /**
     * 计算该项赛事的最终积分
     * @param record 输掉的轮次或者最终夺冠的轮次
     * @return
     */
    private int getMatchScore(Record record) {
        if (record.getWinner().equals(record.getCompetitor())) {// 负
            return ScoreTable.getScore(record.getRound(), record.getLevel(), false, arrRound, arrLevel);
        }
        else if (arrRound[0].equals(record.getRound())) {
            return ScoreTable.getScore(record.getRound(), record.getLevel(), true, arrRound, arrLevel);
        }
        return 0;
    }

    /**
     * 去年的今年这个月第一毫秒
     * @return
     */
    public long getLastYearMinTime() {
        int year = Calendar.getInstance().get(Calendar.YEAR) - 1;
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        String strDate = "" + year + "-" + month;
        if (month < 10) {
            strDate = "" + year + "-0" + month;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = date.getTime();
        return time - 1;
    }

    /**
     * 今年第一毫秒之前，即去年的最后一毫秒
     * @return
     */
    public long getYearMinTime() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String strDate = "" + year + "-01";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = date.getTime();
        return time - 1;
    }

    /**
     * 今年当月最后一毫秒
     * @return
     */
    public long getMonthMaxTime() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        // 最后一天
        int day = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        StringBuffer buffer = new StringBuffer();
        buffer.append(year).append("-");
        if (month < 10) {
            buffer.append("0").append(month);
        }
        else {
            buffer.append(month);
        }
        buffer.append("-");
        if (day < 10) {
            buffer.append("0").append(day);
        }
        else {
            buffer.append(day);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(buffer.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long time = date.getTime();
        return time;
    }

    private void adaptRecord(Cursor cursor, Record record) {
        record.setId(cursor.getInt(DatabaseStruct.COL_ID));
        record.setCompetitor(cursor.getString(DatabaseStruct.COL_competitor));
        record.setCptCountry(cursor.getString(DatabaseStruct.COL_competitor_country));
        record.setCourt(cursor.getString(DatabaseStruct.COL_court));
        record.setLongDate(Long.parseLong(cursor.getString(DatabaseStruct.COL_date_long)));
        record.setStrDate(cursor.getString(DatabaseStruct.COL_date_str));
        record.setWinner(cursor.getString(DatabaseStruct.COL_iswinner));
        record.setLevel(cursor.getString(DatabaseStruct.COL_level));
        record.setMatch(cursor.getString(DatabaseStruct.COL_match));
        record.setCity(cursor.getString(DatabaseStruct.COL_match_city));
        record.setMatchCountry(cursor.getString(DatabaseStruct.COL_match_country));
        record.setRank(Integer.parseInt(cursor.getString(DatabaseStruct.COL_rankp1)));
        record.setCptRank(Integer.parseInt(cursor.getString(DatabaseStruct.COL_rank)));
        record.setSeed(Integer.parseInt(cursor.getString(DatabaseStruct.COL_seedp1)));
        record.setCptSeed(Integer.parseInt(cursor.getString(DatabaseStruct.COL_seed)));
        record.setRegion(cursor.getString(DatabaseStruct.COL_region));
        record.setRound(cursor.getString(DatabaseStruct.COL_round));
        record.setScore(cursor.getString(DatabaseStruct.COL_score));
    }

}
