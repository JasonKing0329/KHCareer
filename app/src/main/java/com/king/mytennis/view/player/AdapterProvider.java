package com.king.mytennis.view.player;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class AdapterProvider {

	private Context mContext;
	private List<WorldPlayer> playerList;
	private List<String> nameList;
	private ArrayAdapter<String> playerACTAdapter;
	
	public AdapterProvider(Context context) {
		mContext = context;
	}
	
	public ArrayAdapter<String> getPlayerAutoCompleteAdapter(int atpOrWta) {
		if (playerACTAdapter == null) {
			if (playerList == null) {
				playerList = new Controller(mContext).getAllPlayer(atpOrWta);
			}

			if (playerList != null) {
				nameList = new ArrayList<String>();
				for (WorldPlayer player:playerList) {
					nameList.add(player.getEngName());
				}
				playerACTAdapter = new ArrayAdapter<String>(mContext
						, android.R.layout.simple_dropdown_item_1line, nameList);
			}
		}
		return playerACTAdapter;
	}
	
	public void updatePlayerAutoCompleteAdapter(int atpOrWta) {

		if (playerACTAdapter != null) {
			playerList = new Controller(mContext).getAllPlayer(atpOrWta);
			nameList = new ArrayList<String>();
			for (WorldPlayer player:playerList) {
				nameList.add(player.getEngName());
			}
			playerACTAdapter.notifyDataSetChanged();
		}
	}
}
