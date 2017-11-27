package com.king.khcareer.model.sql.pubdata;

import com.king.khcareer.model.sql.player.bean.MatchIdBean;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;

import java.util.List;
import java.util.Map;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/24 14:09
 */
public interface PubDataProvider {

    /**
     * 对应getVirtualPlayer数量
     */
    int VIRTUAL_PLAYER = 4;

    void close();

    /**
     * 获取player列表，包括virtual player
     * @return
     */
    List<PlayerBean> getPlayerList();

    /**
     * 获取player列表，不包括virtual player
     * @return
     */
    List<PlayerBean> getRealPlayerList();

    /**
     * 从real和virtual中都要查询
     * @param name
     * @return
     */
    PlayerBean getPlayerByChnName(String name);

    List<MatchNameBean> getMatchList();

    List<MatchNameBean> getMatchNameList(int matchId);

    Map<Integer, MatchIdBean> loadMasterIdMap();

    List<MatchNameBean> getMatchListByLevel(String level);

    Map<String, MatchNameBean> getMatchMap();

    void insertMatch(MatchNameBean bean);

    void updateMatch(MatchNameBean bean);

    /**
     * 只删除match name
     * @param bean
     */
    void deleteMatchName(MatchNameBean bean) ;

    /**
     * 删除matchId，则在match表和match name表都删除
     * @param matchId
     */
    void deleteMatch(int matchId);

    MatchNameBean getMatchByName(String match);

    void insertPlayer(PlayerBean bean);

    void updatePlayer(PlayerBean bean);

    void deletePlayer(PlayerBean bean);

}
