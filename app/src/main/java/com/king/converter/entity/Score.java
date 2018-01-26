package com.king.converter.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/1/26 15:42
 */
@Entity(nameInDb = "scores")
public class Score {

    @Id(autoincrement = true)
    private Long id;

    private long recordId;

    private int setNo;

    private int userPoint;

    private int competitorPoint;

    private boolean isTiebreak;

    private int userTiebreak;

    private int competitorTiebreak;

    @Generated(hash = 338008087)
    public Score(Long id, long recordId, int setNo, int userPoint,
            int competitorPoint, boolean isTiebreak, int userTiebreak,
            int competitorTiebreak) {
        this.id = id;
        this.recordId = recordId;
        this.setNo = setNo;
        this.userPoint = userPoint;
        this.competitorPoint = competitorPoint;
        this.isTiebreak = isTiebreak;
        this.userTiebreak = userTiebreak;
        this.competitorTiebreak = competitorTiebreak;
    }

    @Generated(hash = 226049941)
    public Score() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getRecordId() {
        return this.recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public int getSetNo() {
        return this.setNo;
    }

    public void setSetNo(int setNo) {
        this.setNo = setNo;
    }

    public int getUserPoint() {
        return this.userPoint;
    }

    public void setUserPoint(int userPoint) {
        this.userPoint = userPoint;
    }

    public int getCompetitorPoint() {
        return this.competitorPoint;
    }

    public void setCompetitorPoint(int competitorPoint) {
        this.competitorPoint = competitorPoint;
    }

    public boolean getIsTiebreak() {
        return this.isTiebreak;
    }

    public void setIsTiebreak(boolean isTiebreak) {
        this.isTiebreak = isTiebreak;
    }

    public int getUserTiebreak() {
        return this.userTiebreak;
    }

    public void setUserTiebreak(int userTiebreak) {
        this.userTiebreak = userTiebreak;
    }

    public int getCompetitorTiebreak() {
        return this.competitorTiebreak;
    }

    public void setCompetitorTiebreak(int competitorTiebreak) {
        this.competitorTiebreak = competitorTiebreak;
    }

}
