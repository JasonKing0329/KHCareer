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
    private Map<String, List<ScoreBean>> levelMap;
    private int countScore;
    private int countScoreYear;
    private int countScoreLastYear;
    private int countScoreHard;
    private int countScoreClay;
    private int countScoreGrass;
    private int countScoreInHard;
    private List<String> nonExistMatchList;

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

    public Map<String, List<ScoreBean>> getLevelMap() {
        return levelMap;
    }

    public void setLevelMap(Map<String, List<ScoreBean>> levelMap) {
        this.levelMap = levelMap;
    }

    public List<String> getNonExistMatchList() {
        return nonExistMatchList;
    }

    public void setNonExistMatchList(List<String> nonExistMatchList) {
        this.nonExistMatchList = nonExistMatchList;
    }
}
