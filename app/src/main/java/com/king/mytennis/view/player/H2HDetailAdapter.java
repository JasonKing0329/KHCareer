package com.king.mytennis.view.player;

import java.util.List;

import com.king.khcareer.model.sql.player.bean.Record;
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
	
	public H2HDetailAdapter(Context context, List<Record> list) {

		this.context = context;
		this.list = list;
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

		return list == null ? position:list.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		RecordViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.h2h_table_item, null);
			holder = new RecordViewHolder();
			holder.date = (TextView) convertView.findViewById(R.id.h2h_detail_date);
			holder.court = (TextView) convertView.findViewById(R.id.h2h_detail_court);
			holder.round = (TextView) convertView.findViewById(R.id.h2h_detail_round);
			holder.match = (TextView) convertView.findViewById(R.id.h2h_detail_match);
			holder.winner = (TextView) convertView.findViewById(R.id.h2h_detail_winner);
			holder.score = (TextView) convertView.findViewById(R.id.h2h_detail_score);
			convertView.setTag(holder);
		}
		else {
			holder = (RecordViewHolder) convertView.getTag();
		}

		Record record = list.get(position);

		holder.date.setText(record.getStrDate());
		holder.court.setText(record.getCourt());
		//holder.rank.setText("(" + record.getCptRank() + "/" + record.getCptSeed() + ")"); 
		holder.match.setText(record.getMatch());
		holder.round.setText(record.getRound());
		holder.winner.setText(record.getCompetitor());
		holder.score.setText(record.getScore());
		return convertView;
	}

	private class RecordViewHolder {
		TextView date, court, round, match, winner, score;
	}
}
