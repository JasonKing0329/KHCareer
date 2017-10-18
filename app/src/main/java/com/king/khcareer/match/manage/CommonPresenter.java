package com.king.khcareer.match.manage;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.common.multiuser.MultiUser;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.player.RecordDAOImp;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.player.interfc.RecordDAO;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/14 11:40
 */
public class CommonPresenter {

    private ICommonView commonView;

    private PubDataProvider pubDataProvider;

    public CommonPresenter(ICommonView commonView) {
        this.commonView = commonView;
        pubDataProvider = new PubDataProvider();
    }

    public void loadHistory(String match) {
        MatchNameBean nameBean = pubDataProvider.getMatchByName(match);
        List<MatchNameBean> matchList = pubDataProvider.getMatchNameList(nameBean.getMatchId());
        loadUsers(matchList, MultiUserManager.getInstance().getUserKing());
        loadUsers(matchList, MultiUserManager.getInstance().getUserFlamenco());
        loadUsers(matchList, MultiUserManager.getInstance().getUserHenry());
        loadUsers(matchList, MultiUserManager.getInstance().getUserQi());
    }

    private void loadUsers(List<MatchNameBean> matchList, MultiUser user) {
        List<Record> list = new ArrayList<>();
        RecordDAO dao = new RecordDAOImp(user);
        for (MatchNameBean bean:matchList) {
            list.addAll(dao.queryByMatch(bean.getName()));
        }

        int win = 0, lose = 0;
        for (Record record:list) {
            // W/0不算作胜负场
            if (!Constants.SCORE_RETIRE.equals(record.getScore())) {
                if (record.getWinner().equals(MultiUserManager.USER_DB_FLAG)) {
                    win ++;
                }
                else {
                    lose ++;
                }
            }
        }
        commonView.onMatchHistoryLoaded(user, win, lose);
    }


    public MatchNameBean getMatchNameBean(String match) {
        return pubDataProvider.getMatchByName(match);
    }
}
