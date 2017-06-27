package com.king.khcareer.model.sql.player.bean;

import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/6/27 13:55
 */
public class H2hParentBean {
    private int id;
    private String player;
    private String country;
    private int total;
    private int win;
    private int lose;
    private PlayerBean playerBean;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public PlayerBean getPlayerBean() {
        return playerBean;
    }

    public void setPlayerBean(PlayerBean playerBean) {
        this.playerBean = playerBean;
    }
}
