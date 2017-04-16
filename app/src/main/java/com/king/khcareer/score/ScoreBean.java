package com.king.khcareer.score;

import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/20 17:42
 */
public class ScoreBean {
    private int score;
    private int year;
    private boolean isChampion;
    private boolean isCompleted;
    private MatchNameBean matchBean;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public MatchNameBean getMatchBean() {
        return matchBean;
    }

    public void setMatchBean(MatchNameBean matchBean) {
        this.matchBean = matchBean;
    }

    public boolean isChampion() {
        return isChampion;
    }

    public void setChampion(boolean champion) {
        isChampion = champion;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
