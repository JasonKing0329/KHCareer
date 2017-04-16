package com.king.khcareer.match.card;

import java.util.HashMap;
import java.util.List;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.common.res.ColorRes;
import com.king.khcareer.common.res.JResource;
import com.king.mytennis.view.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MatchCardAdapter extends BaseAdapter {

	private Context mContext;
	private List<HashMap<String, String>> list;
	
	private int mDateColor, mWinnerColor, mRunnerupColor, mSfColor, mNormalColor;
	private String[] roundArray;
	
	public MatchCardAdapter(Context context, List<HashMap<String, String>> list) {
		mContext = context;
		this.list = list;
		resetColor();
		roundArray = Constants.RECORD_MATCH_ROUNDS;
	}
	
	@Override
	public int getCount() {
		return list == null ? 0:list.size();
	}

	@Override
	public Object getItem(int position) {
		return list == null ? null:list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.cardview_match_listitem, null);
			holder = new ViewHolder();
			holder.date = (TextView) convertView.findViewById(R.id.cardview_match_list_item_date);
			holder.line1 = (TextView) convertView.findViewById(R.id.cardview_match_list_item_line1);
			holder.line2 = (TextView) convertView.findViewById(R.id.cardview_match_list_item_line2);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		HashMap<String, String> map = list.get(position);
		holder.date.setText(map.get("date"));
		holder.date.setTextColor(mDateColor);
		
		String line1 = map.get("line1");
		String line2 = map.get("line2");
		holder.line1.setText(line1);
		holder.line2.setText(line2);
		if (line1.startsWith(roundArray[0])) {//Final
			if (line2.contains("Win")) {
				holder.line1.setTextColor(mWinnerColor);
				holder.line2.setTextColor(mWinnerColor);
			}
			else {
				holder.line1.setTextColor(mRunnerupColor);
				holder.line2.setTextColor(mRunnerupColor);
			}
		}
		else if (line1.startsWith(roundArray[1])) {//Semi Final
			holder.line1.setTextColor(mSfColor);
			holder.line2.setTextColor(mSfColor);
		}
		else {
			holder.line1.setTextColor(mNormalColor);
			holder.line2.setTextColor(mNormalColor);
		}
		return convertView;
	}

	private class ViewHolder {
		TextView date, line1, line2;
	}

	public void resetColor() {
		mDateColor = JResource.getColor(mContext
				, ColorRes.CARDVIEW_MATCH_LIST_DATE, R.color.cardview_match_list_date);
		mNormalColor = JResource.getColor(mContext
				, ColorRes.CARDVIEW_MATCH_LIST_NORMAL, R.color.cardview_match_list_normal);
		mWinnerColor = JResource.getColor(mContext
				, ColorRes.CARDVIEW_MATCH_LIST_WINNER, R.color.cardview_match_list_winner);
		mRunnerupColor = JResource.getColor(mContext
				, ColorRes.CARDVIEW_MATCH_LIST_RUNNERUP, R.color.cardview_match_list_runnerup);
		mSfColor = JResource.getColor(mContext
				, ColorRes.CARDVIEW_MATCH_LIST_SF, R.color.cardview_match_list_sf);
	}

	public void updateDateColor(int mDateColor) {
		this.mDateColor = mDateColor;
		notifyDataSetChanged();
	}

	public void updateWinnerColor(int mWinnerColor) {
		this.mWinnerColor = mWinnerColor;
		notifyDataSetChanged();
	}

	public void updateRunnerupColor(int mRunnerupColor) {
		this.mRunnerupColor = mRunnerupColor;
		notifyDataSetChanged();
	}

	public void updateSfColor(int mSfColor) {
		this.mSfColor = mSfColor;
		notifyDataSetChanged();
	}

	public void updateNormalColor(int mNormalColor) {
		this.mNormalColor = mNormalColor;
		notifyDataSetChanged();
	}
}
