package com.king.khcareer.match;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.bumptech.glide.Glide;
import com.king.khcareer.base.KApplication;
import com.king.khcareer.common.config.Constants;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.common.image.glide.GlideOptions;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.base.CustomDialog;
import com.king.khcareer.player.page.PlayerPageActivity;
import com.king.mytennis.view.R;

public class GloryMatchDialog extends CustomDialog implements OnItemLongClickListener {

	public static final String USER_ID = "user_id";

	private ListView listView;
	private ImageView imageView;
	private TextView placeView;
	private TextView levelCourtView;
	private TextView dateView;
	private TextView achieveView;
	private TextView rankView;
	private TextView seedView;
	private List<Record> recordList;

	private String[] roundArray;

	public GloryMatchDialog(Context context,
							OnCustomDialogActionListener actionListener) {
		super(context, actionListener);

		roundArray = Constants.RECORD_MATCH_ROUNDS;
		initData();
	}

	private void initData() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		actionListener.onLoadData(map);
		recordList = (List<Record>) map.get(OnCustomDialogActionListener.DATA_TYPE);
		Record record = recordList.get(recordList.size() - 1);
		Collections.reverse(recordList);

		setTitle(record.getMatch());
		dateView.setText(record.getStrDate());

		Glide.with(KApplication.getInstance())
				.load(ImageFactory.getMatchHeadPath(record.getMatch(), record.getCourt()))
				.apply(GlideOptions.getDefaultMatchOptions())
				.into(imageView);

		placeView.setText(record.getMatchCountry() + "/" + record.getCity());
		levelCourtView.setText(record.getLevel() + "/" + record.getCourt());
		rankView.setText("排名： " + record.getRank());
		if (record.getSeed() != 0) {
			seedView.setText("种子： " + record.getSeed());
		}
		if (record.getRound().equals(roundArray[0])) {
			if (MultiUserManager.USER_DB_FLAG.equals(record.getWinner())) {
				achieveView.setText(getContext().getResources().getString(R.string.glory_match_achieve)
						+ getContext().getResources().getString(R.string.glory_match_winner));
			}
			else {
				achieveView.setText(getContext().getResources().getString(R.string.glory_match_achieve)
						+ getContext().getResources().getString(R.string.glory_match_ru));
			}
		}
		else {
			achieveView.setText(getContext().getResources().getString(R.string.glory_match_achieve)
					+ record.getRound());
		}

		String userId = (String) map.get(USER_ID);
		GloryMatchAdapter adapter = new GloryMatchAdapter(context, recordList, userId);
		listView.setAdapter(adapter);
	}

	/**
	 * 从DetailGallery里初始化不需要再支持长按功能
	 */
	public void enableItemLongClick() {
		listView.setOnItemLongClickListener(this);
	}

	@Override
	protected View getCustomView() {
		View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_glory_match, null);
		imageView = (ImageView) view.findViewById(R.id.glory_match_image);
		listView = (ListView) view.findViewById(R.id.glory_match_list);
		placeView = (TextView) view.findViewById(R.id.glory_match_place);
		rankView = (TextView) view.findViewById(R.id.glory_match_rank);
		seedView = (TextView) view.findViewById(R.id.glory_match_seed);
		levelCourtView = (TextView) view.findViewById(R.id.glory_match_levelcourt);
		achieveView = (TextView) view.findViewById(R.id.glory_match_achieve);
		dateView = (TextView) view.findViewById(R.id.glory_match_date);
		return view;
	}

	@Override
	protected View getCustomToolbar() {
		hideSaveButton();
		hideTitleIcon();
		return null;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
								   int position, long id) {

		Intent intent = new Intent();
		intent.setClass(getContext(), PlayerPageActivity.class);
		intent.putExtra(PlayerPageActivity.KEY_COMPETITOR_NAME, recordList.get(position).getCompetitor());
		getContext().startActivity(intent);
		dismiss();
		return false;
	}

}
