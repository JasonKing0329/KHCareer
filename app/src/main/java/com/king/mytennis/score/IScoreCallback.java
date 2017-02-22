package com.king.mytennis.score;

import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/22 16:27
 */
public interface IScoreCallback {
    void on52WeekRecordsLoaded(List<ScoreBean> list, List<String> nonExistMatchList);
    void onYearRecordsLoaded(List<ScoreBean> list, List<String> nonExistMatchList);
}
