package com.king.khcareer;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.king.mytennis.view.R;

/**
 * Created by Administrator on 2017/4/2 0002.
 * 主页随着NestedScrollView滚动产生的相应变化事件
 * 采用继承AppBarLayout.ScrollingViewBehavior，扩展变化事件的方法
 * 完全遵循AppBarLayout.ScrollingViewBehavior改变collapse效果的同时，
 * 对dependency和child执行额外的变化效果：
 * 改变toolbar图标以及文字
 */

public class HomeBehavior extends AppBarLayout.ScrollingViewBehavior {

    /**
     * 收起多少高度时，显示ContentScrim的内容
     */
    private int scrimTop;

    public HomeBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 为负数
        scrimTop = context.getResources().getDimensionPixelSize(R.dimen.home_scrim_visible_height)
            - context.getResources().getDimensionPixelSize(R.dimen.player_basic_head_height);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        boolean result = super.layoutDependsOn(parent, child, dependency);
        return result;
    }

    /**
     *
     * @param parent
     * @param child NestedScrollView of layout_content_home
     * @param dependency AppBarLayout of layout_app_bar_home
     * @return
     */
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        boolean result = super.onDependentViewChanged(parent, child, dependency);

        // toolbar
        updateToolbar(dependency);

        return result;
    }

    private void updateToolbar(View dependency) {
//        DebugLog.e("dependency top=" + dependency.getTop());
        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) dependency.findViewById(R.id.ctl_toolbar);
        Toolbar toolbar = (Toolbar) dependency.findViewById(R.id.toolbar);
        // 经测量，在top=scrimTop时开始撤销遮罩
        if (dependency.getTop() > scrimTop) {
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
            toolbar.setOverflowIcon(dependency.getContext().getResources().getDrawable(R.drawable.ic_more_vert_white_24dp));
            toolbar.setTitleTextColor(Color.WHITE);
            ctl.setExpandedTitleColor(Color.WHITE);
            ctl.setCollapsedTitleTextColor(Color.WHITE);
        }
        else {
            toolbar.setNavigationIcon(R.drawable.ic_menu_purple_200_24dp);
            toolbar.setOverflowIcon(dependency.getContext().getResources().getDrawable(R.drawable.ic_more_vert_purple_200_24dp));
            toolbar.setTitleTextColor(dependency.getContext().getResources().getColor(R.color.colorPrimary));
            ctl.setExpandedTitleColor(dependency.getContext().getResources().getColor(R.color.colorPrimary));
            ctl.setCollapsedTitleTextColor(dependency.getContext().getResources().getColor(R.color.colorPrimary));
        }
    }

}
