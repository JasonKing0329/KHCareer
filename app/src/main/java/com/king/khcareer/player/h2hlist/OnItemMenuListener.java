package com.king.khcareer.player.h2hlist;

import android.view.View;

import com.king.khcareer.model.sql.player.bean.H2hParentBean;

/**
 * Created by Administrator on 2017/4/22 0022.
 */

public interface OnItemMenuListener {
    void onItemClicked(View v, H2hParentBean bean);
}
