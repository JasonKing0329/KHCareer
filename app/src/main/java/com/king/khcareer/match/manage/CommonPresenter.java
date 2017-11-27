package com.king.khcareer.match.manage;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.common.multiuser.MultiUser;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.PubProviderHelper;
import com.king.khcareer.model.sql.player.RecordDAOImp;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.player.interfc.RecordDAO;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;

import java.util.ArrayList;
import java.util.Collections;
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
        pubDataProvider = PubProviderHelper.getProvider();
    }

    public void loadHistory(String match) {
        // 通过赛事名称获取matchId
        MatchNameBean nameBean = pubDataProvider.getMatchByName(match);
        // 通过matchId获取实际的所有赛事
        List<MatchNameBean> matchList = pubDataProvider.getMatchNameList(nameBean.getMatchId());

        loadUsers(matchList, MultiUserManager.getInstance().getUserKing());
        loadUsers(matchList, MultiUserManager.getInstance().getUserFlamenco());
        loadUsers(matchList, MultiUserManager.getInstance().getUserHenry());
        loadUsers(matchList, MultiUserManager.getInstance().getUserQi());
    }

    /**
     * 统计user对应的胜负场次、参赛年份
     * @param matchList
     * @param user
     */
    private void loadUsers(List<MatchNameBean> matchList, MultiUser user) {
        List<Record> list = new ArrayList<>();
        RecordDAO dao = new RecordDAOImp(user);
        // 查询matchId对应所有赛事的record
        for (MatchNameBean bean:matchList) {
            list.addAll(dao.queryByMatch(bean.getName()));
        }

        List<Integer> yearList = new ArrayList<>();
        int win = 0, lose = 0;
        for (Record record:list) {

            // 统计参赛年份
            int year = Integer.parseInt(record.getStrDate().split("-")[0]);
            if (!yearList.contains(year)) {
                yearList.add(year);
            }

            // 统计胜负场次
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

        // 参赛年份按升序排列
        String years = null;
        Collections.sort(yearList);
        for (Integer year:yearList) {
            if (years == null) {
                years = String.valueOf(year);
            }
            else {
                years = years.concat(", ").concat(String.valueOf(year));
            }
        }

        commonView.onMatchHistoryLoaded(user, win, lose, years);
    }


    public MatchNameBean getMatchNameBean(String match) {
        return pubDataProvider.getMatchByName(match);
    }
}
