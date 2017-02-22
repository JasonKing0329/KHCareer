package com.king.mytennis.score;

import android.content.Context;

import com.king.mytennis.view.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/21 13:59
 */
public class ScorePresenter {

    private ScoreModel scoreModel;
    private IScorePageView scorePageView;

    private String[] arrLevel;
    private String[] arrCourt;
    
    private ScoreComparator scoreComparator;
    private MatchSeqComparator matchSeqComparator;

    private int thisYear;
    
    public ScorePresenter(Context context) {
        arrCourt = context.getResources().getStringArray(R.array.spinner_court);
        arrLevel = context.getResources().getStringArray(R.array.spinner_level);
        scoreModel = new ScoreModel(context);
        scoreComparator = new ScoreComparator();
        matchSeqComparator = new MatchSeqComparator();
        thisYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    public void setScorePageView(IScorePageView scorePageView) {
        this.scorePageView = scorePageView;
    }

    public void queryYearRecords() {
        List<ScoreBean> list = scoreModel.queryYearRecords();
        groupYearScores(list);
    }

    public void query52WeekRecords() {
        List<ScoreBean> list = scoreModel.query52WeekRecords();
        group52WeekScores(list);
    }

    public void groupYearScores(List<ScoreBean> list) {

        createPageData(list);
    }

    public void group52WeekScores(List<ScoreBean> list) {

        createPageData(list);
    }

    private void createPageData(List<ScoreBean> list) {
        ScorePageData data = new ScorePageData();
        data.setScoreList(list);

        Map<String, List<ScoreBean>> levelMap = new HashMap<>();
        data.setLevelMap(levelMap);

        int countScore = 0, countYear = 0, countLastYear = 0, countHard = 0, countClay = 0
                , countGrass = 0, countInHard = 0;

        int year = Calendar.getInstance().get(Calendar.YEAR);
        for (ScoreBean bean:list) {
            // count score
            countScore += bean.getScore();

            // count year score
            if (bean.getYear() == year) {
                countYear += bean.getScore();
            }
            else {
                countLastYear += bean.getScore();
            }

            // count court score
            if (bean.getCourt().equals(arrCourt[1])) {
                countClay += bean.getScore();
            }
            else if (bean.getCourt().equals(arrCourt[2])) {
                countGrass += bean.getScore();
            }
            else if (bean.getCourt().equals(arrCourt[3])) {
                countInHard += bean.getScore();
            }
            else {
                countHard += bean.getScore();
            }

            // group by level
            List<ScoreBean> sl = levelMap.get(bean.getLevel());
            if (sl == null) {
                sl = new ArrayList<>();
                levelMap.put(bean.getLevel(), sl);
            }
            sl.add(bean);
        }

        data.setCountScore(countScore);
        data.setCountScoreClay(countClay);
        data.setCountScoreGrass(countGrass);
        data.setCountScoreInHard(countInHard);
        data.setCountScoreHard(countHard);
        data.setCountScoreYear(countYear);
        data.setCountScoreLastYear(countLastYear);
        scorePageView.onPageDataLoaded(data);
    }

    /**
     * 拼装level对应下的数据记录
     * 只取keepNumber对应条记录，其他进入replaceList
     * @param scoreList
     * @param keepNumber
     * @param replaceList
     * @return
     */
    public String getGroupText(List<ScoreBean> scoreList, int keepNumber, List<ScoreBean> replaceList) {
        StringBuffer buffer = new StringBuffer();
        if (scoreList != null) {
            // 先筛选出积分最高的keepNumber站
            Collections.sort(scoreList, scoreComparator);
            List<ScoreBean> list = new ArrayList<>();
            for (int i = 0; i < scoreList.size(); i ++) {
                if (i < keepNumber) {
                    list.add(scoreList.get(i));
                }
                else {
                    replaceList.add(scoreList.get(i));
                }
            }
            // 再按照赛事的先后顺序排列
            Collections.sort(list, matchSeqComparator);
            for (int i = 0; i < list.size(); i ++) {
                addToGroup(list, i, buffer);
            }
        }
        return buffer.toString();
    }

    /**
     * 拼装level对应下的数据记录
     * @param list
     * @param distinctMatch 需要排除的赛事
     * @param replaceList 排除的赛事加入到replaceList
     * @return
     */
    public String getGroupText(List<ScoreBean> list, String distinctMatch, List<ScoreBean> replaceList) {
        StringBuffer buffer = new StringBuffer();
        if (list != null) {
            // 按照赛事的先后顺序排列
            Collections.sort(list, matchSeqComparator);
            for (int i = 0; i < list.size(); i ++) {
                if (list.get(i).getName().equals(distinctMatch)) {
                    replaceList.add(list.get(i));
                }
                else {
                    addToGroup(list, i, buffer);
                }
            }
        }
        return buffer.toString();
    }

    private void addToGroup(List<ScoreBean> gsList, int i, StringBuffer buffer) {
        buffer.append(gsList.get(i).getName()).append("  ").append(gsList.get(i).getScore());
        // 今年的赛事标记为已完成
        if (gsList.get(i).getYear() == thisYear) {
            buffer.append("  √");
        }
        if (i < gsList.size() - 1) {
            buffer.append("\n");
        }
    }

    public int getThisYear() {
        return thisYear;
    }

    /**
     * 将赛事按照积分从大到小排列
     */
    private class ScoreComparator implements Comparator<ScoreBean> {

        @Override
        public int compare(ScoreBean lhs, ScoreBean rhs) {
            return rhs.getScore() - lhs.getScore();
        }
    }

    /**
     * 将赛事按照赛事序号从小到大排列
     */
    private class MatchSeqComparator implements Comparator<ScoreBean> {

        @Override
        public int compare(ScoreBean lhs, ScoreBean rhs) {
            int lMatchSeq = 0;
            int rMatchSeq = 0;
            if (lhs.getMatchBean() != null) {
                lMatchSeq = lhs.getMatchBean().getSequence();
            }
            if (rhs.getMatchBean() != null) {
                rMatchSeq = rhs.getMatchBean().getSequence();
            }
            return lMatchSeq - rMatchSeq;
        }
    }
}
