package com.king.mytennis.match;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;

import com.king.mytennis.model.Constants;
import com.king.mytennis.view.R;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.util.List;

/**
 * 描述: 处理match item滑动引起的变化事件
 * <p/>作者：景阳
 * <p/>创建时间: 2017/3/21 9:05
 */
public class ScrollManager implements DiscreteScrollView.ScrollStateChangeListener {

    private GradientBkView vMatchBk;
    private FloatingActionButton fab;
    private List<UserMatchBean> matchList;
    private int[] colorHard;
    private int[] colorClay;
    private int[] colorGrass;
    private int[] colorInhard;
    private int[] startColor;
    private int[] targetColor;
    private ArgbEvaluator evaluator;
    private int startPosition;

    public ScrollManager(Context context) {
        colorHard = context.getResources().getIntArray(R.array.gradientCourtHard);
        colorClay = context.getResources().getIntArray(R.array.gradientCourtClay);
        colorGrass = context.getResources().getIntArray(R.array.gradientCourtGrass);
        colorInhard = context.getResources().getIntArray(R.array.gradientCourtInHard);
        evaluator = new ArgbEvaluator();
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

    /**
     * 设置match list数据，根据item的court进行颜色变化
     *
     * @param matchList
     */
    public void bindData(List<UserMatchBean> matchList) {
        this.matchList = matchList;
    }

    private int[] getColor(int position) {
        int[] color;
        String court = matchList.get(position).getNameBean().getMatchBean().getCourt();
        if (court.equals(Constants.RECORD_MATCH_COURTS[1])) {
            color = colorClay;
        } else if (court.equals(Constants.RECORD_MATCH_COURTS[2])) {
            color = colorGrass;
        } else if (court.equals(Constants.RECORD_MATCH_COURTS[3])) {
            color = colorInhard;
        } else {
            color = colorHard;
        }
        return color;
    }

    /**
     * 根据因子计算当前的变化值
     *
     * @param fraction   0到1之间
     * @param startValue
     * @param endValue
     * @return
     */
    private int[] mix(float fraction, int[] startValue, int[] endValue) {
        return new int[]{
                (Integer) evaluator.evaluate(fraction, startValue[0], endValue[0]),
                (Integer) evaluator.evaluate(fraction, startValue[1], endValue[1]),
                (Integer) evaluator.evaluate(fraction, startValue[2], endValue[2])
        };
    }

    /**
     * 根据position初始化状态
     *
     * @param position
     */
    public void initPosition(int position) {
        startColor = getColor(position);
        updateBehaviors(startColor);
    }

    /**
     * 更新绑定的view颜色变化
     *
     * @param color
     */
    private void updateBehaviors(int[] color) {

        // 渐变背景
        vMatchBk.updateGradientValues(color);

        fab.setRippleColor(color[2]);
        // 背景色需要调用这个方法，setBackgroundColor不管用
        fab.setBackgroundTintList(ColorStateList.valueOf(color[0]));
    }

    @Override
    public void onScrollStart(RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {
        startPosition = adapterPosition;
        startColor = getColor(adapterPosition);
    }

    @Override
    public void onScrollEnd(RecyclerView.ViewHolder currentItemHolder, int adapterPosition) {

    }

    /**
     * DiscreteScrollView的滑动回调
     *
     * @param scrollPosition -1到1之间，负数表示向左滑动，正数表示向右滑动
     */
    @Override
    public void onScroll(float scrollPosition) {
        int nextPosition = startPosition + (scrollPosition > 0 ? -1 : 1);
        if (nextPosition >= 0 && nextPosition < matchList.size()) {
            targetColor = getColor(nextPosition);
            // 根据滑动因子计算当前的变化因素
            int[] color = mix(Math.abs(scrollPosition), startColor, targetColor);
            // 更新绑定的view的状态
            updateBehaviors(color);
        }
    }
}
