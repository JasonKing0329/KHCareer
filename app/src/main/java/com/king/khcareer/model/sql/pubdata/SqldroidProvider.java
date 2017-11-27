package com.king.khcareer.model.sql.pubdata;

import com.king.khcareer.common.config.Configuration;
import com.king.khcareer.model.sql.player.DatabaseStruct;
import com.king.khcareer.model.sql.player.bean.MatchIdBean;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.khcareer.model.sql.pubdata.dao.MatchDao;
import com.king.khcareer.model.sql.pubdata.dao.PlayerDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/24 14:09
 */
public class SqldroidProvider implements PubDataProvider {

    /**
     * 对应getVirtualPlayer数量
     */
    public static final int VIRTUAL_PLAYER = 4;

    private String databasePath;

    public SqldroidProvider() {
        databasePath = Configuration.CONF_DIR + DatabaseStruct.DATABASE_PUBLIC;
    }

    @Override
    public void close() {
        SqlConnection.getInstance().close();
    }

    /**
     * 获取player列表，包括virtual player
     * @return
     */
    @Override
    public List<PlayerBean> getPlayerList(){
        try {
            SqlConnection.getInstance().connect(databasePath);
            List<PlayerBean> list = new PlayerDao().queryPlayerList(SqlConnection.getInstance().getConnection(), true);
            list.addAll(0, VirtualManager.getVirtualPlayer());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    /**
     * 获取player列表，不包括virtual player
     * @return
     */
    @Override
    public List<PlayerBean> getRealPlayerList(){
        try {
            SqlConnection.getInstance().connect(databasePath);
            List<PlayerBean> list = new PlayerDao().queryPlayerList(SqlConnection.getInstance().getConnection(), true);
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    /**
     * 从real和virtual中都要查询
     * @param name
     * @return
     */
    @Override
    public PlayerBean getPlayerByChnName(String name) {
        // 先从virtual中查
        PlayerBean bean = VirtualManager.getVirtualPlayer(name);
        if (bean != null) {
            return  bean;
        }

        // 再从real中查
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new PlayerDao().queryPlayerByChnName(name, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    @Override
    public List<MatchNameBean> getMatchList(){
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new MatchDao().queryMatchList(SqlConnection.getInstance().getConnection(), null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    @Override
    public List<MatchNameBean> getMatchNameList(int matchId){
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new MatchDao().queryMatchNameList(matchId, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    @Override
    public Map<Integer, MatchIdBean> loadMasterIdMap() {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new MatchDao().loadMasterIdMap(SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    @Override
    public List<MatchNameBean> getMatchListByLevel(String level) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new MatchDao().queryMatchList(SqlConnection.getInstance().getConnection(), level);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    @Override
    public Map<String, MatchNameBean> getMatchMap() {
        Map<String, MatchNameBean> map = new HashMap<>();
        try {
            SqlConnection.getInstance().connect(databasePath);
            List<MatchNameBean> list = new MatchDao().queryMatchList(SqlConnection.getInstance().getConnection(), null);
            for (MatchNameBean bean:list) {
                map.put(bean.getName(), bean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return map;
    }

    @Override
    public void insertMatch(MatchNameBean bean) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            MatchDao dao = new MatchDao();
            dao.insertMatchBean(bean.getMatchBean(), SqlConnection.getInstance().getConnection());
            int matchId = dao.queryLastMatchBeanSequence(SqlConnection.getInstance().getConnection());
            bean.getMatchBean().setId(matchId);
            bean.setMatchId(matchId);
            dao.insertMatchNameBean(bean, SqlConnection.getInstance().getConnection());
            int id = dao.queryLastMatchNameBeanSequence(SqlConnection.getInstance().getConnection());
            bean.setId(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void updateMatch(MatchNameBean bean) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            MatchDao dao = new MatchDao();
            dao.updateMatchBean(bean.getMatchBean(), SqlConnection.getInstance().getConnection());
            dao.updateMatchNameBean(bean, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 只删除match name
     * @param bean
     */
    @Override
    public void deleteMatchName(MatchNameBean bean) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            MatchDao dao = new MatchDao();
            dao.deleteMatchName(bean.getId(), SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 删除matchId，则在match表和match name表都删除
     * @param matchId
     */
    @Override
    public void deleteMatch(int matchId) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            MatchDao dao = new MatchDao();
            dao.deleteMatch(matchId, SqlConnection.getInstance().getConnection());
            dao.deleteMatchNameByMatchId(matchId, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public MatchNameBean getMatchByName(String match) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new MatchDao().queryMatchByName(match, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    @Override
    public void insertPlayer(PlayerBean bean) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            new PlayerDao().inserPlayerBean(bean, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void updatePlayer(PlayerBean bean) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            new PlayerDao().updatePlayerBean(bean, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    @Override
    public void deletePlayer(PlayerBean bean) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            new PlayerDao().deletePlayer(bean.getId(), SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

}
