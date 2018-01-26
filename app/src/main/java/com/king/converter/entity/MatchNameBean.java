package com.king.converter.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@Entity(nameInDb = "match_names")
public class MatchNameBean {

    @Id(autoincrement = true)
    private Long id;

    private String name;

    private long matchId;

    @ToOne(joinProperty = "matchId")
    private MatchBean matchBean;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1326541218)
    private transient MatchNameBeanDao myDao;

    @Generated(hash = 1143436474)
    public MatchNameBean(Long id, String name, long matchId) {
        this.id = id;
        this.name = name;
        this.matchId = matchId;
    }

    @Generated(hash = 545912728)
    public MatchNameBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMatchId() {
        return this.matchId;
    }

    public void setMatchId(long matchId) {
        this.matchId = matchId;
    }

    @Generated(hash = 86593835)
    private transient Long matchBean__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 677076984)
    public MatchBean getMatchBean() {
        long __key = this.matchId;
        if (matchBean__resolvedKey == null
                || !matchBean__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MatchBeanDao targetDao = daoSession.getMatchBeanDao();
            MatchBean matchBeanNew = targetDao.load(__key);
            synchronized (this) {
                matchBean = matchBeanNew;
                matchBean__resolvedKey = __key;
            }
        }
        return matchBean;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1707161225)
    public void setMatchBean(@NotNull MatchBean matchBean) {
        if (matchBean == null) {
            throw new DaoException(
                    "To-one property 'matchId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.matchBean = matchBean;
            matchId = matchBean.getId();
            matchBean__resolvedKey = matchId;
        }
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
    @Generated(hash = 481788100)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMatchNameBeanDao() : null;
    }

}
