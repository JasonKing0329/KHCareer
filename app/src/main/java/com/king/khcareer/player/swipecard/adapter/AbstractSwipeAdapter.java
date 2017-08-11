package com.king.khcareer.player.swipecard.adapter;

import com.king.khcareer.pubview.SideBar;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.widget.BaseAdapter;

/**
 * @author JingYang
 * @version create time：2016-3-11 下午4:48:07
 *
 */
public abstract class AbstractSwipeAdapter extends BaseAdapter {

	protected Context mContext;
	protected SideBar indexSideBar;

	public AbstractSwipeAdapter(Context context) {
		mContext = context;
	}

	public void setIndexSideBar(SideBar sideBar) {
		indexSideBar = sideBar;
	}

	public abstract void updateDatas(Object list);
	public abstract void remove(int index);
	public abstract int getCardIndexColor(int position);

	/**
	 * swipecard的一个bug，在卡片没有全部消除的情况下，重新适配内容并通过notifyDataSetChanged刷新
	 * position=0的位置没有触发getView，所以第0个item需要强制刷新一下
	 * @param animation 可以为null
	 */
	public abstract void refreshFirstItem(Animation animation);

	public abstract void addItem(int i, Object bean);

	public abstract void onItemClicked(View v, int itemPosition);

	/**
	 * adapter的getItem用于返回position，因此重新定义一个返回item对象的方法
	 * @param i
	 * @return
	 */
	public abstract Object getItemData(int position);

	/**
	 * index bar选择字母
	 * @param index
	 */
	public abstract void onIndexLetter(String index);
}
