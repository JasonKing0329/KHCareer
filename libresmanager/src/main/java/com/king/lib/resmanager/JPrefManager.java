package com.king.lib.resmanager;

import android.content.SharedPreferences;

import com.king.lib.resmanager.action.JPrefAction;
import com.king.lib.resmanager.exception.JResParseException;
import com.king.lib.resmanager.impl.JDom4jPrefImpl;

/**
 * Created by JingYang on 2016/7/20 0020.
 * Description:
 */
public class JPrefManager {

    private JPrefAction jPrefAction;
    public void loadExtendPreference(String filePath, SharedPreferences preferences) throws JResParseException {
        jPrefAction = new JDom4jPrefImpl();
        jPrefAction.loadExtendPreference(filePath, preferences);
    }
}
