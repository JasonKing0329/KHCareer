package com.king.mytennis.glory.adapter;

import java.util.HashMap;
import java.util.List;

import com.king.mytennis.view.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AchieveDateContentAdapter extends BaseAdapter {

	private Context context;
	private List<HashMap<String, String>> achieveList;
	//private SimpleDateFormat format;

	public AchieveDateContentAdapter(Context context, List<HashMap<String, String>> list) {
		this.context = context;
		achieveList = list;
	}

	public void setAchieveList(List<HashMap<String, String>> list) {
		achieveList = list;
	}
	
	@Override
	public int getCount() {
		int count = 0;
		if (achieveList != null) {
			count = achieveList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {

		if (achieveList != null) {
			return achieveList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder h = null;
		if (convertView == null) {
			h = new Holder();
			convertView = LayoutInflater.from(context).inflate(R.layout.glory_xlistview_item_date_content,
					null);
			h.index = (TextView) convertView.findViewById(R.id.tv_index);
			h.date = (TextView) convertView.findViewById(R.id.tv_time);
			h.iv = (ImageView) convertView.findViewById(R.id.iv_icon);
			h.achive = (TextView) convertView.findViewById(R.id.tv_content);
			h.level = (TextView) convertView.findViewById(R.id.tv_level);
			h.court = (TextView) convertView.findViewById(R.id.tv_court);
			h.winner = (TextView) convertView.findViewById(R.id.tv_champion);
			convertView.setTag(h);
		} else {
			h = (Holder) convertView.getTag();
		}

//		AchieveRecord record = achieveList.get(position);

//		Calendar calendar = Calendar.getInstance();
//		calendar.setTimeInMillis(record.getDate());
//		h.date.setText(calendar.get(Calendar.YEAR) + "\n      "
//				+ (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.DAY_OF_MONTH));
//		//h.iv.setBackgroundDrawable(null);
//		if (record.getRecord() != null) {
//			h.achive.setText(record.getRecord().getPlace() + "  " + record.getAchieve() + "  " + record.getPoint());
//		}
//		else {
//			h.achive.setText(record.getAchieve() + "  " + record.getPoint());
//		}
		HashMap<String, String> record = achieveList.get(position);
		h.index.setText(record.get("achieve_index"));
		h.date.setText(record.get("achieve_date"));
		h.achive.setText(record.get("achieve_glory"));
		h.level.setText(record.get("achieve_level"));
		h.court.setText(record.get("achieve_court"));
		if (record.get("achieve_winner") == null) {
			h.winner.setVisibility(View.GONE);
		}
		else {
			h.winner.setVisibility(View.VISIBLE);
			h.winner.setText(record.get("achieve_winner"));
		}
		
		return convertView;
	}

	private class Holder {
		public TextView index;
		public TextView date;
		public ImageView iv;
		public TextView achive;
		public TextView level;
		public TextView court;
		public TextView winner;
	}
}
