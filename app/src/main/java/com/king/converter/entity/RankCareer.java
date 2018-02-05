package com.king.converter.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/2/5 9:27
 */
@Entity(nameInDb = "rank_career")
public class RankCareer {

    @Id(autoincrement = true)
    private Long id;

    private long userId;

    private int rankCurrent;

    private int rankHighest;

    private int top1Week;

    @Generated(hash = 1602926534)
    public RankCareer(Long id, long userId, int rankCurrent, int rankHighest,
            int top1Week) {
        this.id = id;
        this.userId = userId;
        this.rankCurrent = rankCurrent;
        this.rankHighest = rankHighest;
        this.top1Week = top1Week;
    }

    @Generated(hash = 66041825)
    public RankCareer() {
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

    public int getRankCurrent() {
        return this.rankCurrent;
    }

    public void setRankCurrent(int rankCurrent) {
        this.rankCurrent = rankCurrent;
    }

    public int getRankHighest() {
        return this.rankHighest;
    }

    public void setRankHighest(int rankHighest) {
        this.rankHighest = rankHighest;
    }

    public int getTop1Week() {
        return this.top1Week;
    }

    public void setTop1Week(int top1Week) {
        this.top1Week = top1Week;
    }

}
