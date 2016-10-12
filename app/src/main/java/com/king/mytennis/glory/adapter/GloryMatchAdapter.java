package com.king.mytennis.glory.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.model.Record;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.service.ImageUtil;
import com.king.mytennis.view.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GloryMatchAdapter extends BaseAdapter {

	private Context context;
	private List<Record> list;
	private String[] roundArray, roundReferArray;

	/**
	 * 保存首次从文件夹加载的图片序号
	 */
	private Map<String, Integer> imageIndexMap;

	public GloryMatchAdapter(Context context, List<Record> list) {

		this.context = context;
		this.list = list;
		imageIndexMap = new HashMap<>();
		roundArray = context.getResources().getStringArray(R.array.spinner_round);
		roundReferArray = context.getResources().getStringArray(R.array.spinner_round_show_in_glorymatch);
	}
	@Override
	public int getCount() {

		return list.size();
	}

	@Override
	public Object getItem(int position) {

		return list.get(position);
	}

	@Override
	public long getItemId(int position) {

		return list.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		RecordViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.glory_match_list_item, null);
			holder = new RecordViewHolder();
			holder.head = (ImageView) convertView.findViewById(R.id.glory_match_list_item_head);
			holder.round = (TextView) convertView.findViewById(R.id.glory_match_list_item_round);
			holder.player = (TextView) convertView.findViewById(R.id.glory_match_list_item_player);
			holder.line1 = (TextView) convertView.findViewById(R.id.glory_match_list_item_line1);
			holder.score = (TextView) convertView.findViewById(R.id.glory_match_list_item_score);
			convertView.setTag(holder);
		}
		else {
			holder = (RecordViewHolder) convertView.getTag();
		}

		Record record = list.get(position);

		String filePath;
		if (imageIndexMap.get(record.getCompetitor()) == null) {
			filePath = ImageFactory.getPlayerHeadPath(record.getCompetitor(), imageIndexMap);
		}
		else {
			filePath = ImageFactory.getPlayerHeadPath(record.getCompetitor(), imageIndexMap.get(record.getCompetitor()));
		}
		ImageUtil.load("file://" + filePath, holder.head, R.drawable.icon_list);

		holder.player.setText(record.getCompetitor());
		for (int i = 0; i < roundArray.length; i ++) {
			if (record.getRound().equals(roundArray[i])) {
				holder.round.setText(roundReferArray[i]);
				break;
			}
		}
		holder.line1.setText("(" + record.getCptRank() + "/" + record.getCptSeed() + ")  "
				+ record.getCptCountry());
		
		String winner = record.getWinner();
		if (winner.equals(MultiUserManager.USER_DB_FLAG)) {
			winner = MultiUserManager.getInstance().getCurrentUser().getDisplayName();
		}
		holder.score.setText(winner + " " + record.getScore());
		
		return convertView;
	}

	private class RecordViewHolder {
		ImageView head;
		TextView round, player, line1, score;
	}
}
