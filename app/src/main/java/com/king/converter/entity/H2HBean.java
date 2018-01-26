package com.king.converter.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/1/26 15:52
 */
@Entity(nameInDb = "h2h")
public class H2HBean {

    @Id(autoincrement = true)
    private Long id;

    private long userId;

    private long playerId;

    private int win;

    private int lose;

    private int total;

    @Generated(hash = 858867441)
    public H2HBean(Long id, long userId, long playerId, int win, int lose,
            int total) {
        this.id = id;
        this.userId = userId;
        this.playerId = playerId;
        this.win = win;
        this.lose = lose;
        this.total = total;
    }

    @Generated(hash = 380656635)
    public H2HBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public int getWin() {
        return this.win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLose() {
        return this.lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
