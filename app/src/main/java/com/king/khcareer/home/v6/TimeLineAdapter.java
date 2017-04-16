package com.king.khcareer.home.v6;

import java.util.HashMap;
import java.util.List;

import com.king.khcareer.common.res.ColorRes;
import com.king.khcareer.common.res.JResource;
import com.king.mytennis.view.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TimeLineAdapter extends BaseAdapter {

	private Context mContext;
	private List<HashMap<String, String>> list;

	private int mMonthColor, mWinnerColor, mNormalColor;

	public TimeLineAdapter(Context context, List<HashMap<String, String>> list) {
		mContext = context;
		this.list = list;
		resetColor();
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.cardview_timeline_listitem, null);
			holder = new ViewHolder();
			holder.date = (TextView) convertView.findViewById(R.id.cardview_tline_list_item_date);
			holder.match = (TextView) convertView.findViewById(R.id.cardview_tline_list_item_match);
			holder.glory = (TextView) convertView.findViewById(R.id.cardview_tline_list_item_glory);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		HashMap<String, String> map = list.get(position);
		holder.date.setText(map.get("date") + "月");
		holder.date.setTextColor(mMonthColor);

		if (map.get("glory").equals("Winner")) {
			holder.match.setTextColor(mWinnerColor);
			holder.glory.setTextColor(mWinnerColor);
		}
		else {
			holder.match.setTextColor(mNormalColor);
			holder.glory.setTextColor(mNormalColor);
		}
		holder.match.setText(map.get("match"));
		holder.glory.setText(map.get("glory"));
		return convertView;
	}

	private class ViewHolder {
		TextView date, match, glory;
	}

	public void updateMonthColor(int newColor) {
		mMonthColor = newColor;
		notifyDataSetChanged();
	}

	public void updateWinnerColor(int newColor) {
		mWinnerColor = newColor;
		notifyDataSetChanged();
	}

	public void updateNormalColor(int newColor) {
		mNormalColor = newColor;
		notifyDataSetChanged();
	}

	public void resetColor() {
		mMonthColor = JResource.getColor(mContext
				, ColorRes.CARDVIEW_YEAR_ITEM_MONTH, R.color.cardview_year_item_month);
		mWinnerColor = JResource.getColor(mContext
				, ColorRes.CARDVIEW_YEAR_ITEM_WINNER, R.color.cardview_year_item_winner);
		mNormalColor = JResource.getColor(mContext
				, ColorRes.CARDVIEW_YEAR_ITEM_NORMAL, R.color.cardview_year_item_normal);
	}
}
