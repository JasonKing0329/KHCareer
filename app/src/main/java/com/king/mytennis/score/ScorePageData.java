package com.king.mytennis.score;

import java.util.List;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/21 14:07
 */
public class ScorePageData {
    private List<ScoreBean> scoreList;
    private int countScore;
    private int countScoreYear;
    private int countScoreLastYear;
    private int countScoreHard;
    private int countScoreClay;
    private int countScoreGrass;
    private int countScoreInHard;
    private List<String> nonExistMatchList;
    private List<ScoreBean> gsList;
    private List<ScoreBean> masterCupList;
    private List<ScoreBean> atp1000List;
    private List<ScoreBean> atp500List;
    private List<ScoreBean> atp250List;
    private List<ScoreBean> replaceList;
    private List<ScoreBean> otherList;

    private int rank;

    public List<ScoreBean> getScoreList() {
        return scoreList;
    }

    public void setScoreList(List<ScoreBean> scoreList) {
        this.scoreList = scoreList;
    }

    public int getCountScore() {
        return countScore;
    }

    public void setCountScore(int countScore) {
        this.countScore = countScore;
    }

    public int getCountScoreClay() {
        return countScoreClay;
    }

    public void setCountScoreClay(int countScoreClay) {
        this.countScoreClay = countScoreClay;
    }

    public int getCountScoreGrass() {
        return countScoreGrass;
    }

    public void setCountScoreGrass(int countScoreGrass) {
        this.countScoreGrass = countScoreGrass;
    }

    public int getCountScoreHard() {
        return countScoreHard;
    }

    public void setCountScoreHard(int countScoreHard) {
        this.countScoreHard = countScoreHard;
    }

    public int getCountScoreInHard() {
        return countScoreInHard;
    }

    public void setCountScoreInHard(int countScoreInHard) {
        this.countScoreInHard = countScoreInHard;
    }

    public int getCountScoreLastYear() {
        return countScoreLastYear;
    }

    public void setCountScoreLastYear(int countScoreLastYear) {
        this.countScoreLastYear = countScoreLastYear;
    }

    public int getCountScoreYear() {
        return countScoreYear;
    }

    public void setCountScoreYear(int countScoreYear) {
        this.countScoreYear = countScoreYear;
    }

    public List<String> getNonExistMatchList() {
        return nonExistMatchList;
    }

    public void setNonExistMatchList(List<String> nonExistMatchList) {
        this.nonExistMatchList = nonExistMatchList;
    }

    public List<ScoreBean> getMasterCupList() {
        return masterCupList;
    }

    public void setMasterCupList(List<ScoreBean> masterCupList) {
        this.masterCupList = masterCupList;
    }

    public List<ScoreBean> getAtp1000List() {
        return atp1000List;
    }

    public void setAtp1000List(List<ScoreBean> atp1000List) {
        this.atp1000List = atp1000List;
    }

    public List<ScoreBean> getAtp250List() {
        return atp250List;
    }

    public void setAtp250List(List<ScoreBean> atp250List) {
        this.atp250List = atp250List;
    }

    public List<ScoreBean> getAtp500List() {
        return atp500List;
    }

    public void setAtp500List(List<ScoreBean> atp500List) {
        this.atp500List = atp500List;
    }

    public List<ScoreBean> getGsList() {
        return gsList;
    }

    public void setGsList(List<ScoreBean> gsList) {
        this.gsList = gsList;
    }

    public List<ScoreBean> getOtherList() {
        return otherList;
    }

    public void setOtherList(List<ScoreBean> otherList) {
        this.otherList = otherList;
    }

    public List<ScoreBean> getReplaceList() {
        return replaceList;
    }

    public void setReplaceList(List<ScoreBean> replaceList) {
        this.replaceList = replaceList;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
