package com.king.mytennis.update_v_6_0;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;

import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.view.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerCardAdapter extends BaseAdapter {

	public static int CARD_PLAYER = 1;
	public static int CARD_PLAYER_INDEX = 2;
	private int cardType;
	
	private Context mContext;
	private List<HashMap<String, String>> list;
	private ImageFactory imageFactory;
	
	public PlayerCardAdapter(Context context, List<HashMap<String, String>> list
			, int cardType) {
		mContext = context;
		this.list = list;
		this.cardType = cardType;
		imageFactory = new ImageFactory();
	}
	
	public int getCardType() {
		return cardType;
	}

	public void updateList(List<HashMap<String, String>> list) {
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
	public View getView(int position, View convertView, ViewGroup group) {
		if (cardType == CARD_PLAYER_INDEX) {
			PlayerIndexHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.cardview_player_index_listitem, null);
				holder = new PlayerIndexHolder();
				holder.head = (ImageView) convertView.findViewById(R.id.cardview_player_index_head);
				holder.player = (TextView) convertView.findViewById(R.id.cardview_player_index_name);
				holder.h2h = (TextView) convertView.findViewById(R.id.cardview_player_index_h2h);
				convertView.setTag(holder);
			}
			else {
				holder = (PlayerIndexHolder) convertView.getTag();
			}
			
			HashMap<String, String> map = list.get(position);
			String player = map.get("player");
			holder.player.setText(player + "(" + map.get("country") + ")");
			holder.h2h.setText(map.get("h2h"));
			
			Bitmap bitmap = null;
			try {
				bitmap = imageFactory.getPlayerHead(player, 200);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			if (bitmap == null) {
				holder.head.setImageResource(R.drawable.icon_list);
			}
			else {
				holder.head.setImageBitmap(ImageFactory.getCircleBitmap(bitmap));
			}
		}
		else {
			PlayerHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.cardview_player_listitem, null);
				holder = new PlayerHolder();
				holder.date = (TextView) convertView.findViewById(R.id.cardview_player_list_item_date);
				holder.line1 = (TextView) convertView.findViewById(R.id.cardview_player_list_item_line1);
				holder.line2 = (TextView) convertView.findViewById(R.id.cardview_player_list_item_line2);
				convertView.setTag(holder);
			}
			else {
				holder = (PlayerHolder) convertView.getTag();
			}
			
			HashMap<String, String> map = list.get(position);
			holder.date.setText(map.get("date"));
			holder.line1.setText(map.get("line1"));
			holder.line2.setText(map.get("line2"));
		}
		return convertView;
	}

	private class PlayerIndexHolder {
		ImageView head;
		TextView player, h2h;
	}
	private class PlayerHolder {
		TextView date;
		TextView line1, line2;
	}
}
