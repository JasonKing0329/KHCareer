package com.king.mytennis.score.rank;

import android.content.Context;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/4/5 11:20
 */
public class RankPresenter {

    private RankFinalDao dao;

    public RankPresenter(Context context) {
        dao = new RankFinalDao(context);
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
