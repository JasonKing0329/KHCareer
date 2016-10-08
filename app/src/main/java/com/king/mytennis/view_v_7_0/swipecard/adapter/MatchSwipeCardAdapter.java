package com.king.mytennis.view_v_7_0.swipecard.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.service.ImageUtil;
import com.king.mytennis.view.R;
import com.king.mytennis.view_v_7_0.controller.ObjectCache;
import com.king.mytennis.view_v_7_0.model.MatchBean;
import com.king.mytennis.view_v_7_0.view.MatchActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author JingYang
 * @version create time：2016-3-11 上午11:32:25
 *
 */
public class MatchSwipeCardAdapter extends AbstractSwipeAdapter {

	private List<MatchBean>  mList;
	private List<MatchBean> mOriginList;
	private Map<String, Integer> indexPosMap;

	private int colorHard;
	private int colorClay;
	private int colorGrass;
	private int colorInnerHard;
	private String[] courtValues;

	private ViewHolder firstItemHolder;

	public MatchSwipeCardAdapter(Context context, List<MatchBean> list) {
		super(context);
		mContext = context;
		if (list == null) {
			mOriginList = new ArrayList<MatchBean>();
			mList = new ArrayList<MatchBean>();
		}
		else {
			mOriginList = list;
			mList = new ArrayList<MatchBean>();
			mList.addAll(mOriginList);
		}
		indexPosMap = new HashMap<String, Integer>();

		courtValues = mContext.getResources().getStringArray(R.array.spinner_court);
		colorHard = mContext.getResources().getColor(R.color.swipecard_text_hard);
		colorClay = mContext.getResources().getColor(R.color.swipecard_text_clay);
		colorGrass = mContext.getResources().getColor(R.color.swipecard_text_grass);
		colorInnerHard = mContext.getResources().getColor(R.color.swipecard_text_innerhard);
	}
	@Override
	public int getCount() {

		return mList.size();
	}

	@Override
	/**
	 * 为了方便onItemClickListener知道position，这里返回position
	 */
	public Object getItem(int position) {

		return position;
	}

	@Override
	public Object getItemData(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.d("MatchSwipeCardAdapter", "getView " + position);
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.swipecard_item_match, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.swipecard_match_img);
			holder.name = (TextView) convertView.findViewById(R.id.swipecard_match_name);
			holder.place = (TextView) convertView.findViewById(R.id.swipecard_match_city);
			holder.level = (TextView) convertView.findViewById(R.id.swipecard_match_level);
			holder.court = (TextView) convertView.findViewById(R.id.swipecard_match_court);
			holder.total = (TextView) convertView.findViewById(R.id.swipecard_match_total);
			holder.best = (TextView) convertView.findViewById(R.id.swipecard_match_best);
			holder.convertView = convertView;
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (position == 0) {
			firstItemHolder = holder;
		}

		fillHolder(holder, position);
		return convertView;
	}

	private void fillHolder(ViewHolder holder, int position) {
		MatchBean bean = mList.get(position);
		ImageUtil.load("file://" + ImageFactory.getMatchHeadPath(bean.getName(), bean.getCourt())
				, holder.image, R.drawable.swipecard_default_img);
		holder.name.setText(bean.getName());
		holder.place.setText(bean.getCountry() + "/" + bean.getCity());
		holder.level.setText(bean.getLevel());
		holder.court.setText(bean.getCourt());
		holder.total.setText("总胜负  " + bean.getWin() + "胜" + bean.getLose() + "负");
		String best = "最佳战绩  " + bean.getBest();
		if (bean.getBestYears().length() > 0) {
			best = best + "(" + bean.getBestYears() + ")";
		}
		holder.best.setText(best);

		int color = getCardIndexColor(position);
		holder.name.setTextColor(color);
		holder.court.setTextColor(color);
	}

	private class ViewHolder {
		View convertView;
		ImageView image;
		TextView name, place, level, court, total, best;
	}

	@Override
	public void remove(int index) {
		if (index > -1 && index < mList.size()) {
			mList.remove(index);
			notifyDataSetChanged();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateDatas(Object list) {

		mOriginList.clear();
		mOriginList.addAll((List<MatchBean>)list);
		mList.clear();
		mList.addAll(mOriginList);

		indexSideBar.clear();
		indexPosMap.clear();
		TreeSet<String> set = new TreeSet<String>();
		String index = null;
		String name = null;
		for (int i = 0; i < mList.size(); i ++) {
			name = mList.get(i).getNamePinYin();
			index = "" + name.charAt(0);
			if (set.add(index)) {
				indexSideBar.addIndex(index);
				indexPosMap.put(index, i);
			}
		}

		notifyDataSetChanged();
	}

	@Override
	public int getCardIndexColor(int position) {
		int color = colorHard;
		if (position < mList.size()) {
			if (mList.get(position).getCourt().equals(courtValues[1])) {
				color = colorClay;
			}
			else if (mList.get(position).getCourt().equals(courtValues[2])) {
				color = colorGrass;
			}
			else if (mList.get(position).getCourt().equals(courtValues[3])) {
				color = colorInnerHard;
			}
		}
		return color;
	}

	@Override
	public void refreshFirstItem(Animation animation) {

		if (firstItemHolder != null) {
			fillHolder(firstItemHolder, 0);
			if (animation != null) {
				firstItemHolder.convertView.startAnimation(animation);
			}
		}
	}
	@Override
	public void addItem(int i, Object bean) {
		if (i <= mList.size()) {
			mList.add(i, (MatchBean) bean);
		}
	}
	@Override
	public void onItemClicked(int itemPosition) {
		ObjectCache.putMatchBean(mList.get(itemPosition));
		Intent intent = new Intent().setClass(mContext, MatchActivity.class);
		((Activity) mContext).startActivity(intent);
	}
	@Override
	public void onIndexLetter(String index) {
		int position = indexPosMap.get(index);
		mList.clear();
		for (int i = position; i < mOriginList.size(); i ++) {
			mList.add(mOriginList.get(i));
		}
		notifyDataSetChanged();

		refreshFirstItem(null);
	}

}
