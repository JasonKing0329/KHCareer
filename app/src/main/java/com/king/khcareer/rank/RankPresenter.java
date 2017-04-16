package com.king.khcareer.rank;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/5 11:20
 */
public class RankPresenter {

    private RankFinalDao dao;

    public RankPresenter() {
        dao = new RankFinalDao();
    }

    public List<RankFinalBean> getRankList() {
        return dao.queryRanks();
    }

    public void saveRankFinal(RankFinalBean bean) {
        if (bean.getId() == 0) {
            dao.addRank(bean);
        }
        else {
            dao.updateRank(bean);
        }
    }

    public void deleteRank(RankFinalBean bean) {
        dao.deleteRank(bean);
    }
}
