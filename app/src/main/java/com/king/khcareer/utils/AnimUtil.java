package com.king.khcareer.utils;

import android.animation.Animator;
import android.content.Context;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/8/10 15:32
 */
public class AnimUtil {

    /**
     * must be called after anchor being attached
     * (e.g. call this in onCreate, should called in runnable that anchor post)
     * @param context
     * @param anchor
     * @param animListener
     */
    public static void startFullCircleRevealAnimation(Context context, View anchor, Animator.AnimatorListener animListener) {

        int cx = ScreenUtils.getScreenWidth(context) / 2;
        int cy = ScreenUtils.getScreenHeight(context) / 2;

        Animator anim =
                ViewAnimationUtils.createCircularReveal(anchor, cx, cy, 50, ScreenUtils.getScreenWidth(context));

        anim.setDuration(1000);
        anim.setInterpolator(new AccelerateInterpolator(2));
        if (animListener != null) {
            anim.addListener(animListener);
        }

        anim.start();
    }

}
