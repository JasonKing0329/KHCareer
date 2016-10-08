package com.king.mytennis.view_v_7_0.swipecard.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.model.Record;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.service.ImageUtil;
import com.king.mytennis.view.R;
import com.king.mytennis.view_v_7_0.controller.ObjectCache;
import com.king.mytennis.view_v_7_0.model.PlayerBean;
import com.king.mytennis.view_v_7_0.view.PlayerActivity;

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
public class PlayerSwipeCardAdapter extends AbstractSwipeAdapter {

	private List<PlayerBean>  mList;
	private List<PlayerBean> mOriginList;
	private Map<String, Integer> indexPosMap;

	private int colorHard;
	private int colorClay;
	private int colorGrass;
	private int colorInnerHard;
	private String[] courtValues;

	private ViewHolder firstItemHolder;

	public PlayerSwipeCardAdapter(Context context, List<PlayerBean> list) {
		super(context);
		mContext = context;
		if (list == null) {
			mList = new ArrayList<PlayerBean>();
			mOriginList = new ArrayList<PlayerBean>();
		}
		else {
			mOriginList = list;
			mList = new ArrayList<PlayerBean>();
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.swipecard_item_player, parent, false);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.swipecard_player_img);
			holder.name = (TextView) convertView.findViewById(R.id.swipecard_player_name);
			holder.country = (TextView) convertView.findViewById(R.id.swipecard_player_country);
			holder.h2h = (TextView) convertView.findViewById(R.id.swipecard_player_h2h);
			holder.lastTitle = (TextView) convertView.findViewById(R.id.swipecard_player_latest);
			holder.lastRecordLine1 = (TextView) convertView.findViewById(R.id.swipecard_player_latest_line1);
			holder.lastRecordLine2 = (TextView) convertView.findViewById(R.id.swipecard_player_latest_line2);
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
		PlayerBean bean = mList.get(position);
		ImageUtil.load("file://" + ImageFactory.getDetailPlayerPath(bean.getName())
				, holder.image, R.drawable.swipecard_default_img);
		holder.name.setText(bean.getName());
		holder.country.setText(bean.getCountry());
		holder.h2h.setText("交手记录  " + bean.getWin() + " - " + bean.getLose());

		Record record = bean.getLastRecord();
		holder.lastTitle.setText(mContext.getString(R.string.swipecard_player_latest)
				+ "(" + record.getStrDate() + ")");
		holder.lastRecordLine1.setText(record.getMatch() + "  " + record.getRound());
		String winner = record.getWinner();
		if (winner.equals(MultiUserManager.USER_DB_FLAG)) {
			winner = MultiUserManager.getInstance().getCurrentUser().getDisplayName();
		}
		holder.lastRecordLine2.setText(winner + "  " + record.getScore());

		int color = getCardIndexColor(position);
		holder.lastRecordLine1.setTextColor(color);
		holder.lastRecordLine2.setTextColor(color);
	}

	private class ViewHolder {
		View convertView;
		ImageView image;
		TextView name, country, h2h, lastTitle, lastRecordLine1, lastRecordLine2;
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
		mOriginList.addAll((List<PlayerBean>)list);
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
			String court = mList.get(position).getLastRecord().getCourt();
			if (court.equals(courtValues[1])) {
				color = colorClay;
			}
			else if (court.equals(courtValues[2])) {
				color = colorGrass;
			}
			else if (court.equals(courtValues[3])) {
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
			mList.add(i, (PlayerBean) bean);
		}
	}
	@Override
	public void onItemClicked(int itemPosition) {
		ObjectCache.putPlayerBean(mList.get(itemPosition));
		Intent intent = new Intent().setClass(mContext, PlayerActivity.class);
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
