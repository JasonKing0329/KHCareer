package com.king.khcareer.model.sql.player.bean;

/**
 * Created by Administrator on 2017/6/18 0018.
 */

public class MatchResultBean {
    private String match;
    private String date;
    private String court;
    private String result;

    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
