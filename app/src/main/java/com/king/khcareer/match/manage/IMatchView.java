package com.king.khcareer.match.manage;

import com.king.khcareer.model.sql.pubdata.bean.MatchNameBean;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/9 14:16
 */
public interface IMatchView {
    void onLoadMatchList(List<MatchNameBean> list);
    void onSortFinished();
}
