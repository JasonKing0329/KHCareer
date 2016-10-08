package com.king.mytennis.update_v_6_0;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.king.mytennis.glory.GloryController;
import com.king.mytennis.glory.GloryMatchDialog;
import com.king.mytennis.interfc.H2HDAO;
import com.king.mytennis.model.H2HDAODB;
import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.model.Record;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.service.Application;
import com.king.mytennis.service.ScreenUtils;
import com.king.mytennis.update_v_6_0.adapter.AbstractDataCountAdapter;
import com.king.mytennis.update_v_6_0.adapter.PlayerDataAdapter;
import com.king.mytennis.update_v_6_0.controller.CardManager;
import com.king.mytennis.view.CustomDialog;
import com.king.mytennis.view.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PlayerCardView extends CardView implements OnItemClickListener
		, OnClickListener{

	private TextView titleView, countView;
	private ImageView headView;
	private ListView listView;
	private PlayerCardAdapter indexAdapter, playerAdapter;
	private List<HashMap<String, String>> indexData, playerData;
	private String cardIndex;
	
	private int adapterMode;
	private CardManager cardManager;
	
	public PlayerCardView(Context context) {
		super(context);
		View view = LayoutInflater.from(context).inflate(R.layout.cardview_player_index, null);
		titleView = (TextView) view.findViewById(R.id.cardview_player_title);
		countView = (TextView) view.findViewById(R.id.cardview_player_datacount);
		listView = (ListView) view.findViewById(R.id.cardview_player_list);
		headView = (ImageView) view.findViewById(R.id.cardview_player_head);
		listView.setOnItemClickListener(this);
		countView.setOnClickListener(this);
		if (Application.isLollipop()) {
			countView.setBackgroundResource(R.drawable.ripple_white);
		}
		addView(view);
	}

	@Override
	public void initData() {

	}
	
	public void setIndex(String index) {
		cardIndex = index.toUpperCase(Locale.US);
		titleView.setText(cardIndex);
	}

	public void setCardManager(CardManager manager) {
		cardManager = manager;
	}

	@Override
	public void setCardData(List<HashMap<String, String>> playerData) {
		this.indexData = playerData;;
		adapterMode = PlayerCardAdapter.CARD_PLAYER_INDEX;
		indexAdapter = new PlayerCardAdapter(getContext(), playerData, adapterMode);
		listView.setAdapter(indexAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view
			, int position, long arg3) {
		if (adapterMode == PlayerCardAdapter.CARD_PLAYER_INDEX) {

			HashMap<String, String> map = indexData.get(position);
			loadPlayerData(map.get("player"));
			titleView.setText(map.get("player") + "   " + map.get("h2h"));
			headView.setVisibility(View.VISIBLE);
			countView.setVisibility(View.GONE);
			Bitmap bitmap = null;
			try {
				bitmap = new ImageFactory().getPlayerHead(map.get("player"), 200);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (bitmap == null) {
				headView.setImageResource(R.drawable.icon_list);
			}
			else {
				headView.setImageBitmap(ImageFactory.getCircleBitmap(bitmap));
			}

			adapterMode = PlayerCardAdapter.CARD_PLAYER;
			if (playerAdapter == null) {
				playerAdapter = new PlayerCardAdapter(getContext(), playerData, adapterMode);
			}
			listView.setAdapter(playerAdapter);
		}
		else {
			final int  pos = position;
			GloryMatchDialog dialog = new GloryMatchDialog(getContext(), new CustomDialog.OnCustomDialogActionListener() {
				
				@Override
				public boolean onSave(Object object) {
					return false;
				}
				
				@Override
				public void onLoadData(HashMap<String, Object> data) {
					HashMap<String, String> map = playerData.get(pos);
					List<Record> list = new GloryController().loadMatchRecord(getContext(), 
							map.get("match"), map.get("date"));
					data.put(CustomDialog.OnCustomDialogActionListener.DATA_TYPE, list);
				}
				
				@Override
				public boolean onCancel() {
					return false;
				}
			});
			dialog.enableItemLongClick();
			dialog.show();
		}
	}

	private void loadPlayerData(String player) {
		H2HDAO dao = new H2HDAODB(getContext(), player);
		List<Record> list = dao.getH2HList();
		Collections.reverse(list);
		if (playerData == null) {
			playerData = new ArrayList<HashMap<String,String>>();
		}
		else {
			playerData.clear();
		}
		HashMap<String, String> map = null;
		for (Record record:list) {
			map = new HashMap<String, String>();
			map.put("date", record.getStrDate());//keyword, will be used when search match
			map.put("match", record.getMatch());//keyword, will be used when search match
			map.put("line1", record.getMatch() + "   " + record.getRound());
			
			String winner = record.getWinner();
			if (record.getWinner().equals(MultiUserManager.USER_DB_FLAG)) {
				winner = MultiUserManager.getInstance().getCurrentUser().getDisplayName();
			}
			map.put("line2", winner + "  " + record.getScore());
			playerData.add(map);
		}
	}

	public boolean backToIndexView() {

		if (adapterMode == PlayerCardAdapter.CARD_PLAYER) {
			adapterMode = PlayerCardAdapter.CARD_PLAYER_INDEX;
			listView.setAdapter(indexAdapter);
			titleView.setText(cardIndex);
			headView.setVisibility(View.GONE);
			countView.setVisibility(View.VISIBLE);
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View view) {
		if (view == countView) {
			AbstractDataCountAdapter adapter = new PlayerDataAdapter(getContext(), cardManager);
			DataCountDialog dialog = new DataCountDialog(getContext()
					, "" + Calendar.getInstance().get(Calendar.YEAR), adapter);
			int width = ScreenUtils.getScreenWidth(getContext());
			int height = getResources().getDimensionPixelSize(R.dimen.dialog_data_count_year_height);
			dialog.setDialogSize(width, height);
			dialog.show();
		}
	}

	@Override
	public void resetColor() {
		// TODO Auto-generated method stub
		
	}
}
