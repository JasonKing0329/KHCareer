package com.king.khcareer.player.manage;

import com.king.khcareer.model.sql.player.interfc.H2HDAO;
import com.king.khcareer.model.sql.player.H2HDAODB;
import com.king.khcareer.common.multiuser.MultiUserManager;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/14 11:40
 */
public class CommonPresenter {

    private ICommonView commonView;

    public CommonPresenter(ICommonView commonView) {
        this.commonView = commonView;
    }

    public void loadH2hs(String competitor) {
        H2HDAO dao = new H2HDAODB(competitor, MultiUserManager.getInstance().getUserKing());
        commonView.onUserH2hLoaded(MultiUserManager.getInstance().getUserKing(), dao.getWin(), dao.getLose());
        dao = new H2HDAODB(competitor, MultiUserManager.getInstance().getUserFlamenco());
        commonView.onUserH2hLoaded(MultiUserManager.getInstance().getUserFlamenco(), dao.getWin(), dao.getLose());
        dao = new H2HDAODB(competitor, MultiUserManager.getInstance().getUserHenry());
        commonView.onUserH2hLoaded(MultiUserManager.getInstance().getUserHenry(), dao.getWin(), dao.getLose());
        dao = new H2HDAODB(competitor, MultiUserManager.getInstance().getUserQi());
        commonView.onUserH2hLoaded(MultiUserManager.getInstance().getUserQi(), dao.getWin(), dao.getLose());
    }


}
