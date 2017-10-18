package com.king.khcareer.match.manage;

import com.king.khcareer.common.multiuser.MultiUser;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/14 11:51
 */
public interface ICommonView {
    void onMatchHistoryLoaded(MultiUser user, int win, int lose);
}
