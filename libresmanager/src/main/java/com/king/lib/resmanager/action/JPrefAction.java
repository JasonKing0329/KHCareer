package com.king.lib.resmanager.action;

import android.content.SharedPreferences;

import com.king.lib.resmanager.exception.JResParseException;

/**
 * Created by JingYang on 2016/7/20 0020.
 * Description:
 */
public interface JPrefAction {
    void loadExtendPreference(String filePath, SharedPreferences preferences) throws JResParseException;
}
