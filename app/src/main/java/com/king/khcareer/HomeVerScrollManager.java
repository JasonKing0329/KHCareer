package com.king.khcareer;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.NestedScrollView;

import com.king.mytennis.match.UserMatchBean;
import com.king.mytennis.model.Constants;
import com.king.mytennis.utils.DebugLog;
import com.king.mytennis.view.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/4/4 0004.
 * 主页content部分NestedScrollView滑动监听
 * 当scroll view向上滑动到posStart时，执行背景过渡变化(change)
 * 当scroll view向下滑动到posStart时，执行背景还原变化(restore)
 * 如果scroll view的滑动位置始终小于posStart，禁用match list横向滑动颜色变化事件(enableHorScrollChange false)
 * 大于等于posStart，(enableHorScrollChange true)
 */

public class HomeVerScrollManager implements NestedScrollView.OnScrollChangeListener {
    
    private int posStart = 343;
    private int timesCount;
    private ArgbEvaluator evaluator;

    private int[] colorHard;
    private int[] colorClay;
    private int[] colorGrass;
    private int[] colorInhard;
    private int[] colorWhite;

    /**
     * 已处于有背景的状态
     */
    private boolean isStatusChange;
    
    private VerScrollCallback callback;
    private List<UserMatchBean> matchList;

    public HomeVerScrollManager(Context context, VerScrollCallback callback) {
        this.callback = callback;
        evaluator = new ArgbEvaluator();
        colorHard = context.getResources().getIntArray(R.array.gradientCourtHard);
        colorClay = context.getResources().getIntArray(R.array.gradientCourtClay);
        colorGrass = context.getResources().getIntArray(R.array.gradientCourtGrass);
        colorInhard = context.getResources().getIntArray(R.array.gradientCourtInHard);
        colorWhite = context.getResources().getIntArray(R.array.gradientPureWhite);
    }

    public void bindData(List<UserMatchBean> matchList) {
        this.matchList = matchList;
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        if (scrollY > oldScrollY) {//  上拉
            // 上拉到一定位置时，开始启用背景的过度变化
            if (scrollY > posStart && !isStatusChange) {
                startChangeBk();
            }
        }
        else {// 下拉
            if (scrollY < posStart && isStatusChange) {
                startRestoreBk();
            }
        }
    }

    // change和restore timer 处于永远只有一个实例能在同一时间段被执行，并且最后start的一定处于队列顶端
    // 当取出队列中的timer开始执行时如果与上一个timer是相同的功能则取消执行
    private Queue<Timer> timerQueue = new LinkedList<>();
    private Timer timerChange = new Timer();
    private Timer timerRestore = new Timer();
    private Timer workingTimer;
    private final int MSG_UPDATE_VIEW = 100;
    private final int MSG_TASK_CHANGE_END = 101;
    private final int MSG_TASK_RESTORE_END = 102;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_TASK_CHANGE_END) {
                // 切换结束后再启用横向滑动事件
                callback.enableHorScrollChange(true);
                // 继续执行队列中的动作
                startTimerFromQueue(timerChange);
            }
            else if (msg.what == MSG_TASK_RESTORE_END) {
                // 还原结束后再禁用横向滑动事件
                callback.enableHorScrollChange(false);
                // 继续执行队列中的动作
                startTimerFromQueue(timerRestore);
            }
            else if (msg.what == MSG_UPDATE_VIEW) {
                int[] colors = (int[]) msg.obj;
                callback.onColorChanging(colors);
            }
        }
    };

    /**
     * 执行队列中待执行的任务
     */
    private void startTimerFromQueue(Timer lastTimer) {
        DebugLog.e("lastTimer " + lastTimer);
        workingTimer = null;
        Timer timer = timerQueue.poll();
        // 还有任务待执行
        if (timer != null) {
            DebugLog.e("timer " + timer);
            // 跟上一个任务相同，取消执行，清空队列
            if (timer == lastTimer) {
                timerQueue.poll();
            }
            else {
                startTimer(timer);
            }
        }
    }

    private class ChangeTask extends TimerTask {
        @Override
        public void run() {
            timesCount++;
            onScroll(timesCount / 20f);
            if (timesCount == 20) {
                cancel();
                handler.sendEmptyMessage(MSG_TASK_CHANGE_END);
            }
        }
    }

    private class RestoreTask extends TimerTask {
        @Override
        public void run() {
            timesCount++;
            onScroll(1 - timesCount / 20f);
            if (timesCount == 20) {
                cancel();
                handler.sendEmptyMessage(MSG_TASK_RESTORE_END);
            }
        }
    }

    private void startTimer(Timer timer) {
        // 没有执行的动作，直接执行
        if (workingTimer == null) {
            workingTimer = timer;
            timesCount = 0;
            isStatusChange = timer == timerChange;
            DebugLog.e("schedule isStatusChange " + isStatusChange);
            timer.schedule(getTimerTask(timer), 0, 25);
        }
        // 有正在执行的动作，排队
        else {
            // 当前已有排队的动作，挤掉该动作
            if (timerQueue.size() == 1) {
                timerQueue.poll();
            }
            DebugLog.e("offer " + timer.toString());
            timerQueue.offer(timer);
        }
    }

    private TimerTask getTimerTask(Timer timer) {
        if (timer == timerChange) {
            return new ChangeTask();
        }
        else if (timer == timerRestore) {
            return new RestoreTask();
        }
        else {
            return null;
        }
    }

    /**
     * 无背景到有背景的切换
     */
    private void startChangeBk() {
        DebugLog.e("" + timerChange.toString());
        startTimer(timerChange);
    }

    /**
     * 有背景到无背景的切换
     */
    private void startRestoreBk() {
        DebugLog.e("" + timerRestore.toString());
        startTimer(timerRestore);
    }

    private synchronized void onScroll(float scrollPosition) {
        int[] targetColor = getColor(callback.getCurrentMatchPosition());
        int[] startColor = colorWhite;
        int[] color = mix(Math.abs(scrollPosition), startColor, targetColor);
        // 更新绑定的view的状态
        updateBehaviors(color);
    }

    private int[] mix(float fraction, int[] startValue, int[] endValue) {
        return new int[]{
                (Integer) evaluator.evaluate(fraction, startValue[0], endValue[0]),
                (Integer) evaluator.evaluate(fraction, startValue[1], endValue[1]),
                (Integer) evaluator.evaluate(fraction, startValue[2], endValue[2])
        };
    }
    private synchronized void updateBehaviors(int[] color) {

        Message message = new Message();
        message.obj = color;
        message.what = MSG_UPDATE_VIEW;
        handler.sendMessage(message);
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

    public interface VerScrollCallback {

        /**
         * 启用横向滑动变化事件
         * @param enable
         */
        void enableHorScrollChange(boolean enable);

        /**
         * 获取当前match position
         * @return
         */
        int getCurrentMatchPosition();

        /**
         * 颜色变化中，通知UI线程更新
         * @param color
         */
        void onColorChanging(int[] color);
    }
}
