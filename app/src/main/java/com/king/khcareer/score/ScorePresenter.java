package com.king.khcareer.score;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.model.FileIO;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;

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
public class ScorePresenter implements IScoreCallback {

    private ScoreModel scoreModel;
    private IScorePageView scorePageView;
    private PubDataProvider pubDataProvider;

    private String[] arrLevel;
    private String[] arrCourt;
    
    private ScoreComparator scoreComparator;
    private MatchSeqComparator matchSeqComparator;
    private ScorePageData scorePageData;

    private int thisYear;
    private int thisWeek;
    private int currentYear;
    private int startWeek, endWeek;
    private int startYear, endYear;

    public ScorePresenter() {
        arrCourt = Constants.RECORD_MATCH_COURTS;
        arrLevel = Constants.RECORD_MATCH_LEVELS;
        scoreModel = new ScoreModel(this);
        scoreComparator = new ScoreComparator();
        matchSeqComparator = new MatchSeqComparator();
        thisYear = Calendar.getInstance().get(Calendar.YEAR);
        thisWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        currentYear = thisYear;
        scorePageData = new ScorePageData();
        pubDataProvider = new PubDataProvider();
    }

    public void setScorePageView(IScorePageView scorePageView) {
        this.scorePageView = scorePageView;
    }

    public RankBean loadRank() {
        return new FileIO().readRankBean();
    }

    public void queryYearRecords() {
        startYear = currentYear;
        endYear = currentYear;
        startWeek = 0;
        endWeek = 52;
        scoreModel.queryYearRecords(currentYear);
    }

    public void query52WeekRecords() {
        startYear = thisYear - 1;
        endYear = thisYear;
        startWeek = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
        endWeek = startWeek - 1;
        startYear--;
        scoreModel.query52WeekRecords();
    }

    @Override
    public void onYearRecordsLoaded(List<ScoreBean> list, List<String> nonExistMatchList) {
        scorePageData.setNonExistMatchList(nonExistMatchList);
        groupYearScores(list);
    }

    @Override
    public void on52WeekRecordsLoaded(List<ScoreBean> list, List<String> nonExistMatchList) {
        scorePageData.setNonExistMatchList(nonExistMatchList);
        group52WeekScores(list);
    }

    public void groupYearScores(List<ScoreBean> list) {
        createPageData(list);
    }

    public void group52WeekScores(List<ScoreBean> list) {
        createPageData(list);
    }

    private void addScoreBeanToList(boolean isForce, Map<String, ScoreBean> map, List<ScoreBean> gsList, List<MatchNameBean> matchList, int i) {
        ScoreBean scoreBean = map.get(matchList.get(i).getName());
        if (scoreBean == null) {// 没有参加
            if (isForce) {// 强制计0
                scoreBean = new ScoreBean();
                scoreBean.setMatchBean(matchList.get(i));
                scoreBean.setScore(0);
                scoreBean.setChampion(false);
                setScoreBeanDetail(scoreBean);
            }
        }
        if (scoreBean != null) {
            gsList.add(scoreBean);
            map.remove(scoreBean.getMatchBean().getName());
        }
    }

    /**
     * 设置赛事年份、是否已完成标志
     * @param scoreBean
     */
    private void setScoreBeanDetail(ScoreBean scoreBean) {
        int matchWeek = scoreBean.getMatchBean().getMatchBean().getWeek();

        // 按year查询，所有的记录均为已完成记录
        if (startYear == endYear) {
            scoreBean.setYear(currentYear);
            if (endYear == thisYear) {
                scoreBean.setCompleted(matchWeek < thisWeek);
            }
            else {
                scoreBean.setCompleted(true);
            }
        }
        else {// 52 week
            if (matchWeek < thisWeek) {
                scoreBean.setYear(currentYear);
            }
            else {
                scoreBean.setYear(currentYear - 1);
            }

            if (endYear == thisYear) {// 去年到今年，今年的为已完成赛事，去年的为未完成待保分的赛事
                scoreBean.setCompleted(scoreBean.getYear() == thisYear);
            }
            else {
                scoreBean.setCompleted(true);
            }
        }
    }

