package com.king.mytennis.pubdata;

import com.king.mytennis.model.Configuration;
import com.king.mytennis.model.DatabaseStruct;
import com.king.mytennis.pubdata.bean.MatchNameBean;
import com.king.mytennis.pubdata.bean.PlayerBean;
import com.king.mytennis.pubdata.dao.MatchDao;
import com.king.mytennis.pubdata.dao.PlayerDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            List<PlayerBean> list = new PlayerDao().queryPlayerList(SqlConnection.getInstance().getConnection(), true);
            addVirtualPlayer(list);
            return list;
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
            return new MatchDao().queryMatchList(SqlConnection.getInstance().getConnection(), null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }


    public List<MatchNameBean> getMatchListByLevel(String level) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            return new MatchDao().queryMatchList(SqlConnection.getInstance().getConnection(), level);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
        return null;
    }

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
            SqlConnection.getInstance().close();
        }
        return map;
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

    public void insertPlayer(PlayerBean bean) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            new PlayerDao().inserPlayerBean(bean, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }

    public void updatePlayer(PlayerBean bean) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            new PlayerDao().updatePlayerBean(bean, SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }

    public void deletePlayer(PlayerBean bean) {
        try {
            SqlConnection.getInstance().connect(databasePath);
            new PlayerDao().deletePlayer(bean.getId(), SqlConnection.getInstance().getConnection());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SqlConnection.getInstance().close();
        }
    }

    private void addVirtualPlayer(List<PlayerBean> list) {
        PlayerBean bean = new PlayerBean();
        bean.setId(100001);
        bean.setNameChn("King Hao");
        bean.setNameEng("King Hao");
        bean.setNamePinyin("king hao");
        bean.setCountry("中国");
        bean.setCity("上海");
        bean.setBirthday("1991-03-29");
        list.add(0, bean);
        bean = new PlayerBean();
        bean.setId(100002);
        bean.setNameChn("John Flamenco");
        bean.setNameEng("John Flamenco");
        bean.setNamePinyin("john flamenco");
        bean.setCountry("法国");
        bean.setCity("巴黎");
        bean.setBirthday("1997-05-04");
        list.add(1, bean);
        bean = new PlayerBean();
        bean.setId(100003);
        bean.setNameChn("Michael Henry");
        bean.setNameEng("Michael Henry");
        bean.setNamePinyin("Michael Henry");
        bean.setCountry("美国");
        bean.setCity("芝加哥");
        bean.setBirthday("1998-05-26");
        list.add(2, bean);
        bean = new PlayerBean();
        bean.setId(100004);
        bean.setNameChn("Qi Tian");
        bean.setNameEng("Qi Tian");
        bean.setNamePinyin("Qi Tian");
        bean.setCountry("中国");
        bean.setCity("成都");
        bean.setBirthday("1991-03-27");
        list.add(3, bean);
    }
}
