package com.king.mytennis.player;

import com.king.mytennis.multiuser.MultiUser;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/14 11:51
 */
public interface ICommonView {
    void onUserH2hLoaded(MultiUser user, int win, int lose);
}
