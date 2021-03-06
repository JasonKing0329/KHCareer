package com.king.khcareer.home.k4;

import android.content.Context;

import com.king.khcareer.match.gallery.GradientBkView;
import com.king.khcareer.match.gallery.ScrollManager;

/**
 * Created by Administrator on 2017/4/4 0004.
 * 主页执行的赛事切换行为变化
 */

public class HomeMatchScrollManager extends ScrollManager {

    private GradientBkView vMatchBk;

    public HomeMatchScrollManager(Context context) {
        super(context);
    }

    /**
     * 绑定待变化的view
     *
     * @param vMatchBk
     */
    public void bindBehaviorView(GradientBkView vMatchBk) {
        this.vMatchBk = vMatchBk;
    }

    @Override
    protected void updateBehaviors(int[] color) {
        // 渐变背景
        vMatchBk.updateGradientValues(color);
    }
}
