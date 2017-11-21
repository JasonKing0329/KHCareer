package com.king.khcareer.player.page;

import java.util.List;

/**
 * 描述: the view interface of PlayerPageActivity
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/20 15:49
 */
public interface IPageView {
    void showPlayerInfo(String engName, String info, String imagePath, String country);

    void showError(String s);

    void onTabLoaded(List<TabBean> list);
}
