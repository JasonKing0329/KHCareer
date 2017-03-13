package com.king.mytennis.view.editor;

import android.app.Activity;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/13 14:38
 */
public interface IEditorHolder {
    Activity getActivity();

    String getCompetitor();

    void selectPlayer();

    void selectMatch();

}
