package com.king.khcareer.home.k4;

import android.content.Context;

/**
 * Created by Administrator on 2017/4/3 0003.
 */

public interface IHomeView {
    void onHomeDataLoaded(HomeData data);
    void onScoreLoaded(int score, int rank);
    Context getContext();
}
