package com.king.converter.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2018/1/26 15:25
 */
@Entity(nameInDb = "match_records")
public class Record {

    @Id(autoincrement = true)
    private Long id;

    private long userId;

    @ToOne(joinProperty = "userId")
    private User user;

    private int rankCpt;

    private int rank;

    private int seedpCpt;

    private int seed;

    private long playerId;

    @ToOne(joinProperty = "playerId")
    private PlayerBean competitor;

    private long matchNameId;

    @ToOne(joinProperty = "matchNameId")
    private MatchNameBean match;

    private String dateStr;

    private long dateLong;

    private String round;

    @ToMany(referencedJoinProperty = "recordId")
    private List<Score> scoreList;

    /**
     * 0: user
     * 1: competitor
     */
    private long winnerFlag;

    /**
     * 0: no retire
     * 1: retire with score
     * 2: retire before match(W/0)
     */
    private long retireFlag;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 765166123)
    private transient RecordDao myDao;

    @Generated(hash = 350023155)
    public Record(Long id, long userId, int rankCpt, int rank, int seedpCpt,
            int seed, long playerId, long matchNameId, String dateStr,
            long dateLong, String round, long winnerFlag, long retireFlag) {
        this.id = id;
        this.userId = userId;
        this.rankCpt = rankCpt;
        this.rank = rank;
        this.seedpCpt = seedpCpt;
        this.seed = seed;
        this.playerId = playerId;
        this.matchNameId = matchNameId;
        this.dateStr = dateStr;
        this.dateLong = dateLong;
        this.round = round;
        this.winnerFlag = winnerFlag;
        this.retireFlag = retireFlag;
    }

    @Generated(hash = 477726293)
    public Record() {
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

    public int getRankCpt() {
        return this.rankCpt;
    }

    public void setRankCpt(int rankCpt) {
        this.rankCpt = rankCpt;
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getSeedpCpt() {
        return this.seedpCpt;
    }

    public void setSeedpCpt(int seedpCpt) {
        this.seedpCpt = seedpCpt;
    }

    public int getSeed() {
        return this.seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public long getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getMatchNameId() {
        return this.matchNameId;
    }

    public void setMatchNameId(long matchNameId) {
        this.matchNameId = matchNameId;
    }

    public String getDateStr() {
        return this.dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public long getDateLong() {
        return this.dateLong;
    }

    public void setDateLong(long dateLong) {
        this.dateLong = dateLong;
    }

    public String getRound() {
        return this.round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public long getWinnerFlag() {
        return this.winnerFlag;
    }

    public void setWinnerFlag(long winnerFlag) {
        this.winnerFlag = winnerFlag;
    }

    public long getRetireFlag() {
        return this.retireFlag;
    }

    public void setRetireFlag(long retireFlag) {
        this.retireFlag = retireFlag;
    }

    @Generated(hash = 251390918)
    private transient Long user__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 115391908)
    public User getUser() {
        long __key = this.userId;
        if (user__resolvedKey == null || !user__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            UserDao targetDao = daoSession.getUserDao();
            User userNew = targetDao.load(__key);
            synchronized (this) {
                user = userNew;
                user__resolvedKey = __key;
            }
        }
        return user;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 462495677)
    public void setUser(@NotNull User user) {
        if (user == null) {
            throw new DaoException(
                    "To-one property 'userId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.user = user;
            userId = user.getId();
            user__resolvedKey = userId;
        }
    }

    @Generated(hash = 1040442779)
    private transient Long competitor__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 73011782)
    public PlayerBean getCompetitor() {
        long __key = this.playerId;
        if (competitor__resolvedKey == null
                || !competitor__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PlayerBeanDao targetDao = daoSession.getPlayerBeanDao();
            PlayerBean competitorNew = targetDao.load(__key);
            synchronized (this) {
                competitor = competitorNew;
                competitor__resolvedKey = __key;
            }
        }
        return competitor;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1202505931)
    public void setCompetitor(@NotNull PlayerBean competitor) {
        if (competitor == null) {
            throw new DaoException(
                    "To-one property 'playerId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.competitor = competitor;
            playerId = competitor.getId();
            competitor__resolvedKey = playerId;
        }
    }

    @Generated(hash = 74816300)
    private transient Long match__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 859806891)
    public MatchNameBean getMatch() {
        long __key = this.matchNameId;
        if (match__resolvedKey == null || !match__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MatchNameBeanDao targetDao = daoSession.getMatchNameBeanDao();
            MatchNameBean matchNew = targetDao.load(__key);
            synchronized (this) {
                match = matchNew;
                match__resolvedKey = __key;
            }
        }
        return match;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1791484198)
    public void setMatch(@NotNull MatchNameBean match) {
        if (match == null) {
            throw new DaoException(
                    "To-one property 'matchNameId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.match = match;
            matchNameId = match.getId();
            match__resolvedKey = matchNameId;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 294390383)
    public List<Score> getScoreList() {
        if (scoreList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ScoreDao targetDao = daoSession.getScoreDao();
            List<Score> scoreListNew = targetDao._queryRecord_ScoreList(id);
            synchronized (this) {
                if (scoreList == null) {
                    scoreList = scoreListNew;
                }
            }
        }
        return scoreList;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 2074989865)
    public synchronized void resetScoreList() {
        scoreList = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1505145191)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRecordDao() : null;
    }

}
