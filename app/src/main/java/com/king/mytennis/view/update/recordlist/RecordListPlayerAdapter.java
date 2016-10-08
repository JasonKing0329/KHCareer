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

public class RecordListPlayerAdapter extends BaseExpandableListAdapter {

	private List<HashMap<String, String>> titleList;
	private List<List<HashMap<String, String>>> recordList;
	private Context context;
	private int COLOR_COURT_HARD;
	private int COLOR_COURT_CLAY;
	private int COLOR_COURT_GRASS;
	private int PLAYER_TITLE_FLAG0;
	private int PLAYER_TITLE_FLAG1;
	private ImageLoader imageLoader;

	public RecordListPlayerAdapter(Context context, List<HashMap<String, String>> titleList, List<List<HashMap<String, String>>> recordList) {
		this.context = context;
		this.titleList = titleList;
		this.recordList = recordList;
		COLOR_COURT_HARD = context.getResources().getColor(R.color.court_hard);
		COLOR_COURT_CLAY = context.getResources().getColor(R.color.court_clay);
		COLOR_COURT_GRASS = context.getResources().getColor(R.color.court_grass);
		PLAYER_TITLE_FLAG0 = context.getResources().getColor(R.color.groupbyplayer_flag0);
		PLAYER_TITLE_FLAG1 = context.getResources().getColor(R.color.groupbyplayer_flag1);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.recordlist_title_group_by_player, null);
			holder = new TitleViewHolder();
			holder.arrow = (ImageView) convertView.findViewById(R.id.recordlist_title_image);
			holder.player = (TextView) convertView.findViewById(R.id.recordlist_player_title_player);
			holder.head = (ImageView) convertView.findViewById(R.id.recordlist_title_image_player);
			holder.h2h = (TextView) convertView.findViewById(R.id.recordlist_player_title_h2h);
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
		holder.player.setText(map.get("player") + "(" + map.get("country") + ")");

		holder.h2h.setText(map.get("h2h"));

		Bitmap bitmap = imageLoader.loadPlayerHead(map.get("player"));
		if (bitmap == null) {
			holder.head.setImageResource(R.drawable.icon_list);
		}
		else {
			holder.head.setImageBitmap(ImageFactory.getCircleBitmap(bitmap));
		}

		String bkFlag = map.get("bkFlag");
		if (bkFlag != null) {
			if (bkFlag.equals("0")) {
				convertView.setBackgroundColor(PLAYER_TITLE_FLAG0);
			}
			else if (bkFlag.equals("1")) {
				convertView.setBackgroundColor(PLAYER_TITLE_FLAG1);
			}
		}
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {

		RecordViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.recordlist_record_group_by_player, null);
			holder = new RecordViewHolder();
			holder.matchImage = (ImageView) convertView.findViewById(R.id.recordlist_record_head);
			holder.player = (TextView) convertView.findViewById(R.id.recordlist_record_player);
			holder.match = (TextView) convertView.findViewById(R.id.recordlist_record_match);
			holder.score = (TextView) convertView.findViewById(R.id.recordlist_record_score);
			convertView.setTag(holder);
		}
		else {
			holder = (RecordViewHolder) convertView.getTag();
		}
		HashMap<String, String> map = recordList.get(groupPosition).get(childPosition);

		holder.player.setText(map.get("player"));
		holder.match.setText(map.get("match"));
		holder.score.setText(map.get("score"));

		Bitmap bitmap = imageLoader.loadMatchImage(map.get("match_name"), map.get("court_name"));
		if (bitmap == null) {
			holder.matchImage.setImageResource(R.drawable.icon_list);
		}
		else {
			holder.matchImage.setImageBitmap(bitmap);
		}

		if (map.get("player").contains("硬地")) {
			convertView.setBackgroundColor(COLOR_COURT_HARD);
		}
		else if (map.get("player").contains("红土")) {
			convertView.setBackgroundColor(COLOR_COURT_CLAY);
		}
		else if (map.get("player").contains("草地")) {
			convertView.setBackgroundColor(COLOR_COURT_GRASS);
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {

		return true;//一定要在这里控制，否则单击、长按都没有效果
	}

	private class TitleViewHolder {
		ImageView arrow, head;
		TextView player, h2h;
	}
	private class RecordViewHolder {
		ImageView matchImage;
		TextView player, match, score;
	}
}
