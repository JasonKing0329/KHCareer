package com.king.khcareer.match;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.king.khcareer.base.KApplication;
import com.king.khcareer.common.config.Constants;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.image.glide.GlideOptions;
import com.king.khcareer.common.multiuser.MultiUser;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.common.multiuser.MultiUserManager;
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
	private MultiUser user;

	private RequestOptions playerOptions;

	/**
	 * 保存首次从文件夹加载的图片序号
	 */
	private Map<String, Integer> imageIndexMap;

	public GloryMatchAdapter(Context context, List<Record> list, String userId) {

		this.context = context;
		this.list = list;
		imageIndexMap = new HashMap<>();
		roundArray = Constants.RECORD_MATCH_ROUNDS;
		roundReferArray = Constants.RECORD_MATCH_ROUNDS_SHORT;

		playerOptions = GlideOptions.getDefaultPlayerOptions();

		if (userId == null) {
			user = MultiUserManager.getInstance().getCurrentUser();
		}
		else {
			user = MultiUserManager.getInstance().getUser(userId);
		}
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
			holder.head = (ImageView) convertView.findViewById(R.id.iv_player);
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

		Glide.with(KApplication.getInstance())
				.load(filePath)
				.apply(playerOptions)
				.into(holder.head);

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
			winner = user.getDisplayName();
		}
		holder.score.setText(winner + " " + record.getScore());
		
		return convertView;
	}

	private class RecordViewHolder {
		ImageView head;
		TextView round, player, line1, score;
	}
}