    private void createPageData(List<ScoreBean> list) {
        scorePageData.setScoreList(list);

        // 戴维斯杯的积分情况较为复杂，本系统不考虑这种情况，按照other赛事0分处理
        // 4大满贯+年终总决赛+8站强制ATP1000+6站最好
        // 上一年年终top30的有500赛强制罚分，必须参加4项500赛，且有一项是美网后（蒙卡算500赛），只要参加即刻，可以不计入6站最好（比如6个250夺冠，就不计算4个500赛首轮游）
        // 非top30 按照取18站最好成绩的做法，若参加了大满贯和1000赛需要强制计入

        Map<String, ScoreBean> map = new HashMap<>();
        for (ScoreBean bean:list) {
            // 设置年份和完成情况
            setScoreBeanDetail(bean);
            map.put(bean.getMatchBean().getName(), bean);
        }

        boolean isForce = currentYear > MultiUserManager.getInstance().getFirstTop30Year();

        List<ScoreBean> replaceList = new ArrayList<>();

        // create gs list
        List<ScoreBean> gsList = new ArrayList<>();
        List<MatchNameBean> matchList = pubDataProvider.getMatchListByLevel(arrLevel[0]);
        if (matchList != null) {
            for (int i = 0; i < matchList.size(); i ++) {
                addScoreBeanToList(isForce, map, gsList, matchList, i);
            }
        }

        // create master cup
        matchList = pubDataProvider.getMatchListByLevel(arrLevel[1]);
        List<ScoreBean> mcList = new ArrayList<>();
        if (matchList != null) {
            for (int i = 0; i < matchList.size(); i ++) {
                addScoreBeanToList(false, map, mcList, matchList, i);
            }
        }

        // create 1000, 蒙卡先排除
        matchList = pubDataProvider.getMatchListByLevel(arrLevel[2]);
        List<ScoreBean> list1000 = new ArrayList<>();
        if (matchList != null) {
            for (int i = 0; i < matchList.size(); i ++) {
                if (matchList.get(i).getName().equals(Constants.MATCH_CONST_MONTECARLO)) {
                    if (map.get(Constants.MATCH_CONST_MONTECARLO) != null) {
                        continue;
                    }
                }
                addScoreBeanToList(isForce, map, list1000, matchList, i);
           }
        }
        ScoreBean removeBean = null;
        // 目前的历史记录里，西南财团公开赛和辛辛那提大师赛会出现重复，只保留有分的一站，如果都没分保留辛辛那提大师赛
        for (int i = 1; i < list1000.size(); i ++) {
            if (list1000.get(i).getMatchBean().getMatchId() == list1000.get(i - 1).getMatchBean().getMatchId()) {
                if (list1000.get(i - 1).getScore() > 0) {
                    removeBean = list1000.get(i);
                }
                else {
                    removeBean = list1000.get(i - 1);
                }
            }
        }
        if (removeBean != null) {
            list1000.remove(removeBean);
        }

        // 在剩下的赛事中检查需要罚分的站数（必须够4站，且有一站在美网之后）
        int countPunish = 4;
        if (isForce) {
            boolean hasAfterUsOpen = false;
            for (ScoreBean bean:map.values()) {
                String level = bean.getMatchBean().getMatchBean().getLevel();
                if (level.equals(arrLevel[3])) {// atp500
                    if (countPunish > 0) {
                        countPunish --;
                    }
                    if (bean.getMatchBean().getMatchBean().getWeek() > 35) {// 35是美网的周数
                        hasAfterUsOpen = true;
                    }
                }
                else if (level.equals(arrLevel[2])) {// 蒙特卡洛
                    if (countPunish > 0) {
                        countPunish --;
                    }
                }
            }
            if (countPunish == 0 && !hasAfterUsOpen) {
                countPunish ++;
            }
        }
        else {
            countPunish = 0;
        }

        // 将剩下的全部赛事按照积分排序
        for (ScoreBean bean:map.values()) {
            replaceList.add(bean);
        }
        Collections.sort(replaceList, scoreComparator);

        int countAvailable;
        // top30取包括罚分在内的6站最好成绩
        if (isForce) {
            countAvailable = 6 - countPunish;
        }
        // 非top30取 18-gs-atp1000 站最好成绩
        else {
            countAvailable = 18 - gsList.size() - list1000.size();
        }

        List<ScoreBean> list500 = new ArrayList<>();
        List<ScoreBean> list250 = new ArrayList<>();
        List<ScoreBean> listOther = new ArrayList<>();

        // 加上罚分赛事
        for (int i = 0; i < countPunish; i ++) {
            list500.add(getPunishScoreBean());
        }

        // 将countAvailable站赛事积分算入积分系统内（进入相应的list1000, 500, 250, replace, other）
        for (int i = 0; i < replaceList.size(); i ++) {
            ScoreBean bean = replaceList.get(i);
            String level = bean.getMatchBean().getMatchBean().getLevel();
            if (i < countAvailable) {
                if (level.equals(arrLevel[2])) {// 蒙特卡洛
                    list1000.add(bean);
                }
                else if (level.equals(arrLevel[3])) {
                    list500.add(bean);
                }
                else if (level.equals(arrLevel[4])) {
                    list250.add(bean);
                }
                else {
                    listOther.add(bean);
                }
            }
            // 留在replace里的也要检测是否是属于other的
            else {
                if (level.equals(arrLevel[5]) || level.equals(arrLevel[6])) {// 戴维斯杯，2016奥运会
                    listOther.add(bean);
                }
            }
        }
        for (int i = countAvailable - 1; i >= 0; i --) {
            if (i < replaceList.size()) {
                replaceList.remove(i);
            }
        }

        scorePageData.setGsList(gsList);
        scorePageData.setMasterCupList(mcList);
        scorePageData.setAtp1000List(list1000);
        scorePageData.setAtp500List(list500);
        scorePageData.setAtp250List(list250);
        scorePageData.setReplaceList(replaceList);
        scorePageData.setOtherList(listOther);

        // 生成统计数据，统计范围为算进积分的赛事
        int countScore = 0, countYear = 0, countLastYear = 0, countHard = 0, countClay = 0
                , countGrass = 0, countInHard = 0;

        int year = Calendar.getInstance().get(Calendar.YEAR);
        List<ScoreBean> availableList = new ArrayList<>();
        availableList.addAll(gsList);
        availableList.addAll(mcList);
        availableList.addAll(list1000);
        availableList.addAll(list500);
        availableList.addAll(list250);

        for (ScoreBean bean:availableList) {
            if (bean.getMatchBean() == null) {
                continue;
            }
            // count score
            countScore += bean.getScore();

            // count year score
            if (bean.getYear() == year) {
                countYear += bean.getScore();
            }
            else {
                countLastYear += bean.getScore();
            }

            String court = bean.getMatchBean().getMatchBean().getCourt();
            // count court score
            if (court.equals(arrCourt[1])) {
                countClay += bean.getScore();
            }
            else if (court.equals(arrCourt[2])) {
                countGrass += bean.getScore();
            }
            else if (court.equals(arrCourt[3])) {
                countInHard += bean.getScore();
            }
            else {
                countHard += bean.getScore();
            }
        }

        scorePageData.setCountScore(countScore);
        scorePageData.setCountScoreClay(countClay);
        scorePageData.setCountScoreGrass(countGrass);
        scorePageData.setCountScoreInHard(countInHard);
        scorePageData.setCountScoreHard(countHard);
        scorePageData.setCountScoreYear(countYear);
        scorePageData.setCountScoreLastYear(countLastYear);

        scorePageView.onPageDataLoaded(scorePageData);
    }

    public ScoreBean getPunishScoreBean() {
        ScoreBean bean = new ScoreBean();
        bean.setYear(currentYear);
        bean.setMatchBean(null);
        bean.setCompleted(false);
        bean.setChampion(false);
        bean.setScore(0);
        return bean;
    }

    public int getThisYear() {
        return thisYear;
    }

    public void setCurrentYear(int year) {
        this.currentYear = year;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public ScoreComparator getScoreComparator() {
        return scoreComparator;
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
                lMatchSeq = lhs.getMatchBean().getMatchBean().getWeek();
            }
            if (rhs.getMatchBean() != null) {
                rMatchSeq = rhs.getMatchBean().getMatchBean().getWeek();
            }
            return lMatchSeq - rMatchSeq;
        }
    }
}
