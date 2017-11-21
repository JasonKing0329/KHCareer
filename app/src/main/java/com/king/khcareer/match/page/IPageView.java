package com.king.khcareer.match.page;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/21 14:14
 */
public interface IPageView {
    void showMatchInfo(String name, String country, String city, String level, String court);

    void showError(String msg);

    void onRecordsLoaded(List<Object> list, int win, int lose);
}
