package com.king.mytennis.view.update.recordlist;

import java.util.HashMap;
import java.util.List;

import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.view.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RecordListDateAdapter extends BaseExpandableListAdapter {

	private List<HashMap<String, String>> titleList;
	private List<List<HashMap<String, String>>> recordList;
	private Context context;
	private int COLOR_TITLE_COURT_HARD;
	private int COLOR_TITLE_COURT_CLAY;
	private int COLOR_TITLE_COURT_GRASS;
	private ImageLoader imageLoader;

	public RecordListDateAdapter(Context context, List<HashMap<String, String>> titleList, List<List<HashMap<String, String>>> recordList) {
		this.context = context;
		this.titleList = titleList;
		this.recordList = recordList;
		COLOR_TITLE_COURT_HARD = context.getResources().getColor(R.color.title_court_hard);
		COLOR_TITLE_COURT_CLAY = context.getResources().getColor(R.color.title_court_clay);
		COLOR_TITLE_COURT_GRASS = context.getResources().getColor(R.color.titlecourt_grass);
	}

	public void setImageLoader(ImageLoader loader) {
		imageLoader = loader;
	}
	@Override
	public int getGroupCount() {

		return titleList == null ? 0:titleList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (recordList != null) {
			if (recordList.get(groupPosition) != null) {
				return recordList.get(groupPosition).size();
			}
		}
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {

		return titleList == null ? null:titleList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {

		if (recordList != null) {
			if (recordList.get(groupPosition) != null) {
				return recordList.get(groupPosition).get(childPosition);
			}
		}
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {

		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {

		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent) {

		TitleViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.recordlist_title_group_by_date, null);
			holder = new TitleViewHolder();
			holder.arrow = (ImageView) convertView.findViewById(R.id.recordlist_title_image);
			holder.level = (TextView) convertView.findViewById(R.id.recordlist_level);
			holder.match = (TextView) convertView.findViewById(R.id.recordlist_match);
			holder.date = (TextView) convertView.findViewById(R.id.recordlist_date);
			convertView.setTag(holder);
		}
		else {
			holder = (TitleViewHolder) convertView.getTag();
		}
		if (isExpanded) {
			holder.arrow.setImageResource(R.drawable.group_expand_up);
		}
		else {
			holder.arrow.setImageResource(R.drawable.group_expand_down);
		}
		HashMap<String, String> map = titleList.get(groupPosition);
		holder.level.setText(map.get("level"));
		holder.match.setText(map.get("match"));
		holder.date.setText(map.get("date"));
		String courtFlag = map.get("courtFlag");
		if (courtFlag != null) {
			if (courtFlag.contains("硬地")) {
				convertView.setBackgroundColor(COLOR_TITLE_COURT_HARD);
			}
			else if (courtFlag.equals("红土")) {
				convertView.setBackgroundColor(COLOR_TITLE_COURT_CLAY);
			}
			else if (courtFlag.equals("草地")) {
				convertView.setBackgroundColor(COLOR_TITLE_COURT_GRASS);
			}
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {

		RecordViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.recordlist_record_group_by_date, null);
			holder = new RecordViewHolder();
			holder.head = (ImageView) convertView.findViewById(R.id.recordlist_record_head);
			holder.player = (TextView) convertView.findViewById(R.id.recordlist_record_player);
			holder.line1 = (TextView) convertView.findViewById(R.id.recordlist_record_line1);
			holder.score = (TextView) convertView.findViewById(R.id.recordlist_record_score);
			convertView.setTag(holder);
		}
		else {
			holder = (RecordViewHolder) convertView.getTag();
		}
		List<HashMap<String, String>> childList = recordList.get(groupPosition);
		HashMap<String, String> map = childList.get(childPosition);

		Bitmap bitmap = imageLoader.loadPlayerHead(map.get("player"));
		if (bitmap == null) {
			holder.head.setImageResource(Integer.parseInt(map.get("head")));
		}
		else {
			holder.head.setImageBitmap(ImageFactory.getCircleBitmap(bitmap));
		}
		holder.player.setText(map.get("player"));
		holder.line1.setText(map.get("line1"));
		holder.score.setText(map.get("score"));
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {

		return true;//一定要在这里控制，否则单击、长按都没有效果
	}

	private class TitleViewHolder {
		ImageView arrow;
		TextView date, level, match;
	}
	private class RecordViewHolder {
		ImageView head;
		TextView player, line1, score;
	}
}
