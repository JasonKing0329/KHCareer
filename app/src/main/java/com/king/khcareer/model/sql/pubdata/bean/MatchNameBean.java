package com.king.khcareer.model.sql.pubdata.bean;

public class MatchNameBean {

    private int id;
    private String name;
    private int matchId;
    private MatchBean matchBean;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public MatchBean getMatchBean() {
        return matchBean;
    }

    public void setMatchBean(MatchBean matchBean) {
        this.matchBean = matchBean;
    }
}
