package com.king.khcareer.model.sqlbrite;

import android.content.Context;

import com.king.khcareer.model.sql.player.bean.MatchIdBean;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.VirtualManager;
import com.king.khcareer.model.sql.pubdata.bean.MatchBean;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.khcareer.model.sqlbrite.dao.MatchDao;
import com.king.khcareer.model.sqlbrite.dao.PlayerDao;
import com.king.khcareer.utils.DebugLog;
import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.schedulers.Schedulers;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/27 13:11
 */
public class SqlBriteProvider implements PubDataProvider {

    private PubHelper pubHelper;
    private BriteDatabase pubDb;

    private MatchDao matchDao;
    private PlayerDao playerDao;

    public SqlBriteProvider(Context context) {
        pubHelper = new PubHelper(context);
        SqlBrite sqlBrite = new SqlBrite.Builder().logger(new SqlBrite.Logger() {
            @Override
            public void log(String message) {
                DebugLog.e(message);
            }
        }).build();
        pubDb = sqlBrite.wrapDatabaseHelper(pubHelper, Schedulers.io());
        pubDb.setLoggingEnabled(true);

        matchDao = new MatchDao(pubDb);
        playerDao = new PlayerDao(pubDb);
    }

    @Override
    public void close() {
        if (pubDb != null) {
            pubDb.close();
        }
        if (pubHelper != null) {
            pubHelper.close();
        }
    }

    @Override
    public List<PlayerBean> getPlayerList() {
        List<PlayerBean> list = getRealPlayerList();
        list.addAll(0, VirtualManager.getVirtualPlayer());
        return list;
    }

    @Override
    public List<PlayerBean> getRealPlayerList() {
        return playerDao.queryPlayerList(true);
    }

    @Override
    public PlayerBean getPlayerByChnName(String name) {
        // 先从virtual中查
        PlayerBean bean = VirtualManager.getVirtualPlayer(name);
        if (bean != null) {
            return  bean;
        }

        // 再从real中查
        return playerDao.queryPlayerByChnName(name);
    }

    @Override
    public List<MatchNameBean> getMatchList() {
        return matchDao.queryMatchList(null);
    }

    public List<MatchBean> getMatchDbList() {
        return matchDao.getMatchDbList();
    }

    public List<MatchNameBean> getMatchNameDbList() {
        return matchDao.getMatchNameDbList();
    }

    @Override
    public List<MatchNameBean> getMatchNameList(int matchId) {
        return matchDao.queryMatchNameList(matchId);
    }

    @Override
    public Map<Integer, MatchIdBean> loadMasterIdMap() {
        return matchDao.loadMasterIdMap();
    }

    @Override
    public List<MatchNameBean> getMatchListByLevel(String level) {
        return matchDao.queryMatchList(level);
    }

    @Override
    public Map<String, MatchNameBean> getMatchMap() {
        Map<String, MatchNameBean> map = new HashMap<>();
        List<MatchNameBean> list = matchDao.queryMatchList(null);
        for (MatchNameBean bean:list) {
            map.put(bean.getName(), bean);
        }
        return map;
    }

    @Override
    public void insertMatch(MatchNameBean bean) {
        matchDao.insertMatchBean(bean.getMatchBean());
        bean.setMatchId(matchDao.queryLastMatchBeanSequence());
        bean.getMatchBean().setId(bean.getMatchId());
        matchDao.insertMatchNameBean(bean);
        bean.setId(matchDao.queryLastMatchNameBeanSequence());
    }

    @Override
    public void updateMatch(MatchNameBean bean) {
        matchDao.updateMatchBean(bean.getMatchBean());
        matchDao.updateMatchNameBean(bean);
    }

    @Override
    public void deleteMatchName(MatchNameBean bean) {
        matchDao.deleteMatchName(bean.getId());
    }

    @Override
    public void deleteMatch(int matchId) {
        matchDao.deleteMatch(matchId);
        matchDao.deleteMatchNameByMatchId(matchId);
    }

    @Override
    public MatchNameBean getMatchByName(String match) {
        return matchDao.queryMatchByName(match);
    }

    @Override
    public void insertPlayer(PlayerBean bean) {
        playerDao.inserPlayerBean(bean);
    }

    @Override
    public void updatePlayer(PlayerBean bean) {
        playerDao.updatePlayerBean(bean);
    }

    @Override
    public void deletePlayer(PlayerBean bean) {
        playerDao.deletePlayer(bean.getId());
    }
}
