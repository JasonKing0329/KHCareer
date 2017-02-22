package com.king.mytennis.score;

import com.king.mytennis.match.MatchSeqBean;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/20 17:42
 */
public class ScoreBean {
    private String level;
    private String name;
    private String court;
    private int score;
    private int year;
    private int week;

    private MatchSeqBean matchBean;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public MatchSeqBean getMatchBean() {
        return matchBean;
    }

    public void setMatchBean(MatchSeqBean matchBean) {
        this.matchBean = matchBean;
    }
}
