package com.king.khcareer.player.h2hlist;

import android.content.Context;
import android.view.View;

import com.king.khcareer.base.CustomDialog;

/**
 * Created by Administrator on 2017/5/1 0001.
 */

public class FilterDialog extends CustomDialog {

    public FilterDialog(Context context, OnCustomDialogActionListener actionListener) {
        super(context, actionListener);
    }

    @Override
    protected View getCustomView() {
        return null;
    }

    @Override
    protected View getCustomToolbar() {
        return null;
    }
}
