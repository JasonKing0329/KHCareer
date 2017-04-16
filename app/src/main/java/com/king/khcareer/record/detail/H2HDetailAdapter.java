package com.king.khcareer.record.detail;

import java.util.List;

import com.king.khcareer.common.config.Constants;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.common.res.ColorRes;
import com.king.khcareer.common.res.JResource;
import com.king.mytennis.view.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class H2HDetailAdapter extends BaseAdapter {

	private Context context;
	private List<Record> list;
	private String[] roundArray, roundReferArray;
	
	private int sec1Color, sec2Color, sec3Color;
	private int text1Color, text2Color, text3Color;
	
	public H2HDetailAdapter(Context context, List<Record> list) {

		this.context = context;
		this.list = list;
		roundArray = Constants.RECORD_MATCH_ROUNDS;
		roundReferArray = Constants.RECORD_MATCH_ROUNDS_SHORT;
		resetColor();
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
			convertView = LayoutInflater.from(context).inflate(R.layout.h2h_detail_item, null);
			holder = new RecordViewHolder();
			holder.date = (TextView) convertView.findViewById(R.id.h2h_detail_date);
			holder.rank = (TextView) convertView.findViewById(R.id.h2h_detail_rank);
			holder.court = (TextView) convertView.findViewById(R.id.h2h_detail_court);
			holder.round = (TextView) convertView.findViewById(R.id.h2h_detail_round);
			holder.match = (TextView) convertView.findViewById(R.id.h2h_detail_match);
			holder.score = (TextView) convertView.findViewById(R.id.h2h_detail_score);
			holder.sec1 = convertView.findViewById(R.id.h2h_detail_sec1);
			holder.sec2 = convertView.findViewById(R.id.h2h_detail_sec2);
			holder.sec3 = convertView.findViewById(R.id.h2h_detail_sec3);
			convertView.setTag(holder);
		}
		else {
			holder = (RecordViewHolder) convertView.getTag();
		}

		Record record = list.get(position);

		holder.date.setText(record.getStrDate());
		holder.court.setText(record.getCourt());
		holder.rank.setText("(" + record.getCptRank() + "/" + record.getCptSeed() + ")"); 
		holder.match.setText(record.getMatch());
		for (int i = 0; i < roundArray.length; i ++) {
			if (record.getRound().equals(roundArray[i])) {
				holder.round.setText(roundReferArray[i]);
				break;
			}
		}
		
		String winner = record.getWinner();
		if (winner.equals(MultiUserManager.USER_DB_FLAG)) {
			winner = MultiUserManager.getInstance().getCurrentUser().getDisplayName();
		}
		holder.score.setText(winner + " " + record.getScore());
		
		holder.sec1.setBackgroundColor(sec1Color);
		holder.sec2.setBackgroundColor(sec2Color);
		holder.sec3.setBackgroundColor(sec3Color);
		holder.date.setTextColor(text1Color);
		holder.rank.setTextColor(text1Color);
		holder.court.setTextColor(text2Color);
		holder.round.setTextColor(text2Color);
		holder.match.setTextColor(text3Color);
		holder.score.setTextColor(text3Color);
		return convertView;
	}

	private class RecordViewHolder {
		TextView date, rank, court, round, match, score;
		View sec1, sec2, sec3;
	}

	public void resetColor() {
		sec1Color = JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_SEC1, R.color.detail_viewpage_h2h_item_sec1);
		sec2Color = JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_SEC2, R.color.detail_viewpage_h2h_item_sec2);
		sec3Color = JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_SEC3, R.color.detail_viewpage_h2h_item_sec3);
		text1Color = JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_TEXT1, R.color.detail_viewpage_h2h_item_text1);
		text2Color = JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_TEXT2, R.color.detail_viewpage_h2h_item_text2);
		text3Color = JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_TEXT3, R.color.detail_viewpage_h2h_item_text3);
		notifyDataSetChanged();
	}
	
	public void onApplyDefaultColors() {
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_SEC1);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_SEC2);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_SEC3);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_TEXT1);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_TEXT2);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_TEXT3);
	}
	
	public void onColorChanged(String key, int newColor) {
		if (key.equals(ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_SEC1)) {
			sec1Color = newColor;
		}
		else if (key.equals(ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_SEC2)) {
			sec2Color = newColor;
		}
		else if (key.equals(ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_SEC3)) {
			sec3Color = newColor;
		}
		else if (key.equals(ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_TEXT1)) {
			text1Color = newColor;
		}
		else if (key.equals(ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_TEXT2)) {
			text2Color = newColor;
		}
		else if (key.equals(ColorRes.DETAIL_VIEWPAGE_H2H_ITEM_TEXT3)) {
			text3Color = newColor;
		}
		notifyDataSetChanged();
	}
}
