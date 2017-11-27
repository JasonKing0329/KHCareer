package com.king.khcareer.home.k4;

import android.content.Context;

import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/3 0003.
 */

public interface IHomeView {
    void onHomeDataLoaded(HomeData data);
    Context getContext();

    void onRecordUpdated(List<Record> list);
    void onPlayerUpdated(List<PlayerBean> list);
}
