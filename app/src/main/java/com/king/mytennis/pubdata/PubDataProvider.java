package com.king.mytennis.pubdata;

import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.DatabaseStruct;
import com.king.mytennis.pubdata.bean.MatchNameBean;
import com.king.mytennis.pubdata.bean.PlayerBean;
import com.king.mytennis.pubdata.dao.MatchDao;
import com.king.mytennis.pubdata.dao.PlayerDao;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/24 14:09
 */
public class PubDataProvider {
    private String databasePath;

    public PubDataProvider() {
        databasePath = Configuration.CONF_DIR + DatabaseStruct.DATABASE_PUBLIC;
    }

    public List<PlayerBean> getPlayerList(){
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new PlayerDao().queryPlayerList(SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

    public List<MatchNameBean> getMatchList(){
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new MatchDao().queryMatchList(SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

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
            SqlConnection.getInstance().close();
        }
    }

    public void updateMatch(MatchNameBean bean) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            MatchDao dao = new MatchDao();
            dao.updateMatchBean(bean.getMatchBean(), SqlConnection.getInstance().getConnection());
            dao.updateMatchNameBean(bean, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }

    /**
     * 只删除match name
     * @param bean
     */
    public void deleteMatchName(MatchNameBean bean) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            MatchDao dao = new MatchDao();
            dao.deleteMatchName(bean.getId(), SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }

    /**
     * 删除matchId，则在match表和match name表都删除
     * @param matchId
     */
    public void deleteMatch(int matchId) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            MatchDao dao = new MatchDao();
            dao.deleteMatch(matchId, SqlConnection.getInstance().getConnection());
            dao.deleteMatchNameByMatchId(matchId, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }

    public MatchNameBean getMatchByName(String match) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new MatchDao().queryMatchByName(match, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }
}
