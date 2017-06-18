package com.king.khcareer.model.sql.player.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/18 0018.
 */

public class MatchIdBean {
    private int id;
    private int week;
    private List<String> names;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
