package com.king.mytennis.view;

import java.util.List;

import com.king.mytennis.model.AtpRank;
import com.king.mytennis.model.AtpSinaRank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RankListAdapter extends BaseAdapter {

	private List<AtpSinaRank> list;
	private Context mContext;
	private int COLOR_TYPE1;
	private int COLOR_TYPE2;
	private int COLOR_TYPE3;
	private int COLOR_TEXT_TYPE1;
	private int COLOR_TEXT_TYPE2;
	private OnClickListener onClickListener;
	
	public RankListAdapter(Context context, List<AtpSinaRank> list) {
		mContext = context;
		this.list = list;
		COLOR_TYPE1 = context.getResources().getColor(R.color.white);
		COLOR_TYPE2 = context.getResources().getColor(R.color.rank_color_type2);
		COLOR_TYPE3 = context.getResources().getColor(R.color.rank_color_type3);
		COLOR_TEXT_TYPE1 = context.getResources().getColor(R.color.white);
		COLOR_TEXT_TYPE2 = context.getResources().getColor(R.color.rank_color_text_type2);
	}
	
	public void updateRankList(List<AtpSinaRank> list) {
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

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.rank_list_item, null);
			holder = new ViewHolder();
			holder.layout = (LinearLayout) convertView.findViewById(R.id.ranklist_layout);
			holder.rank = (TextView) convertView.findViewById(R.id.ranklist_rank);
			holder.change = (TextView) convertView.findViewById(R.id.ranklist_change);
			holder.player = (TextView) convertView.findViewById(R.id.ranklist_player);
			holder.country = (TextView) convertView.findViewById(R.id.ranklist_country);
			holder.score = (TextView) convertView.findViewById(R.id.ranklist_score);
			holder.matchnum = (TextView) convertView.findViewById(R.id.ranklist_matchnumber); 
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		AtpSinaRank atpSinaRank = list.get(position);
		holder.rank.setText("" + atpSinaRank.getRank());
		holder.change.setText("" + atpSinaRank.getChange());
		holder.player.setText(atpSinaRank.getPlayer());
		holder.country.setText(atpSinaRank.getCountry());
		holder.score.setText(atpSinaRank.getScore());
		holder.matchnum.setText("" + atpSinaRank.getMatchNumber());
		
		if (onClickListener != null && atpSinaRank instanceof AtpRank) {
			AtpRank rank = (AtpRank) atpSinaRank;
			holder.player.setTag(rank.getPlayerLink());
			holder.player.setOnClickListener(onClickListener);
			holder.score.setTag(rank.getScoreLink());
			holder.score.setOnClickListener(onClickListener);
			holder.matchnum.setTag(rank.getMatchNumLink());
			holder.matchnum.setOnClickListener(onClickListener);
		}

		if (atpSinaRank.getRank() % 2 == 1) {
			holder.layout.setBackgroundColor(COLOR_TYPE1);
			holder.rank.setBackgroundColor(COLOR_TYPE1);
			holder.rank.setTextColor(COLOR_TEXT_TYPE2);
			holder.player.setBackgroundColor(COLOR_TYPE1);
			holder.player.setTextColor(COLOR_TEXT_TYPE2);
			holder.score.setBackgroundColor(COLOR_TYPE1);
			holder.score.setTextColor(COLOR_TEXT_TYPE2);
			holder.change.setBackgroundColor(COLOR_TYPE1);
			holder.country.setBackgroundColor(COLOR_TYPE1);
			holder.matchnum.setBackgroundColor(COLOR_TYPE1);
		}
		else {
			holder.layout.setBackgroundColor(COLOR_TYPE1);
			holder.rank.setBackgroundColor(COLOR_TYPE2);
			holder.rank.setTextColor(COLOR_TEXT_TYPE1);
			holder.player.setBackgroundColor(COLOR_TYPE2);
			holder.player.setTextColor(COLOR_TEXT_TYPE1);
			holder.score.setBackgroundColor(COLOR_TYPE2);
			holder.score.setTextColor(COLOR_TEXT_TYPE1);
			holder.change.setBackgroundColor(COLOR_TYPE3);
			holder.country.setBackgroundColor(COLOR_TYPE3);
			holder.matchnum.setBackgroundColor(COLOR_TYPE3);
		}
		
		return convertView;
	}

	private class ViewHolder {
		public TextView rank, change, player, country, score, matchnum;
		public LinearLayout layout;
	}

	public void setOnclickListener(OnClickListener listener) {
		onClickListener = listener;
	}
}
