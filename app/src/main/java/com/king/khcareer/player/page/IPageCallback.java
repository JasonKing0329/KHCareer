package com.king.khcareer.player.page;

import java.util.List;

/**
 * 描述: the callback of PageFragment for presenter operation
 * <p/>作者：景阳
 * <p/>创建时间: 2017/11/21 9:20
 */
public interface IPageCallback {
    void onDataLoaded(List<Object> list);
}
