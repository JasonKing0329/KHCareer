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
}
