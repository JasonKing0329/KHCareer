package com.king.mytennis.view_v_7_0.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.king.mytennis.model.Constants;
import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.model.Record;
import com.king.mytennis.multiuser.MultiUser;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.service.ImageUtil;
import com.king.mytennis.view.R;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author JingYang
 * @version create time：2016-3-14 下午4:00:15
 *
 */
public class PlayerExpanAdapter extends BaseExpandableListAdapter {

	private List<String> titleList;
	private List<List<Record>> childList;
	private Context mContext;
	private int titleColor;
	private MultiUser pageUser;

	/**
	 * 保存首次从文件夹加载的图片序号
	 */
	private Map<String, Integer> imageIndexMap;

	public PlayerExpanAdapter(Context context, List<List<Record>> list, MultiUser pageUser) {
		mContext = context;
		this.pageUser = pageUser;
		childList = list;
		titleColor = mContext.getResources().getColor(R.color.actionbar_bk_orange);
		imageIndexMap = new HashMap<>();
		createTitleList();
	}

	private void createTitleList() {
		if (childList != null) {
			titleList = new ArrayList<String>();
			for (List<Record> list:childList) {
				int win = 0;
				int lose = 0;
				StringBuffer buffer = new StringBuffer(list.get(0).getStrDate().split("-")[0]);
				buffer.append("（");
				for (Record record:list) {
					//如果是赛前退赛不算作h2h
					if (record.getScore().equals(Constants.SCORE_RETIRE)) {
						continue;
					}
					if (record.getWinner().equals(MultiUserManager.USER_DB_FLAG)) {
						win ++;
					}
					else {
						lose ++;
					}
				}
				buffer.append(win).append("胜").append(lose).append("负）");
				titleList.add(buffer.toString());
			}
		}
	}

	@Override
	public int getGroupCount() {

		return childList == null ? 0:childList.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return childList.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {

		return childList.get(groupPosition).get(childPosition);
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
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent) {
		TextView view = new TextView(mContext);
		view.setText(titleList.get(groupPosition));
		view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		view.setTextColor(titleColor);
		if (groupPosition == 0) {
			view.setPadding(60, 0, 0, 20);
		}
		else {
			view.setPadding(60, 20, 0, 20);
		}
		convertView = view;
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {
		RecordViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_player_expan_child, parent, false);
			holder = new RecordViewHolder();
			holder.head = (ImageView) convertView.findViewById(R.id.player_item_head);
			holder.player = (TextView) convertView.findViewById(R.id.player_item_player);
			holder.match = (TextView) convertView.findViewById(R.id.player_item_match);
			holder.score = (TextView) convertView.findViewById(R.id.player_item_score);
			convertView.setTag(holder);
		}
		else {
			holder = (RecordViewHolder) convertView.getTag();
		}
		Record record = childList.get(groupPosition).get(childPosition);
		holder.player.setText("(" + record.getCptSeed() + "/" + record.getCptRank() + ")  "
				+ record.getCourt() + "  " + record.getStrDate());
		holder.match.setText(record.getLevel() + "  " + record.getMatch()
				+ "  " + record.getRound());
		String winner = record.getWinner();
		if (winner.equals(MultiUserManager.USER_DB_FLAG)) {
			winner = pageUser.getDisplayName();
		}
		holder.score.setText(winner + "  " + record.getScore());

		String filePath;
		if (imageIndexMap.get(record.getCompetitor()) == null) {
			filePath = ImageFactory.getMatchHeadPath(record.getMatch(), record.getCourt(), imageIndexMap);
		}
		else {
			filePath = ImageFactory.getMatchHeadPath(record.getMatch(), record.getCourt(), imageIndexMap.get(record.getCompetitor()));
		}
		ImageUtil.load("file://" + filePath, holder.head, R.drawable.icon_list);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;//一定要在这里控制，否则单击、长按都没有效果
	}

	private class RecordViewHolder {
		ImageView head;
		TextView player, match, score;
	}
}
