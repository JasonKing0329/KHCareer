package com.king.khcareer.player.h2hlist;

import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;

/**
 * Created by Administrator on 2017/4/30 0030.
 */

public class H2hHeaderBean {
    private String player;
    private String country;
    private int win;
    private int lose;
    private PlayerBean playerBean;

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
