package com.king.mytennis.player;

import com.king.mytennis.pubdata.bean.PlayerBean;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/9 14:02
 */
public interface IPlayerView {
    void onLoadPlayerList(List<PlayerBean> list);
    void onSortFinished();
}
