package com.king.khcareer.match.timeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.multiuser.MultiUser;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.common.image.ImageUtil;
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
public class MatchExpanAdapter extends BaseExpandableListAdapter {

	private List<List<Record>> childList;
	private Context mContext;
	private int titleColor;

	/**
	 * 保存首次从文件夹加载的图片序号
	 */
	private Map<String, Integer> imageIndexMap;
	private String userId;

	public MatchExpanAdapter(Context context, List<List<Record>> list) {
		mContext = context;
		childList = list;
		imageIndexMap = new HashMap<>();
		titleColor = mContext.getResources().getColor(R.color.actionbar_bk_blue);
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
		String date = childList.get(groupPosition).get(0).getStrDate();
		view.setText(date.split("-")[0]);
		view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
		view.setTextColor(titleColor);
		if (groupPosition == 0) {
			view.setPadding(60, 0, 0, 40);
		}
		else {
			view.setPadding(60, 40, 0, 40);
		}
		convertView = view;
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {
		RecordViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_match_expan_child, null);
			holder = new RecordViewHolder();
			holder.head = (ImageView) convertView.findViewById(R.id.match_item_head);
			holder.player = (TextView) convertView.findViewById(R.id.match_item_player);
			holder.line1 = (TextView) convertView.findViewById(R.id.match_item_line1);
			holder.score = (TextView) convertView.findViewById(R.id.match_item_score);
			convertView.setTag(holder);
		}
		else {
			holder = (RecordViewHolder) convertView.getTag();
		}
		Record record = childList.get(groupPosition).get(childPosition);
		holder.player.setText(record.getCompetitor());
		holder.line1.setText("(" + record.getCptSeed() + "/" + record.getCptRank() + ")"
				+ "  " + record.getRound());
		String winner = record.getWinner();
		if (winner.equals(MultiUserManager.USER_DB_FLAG)) {
			MultiUser user;
			if (userId == null) {
				user = MultiUserManager.getInstance().getCurrentUser();
			}
			else {
				user = MultiUserManager.getInstance().getUser(userId);
			}
			winner = user.getDisplayName();
		}
		holder.score.setText(winner + "  " + record.getScore());

		String filePath;
		if (imageIndexMap.get(record.getCompetitor()) == null) {
			filePath = ImageFactory.getPlayerHeadPath(record.getCompetitor(), imageIndexMap);
		}
		else {
			filePath = ImageFactory.getPlayerHeadPath(record.getCompetitor(), imageIndexMap.get(record.getCompetitor()));
		}
		ImageUtil.load("file://" + filePath, holder.head, R.drawable.icon_list);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;//一定要在这里控制，否则单击、长按都没有效果
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	private class RecordViewHolder {
		ImageView head;
		TextView player, line1, score;
	}
}
