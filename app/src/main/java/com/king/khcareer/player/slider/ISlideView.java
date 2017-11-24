package com.king.khcareer.player.slider;

import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/24 14:57
 */
public interface ISlideView {
    void onPlayerLoaded(List<PlayerBean> playerList);

    void onPlayerLoadFailed(String message);

    void onRecordLoaded(List<Object> list);
}
