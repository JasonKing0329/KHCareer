package com.king.mytennis.view_v_7_0.swipecard.controller;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.king.mytennis.model.Record;
import com.king.mytennis.service.MenuService;
import com.king.mytennis.service.ScreenUtils;
import com.king.mytennis.view_v_7_0.model.MatchBean;
import com.king.mytennis.view_v_7_0.model.PlayerBean;
import com.king.mytennis.view_v_7_0.swipecard.SwipeCardActivity;

/**
 * @author JingYang
 * @version create time：2016-3-11 上午11:25:47
 *
 */
public class SwipeCardController {

	private List<Record> recordList;
	private Context mContext;

	private int mCardMode;

	private Stack<MatchBean> matchStack;
	private Stack<PlayerBean> playerStack;

	public SwipeCardController(Context context, int cardMode) {
		mContext = context;
		mCardMode = cardMode;
		matchStack = new Stack<MatchBean>();
		playerStack = new Stack<PlayerBean>();
	}

	public void loadRecordList() {
		recordList = new MenuService().loadRecords(mContext);
		//从最近往前排
		Collections.reverse(recordList);
	}

	public List<Record> getRecordList() {
		return recordList;
	}

	public void onCardRemoved(Object bean) {
		if (bean instanceof MatchBean) {
			matchStack.push((MatchBean)bean);
		}
		else if (bean instanceof PlayerBean) {
			playerStack.push((PlayerBean)bean);
		}
	}

	public Object getLastCard() {
		if (mCardMode == SwipeCardActivity.INIT_MATCH) {
			if (matchStack.isEmpty()) {
				return null;
			}
			return matchStack.pop();
		}
		else if (mCardMode == SwipeCardActivity.INIT_PLAYER) {
			if (playerStack.isEmpty()) {
				return null;
			}
			return playerStack.pop();
		}
		return null;
	}

	public boolean isStackEmpty() {
		if (mCardMode == SwipeCardActivity.INIT_MATCH) {
			return matchStack.isEmpty();
		}
		else if (mCardMode == SwipeCardActivity.INIT_PLAYER) {
			return playerStack.isEmpty();
		}
		return true;
	}
	public void clearStack() {
		if (mCardMode == SwipeCardActivity.INIT_MATCH) {
			matchStack.clear();
		}
		else if (mCardMode == SwipeCardActivity.INIT_PLAYER) {
			playerStack.clear();
		}
	}

	/**
	 * 获取恢复上张卡片的动画效果，从左右任意Y轴方向位置飞入
	 * @param context
	 * @return
	 */
	public Animation getRestoreCardAnim(Context context) {

		Random random = new Random();
		float fromX = 1;
		if (random.nextInt() < 0) {
			fromX = -1;
		}
		float fromY = 0;
		int height = ScreenUtils.getScreenHeight(context);
		fromY = random.nextInt() % height;

		TranslateAnimation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, fromX
				, Animation.RELATIVE_TO_SELF, 0
				, Animation.ABSOLUTE, fromY
				, Animation.RELATIVE_TO_SELF, 0);
		animation.setDuration(500);
		return animation;
	}

}
