package com.king.mytennis.glory.adapter;

import java.util.List;

import com.king.mytennis.model.Record;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.view.R;
import com.king.mytennis.view.update.recordlist.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GloryMatchAdapter extends BaseAdapter {

	private Context context;
	private List<Record> list;
	private ImageLoader imageLoader;
	private String[] roundArray, roundReferArray;
	
	public GloryMatchAdapter(Context context, List<Record> list, ImageLoader imageLoader) {

		this.context = context;
		this.list = list;
		this.imageLoader = imageLoader;
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
		Bitmap bitmap = imageLoader.loadPlayerHead(record.getCompetitor());
		if (bitmap == null) {
			holder.head.setImageResource(R.drawable.icon_list);
		}
		else {
			holder.head.setImageBitmap(bitmap);
		}
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
