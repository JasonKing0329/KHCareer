package com.king.khcareer.match.gallery;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;

import com.king.khcareer.match.gallery.GradientBkView;
import com.king.khcareer.match.gallery.ScrollManager;

/**
 * Created by Administrator on 2017/4/4 0004.
 * UserMatchActivity执行的赛事切换行为变化
 */

public class MatchScrollManager extends ScrollManager {

    private GradientBkView vMatchBk;
    private FloatingActionButton fab;

    public MatchScrollManager(Context context) {
        super(context);
    }

    /**
     * 绑定待变化的view
     *
     * @param vMatchBk
     * @param fab
     */
    public void bindBehaviorView(GradientBkView vMatchBk, FloatingActionButton fab) {
        this.vMatchBk = vMatchBk;
        this.fab = fab;
    }

    @Override
    protected void updateBehaviors(int[] color) {

        // 渐变背景
        vMatchBk.updateGradientValues(color);

        fab.setRippleColor(color[2]);
        // 背景色需要调用这个方法，setBackgroundColor不管用
        fab.setBackgroundTintList(ColorStateList.valueOf(color[0]));
    }
}
