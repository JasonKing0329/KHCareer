package com.king.khcareer.record.detail;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.king.lib.tool.ui.RippleFactory;
import com.king.lib.tool.ui.RippleParseException;
import com.king.mytennis.glory.GloryController;
import com.king.khcareer.match.GloryMatchDialog;
import com.king.khcareer.model.sql.player.interfc.H2HDAO;
import com.king.khcareer.common.image.ImageFactory;
import com.king.khcareer.model.sql.player.bean.Record;
import com.king.khcareer.common.multiuser.MultiUserManager;
import com.king.khcareer.model.sql.pubdata.PubDataProvider;
import com.king.khcareer.model.sql.pubdata.bean.PlayerBean;
import com.king.khcareer.common.res.ColorRes;
import com.king.khcareer.common.res.JResource;
import com.king.khcareer.base.KApplication;
import com.king.khcareer.common.image.ImageUtil;
import com.king.khcareer.base.CustomDialog;
import com.king.mytennis.view.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ViewFactory implements OnItemClickListener {

	private final String TAG = "ViewFactory";
	private Context context;
	private Record record;
	private H2HDAO h2hdao;
	private OnClickListener buttonListener;
	public static final int VIEW_MATCH = 0;
	public static final int VIEW_H2H = 1;
	public static final int VIEW_PLAYER = 2;

	private H2HDetailAdapter h2hDetailAdapter;
	private List<Record> h2hList;

	private MatchHolder mMatchHolder;
	private H2HHolder mH2hHolder;
	private int rippleColor;
	private int leftColor, left1Color, left2Color;
	private int rightColor, right1Color, right2Color;
	private int h2hColor;

	private PubDataProvider pubDataProvider;

	public ViewFactory(Context context, Record record, H2HDAO h2hdao) {
		this.context = context;
		this.record = record;
		this.h2hdao = h2hdao;

		pubDataProvider = new PubDataProvider();

		rippleColor = context.getResources().getColor(R.color.ripple_material_light);

		h2hList = h2hdao.getH2HList();
		if (h2hList != null) {
			Collections.reverse(h2hList);//按日期降序
			h2hDetailAdapter = new H2HDetailAdapter(context, h2hList);
		}
	}

	public int getViewNumber() {
		return 2;
	}

	public Object getViewId(int position) {
		return position;
	}

	public View getView(int position, View convertView) {
		Log.i(TAG, "getView " + position);
		if (position == VIEW_MATCH) {
			convertView = showMatchInfor(convertView);
		}
		else if (position == VIEW_H2H) {
			convertView = showH2HInfor(convertView);
		}
		else if (position == VIEW_PLAYER) {

		}
		return convertView;
	}

	private View showH2HInfor(View view) {
		/**
		 * 也许是因为FlingGallery的设计缺陷，连续两次调用getView(1)且converView=null
		 * 用传统的判断convertView==null/getTag方法最后获取的holder并不是显示在视图里的holder
		 * 因此这里为避免这个bug改用判断holder的方法
		 */
		if (mH2hHolder == null) {
			view = LayoutInflater.from(context).inflate(R.layout.detail_h2h, null);
			mH2hHolder = new H2HHolder();
			mH2hHolder.bkView = view.findViewById(R.id.detail_h2h_bk);
			mH2hHolder.rightButton = (Button) view.findViewById(R.id.detail_button_right);
			mH2hHolder.rightButton.setOnClickListener(buttonListener);
			mH2hHolder.player = (TextView) view.findViewById(R.id.detail_h2h_player);
			mH2hHolder.playerH2H = (TextView) view.findViewById(R.id.detail_h2h_h2h);
			mH2hHolder.player1Name = (TextView) view.findViewById(R.id.detail_h2h_player1);
			mH2hHolder.player1RS = (TextView) view.findViewById(R.id.detail_h2h_player1rs);
			mH2hHolder.player2Name = (TextView) view.findViewById(R.id.detail_h2h_player2);
			mH2hHolder.player2RS = (TextView) view.findViewById(R.id.detail_h2h_player2rs);
			mH2hHolder.detail = (ListView) view.findViewById(R.id.detail_h2h_detail);
			mH2hHolder.image = (ImageView) view.findViewById(R.id.detail_h2h_player_image);
			mH2hHolder.playerNameEng = (TextView) view.findViewById(R.id.detail_h2h_player_name_eng);
			mH2hHolder.playerBirthday = (TextView) view.findViewById(R.id.detail_h2h_player_birthday);
			view.setTag(mH2hHolder);
		}
//		else {
//			mH2hHolder = (H2HHolder) view.getTag();
//		}

		mH2hHolder.player.setText(record.getCompetitor() + "(" + record.getCptCountry() + ")");
		mH2hHolder.player1Name.setText(MultiUserManager.getInstance().getCurrentUser().getDisplayName());
		mH2hHolder.player2Name.setText(record.getCompetitor());
		mH2hHolder.player1RS.setText("rank(" + record.getRank() + ")  seed(" + record.getSeed() +")");
		mH2hHolder.player2RS.setText("rank(" + record.getCptRank() + ")  seed(" + record.getCptSeed() +")");
		mH2hHolder.playerH2H.setText(h2hdao.getWin() + " - " + h2hdao.getLose());
		PlayerBean bean = pubDataProvider.getPlayerByChnName(record.getCompetitor());
		if (bean != null) {
			if (!TextUtils.isEmpty(bean.getNameEng())) {
				mH2hHolder.playerNameEng.setText(bean.getNameEng());
			}
			if (!TextUtils.isEmpty(bean.getBirthday())) {
				mH2hHolder.playerBirthday.setText(bean.getBirthday());
			}
		}

		mH2hHolder.detail.setAdapter(h2hDetailAdapter);
		mH2hHolder.detail.setOnItemClickListener(this);

		ImageUtil.load("file://" + ImageFactory.getDetailPlayerPath(record.getCompetitor()), mH2hHolder.image);

		return view;
	}

	private View showMatchInfor(View view) {

		/**
		 * 当FlingGallery设置了循环后，右滑到1后再右滑，还是getView(1)，这时会出现空界面（对应上一个注释里的连续两次getView(1)）
		 * 左滑到0后再左滑也是这样的
		 */
		if (mMatchHolder == null) {
			view = LayoutInflater.from(context).inflate(R.layout.detail_match, null);
			mMatchHolder = new MatchHolder();
			mMatchHolder.bkView = view.findViewById(R.id.detail_match_bk);
			mMatchHolder.leftButton = (Button) view.findViewById(R.id.detail_button_left);
			mMatchHolder.rightButton = (Button) view.findViewById(R.id.detail_button_right);
			mMatchHolder.leftButton1 = (Button) view.findViewById(R.id.detail_button_left1);
			mMatchHolder.rightButton1 = (Button) view.findViewById(R.id.detail_button_right1);
			mMatchHolder.leftButton2 = (Button) view.findViewById(R.id.detail_button_left2);
			mMatchHolder.rightButton2 = (Button) view.findViewById(R.id.detail_button_right2);
			mMatchHolder.leftButton.setOnClickListener(buttonListener);
			mMatchHolder.rightButton.setOnClickListener(buttonListener);
			mMatchHolder.leftButton1.setOnClickListener(buttonListener);
			mMatchHolder.rightButton1.setOnClickListener(buttonListener);
			mMatchHolder.leftButton2.setOnClickListener(buttonListener);
			mMatchHolder.rightButton2.setOnClickListener(buttonListener);

			mMatchHolder.court = (TextView) view.findViewById(R.id.detail_match_court);
			mMatchHolder.name = (TextView) view.findViewById(R.id.detail_match_name);
			mMatchHolder.date = (TextView) view.findViewById(R.id.detail_match_date);
			mMatchHolder.level = (TextView) view.findViewById(R.id.detail_match_level);
			mMatchHolder.place = (TextView) view.findViewById(R.id.detail_match_place);
			mMatchHolder.round = (TextView) view.findViewById(R.id.detail_match_round);
			mMatchHolder.score = (TextView) view.findViewById(R.id.detail_match_score);
			mMatchHolder.image = (ImageView) view.findViewById(R.id.detail_match_image);

			view.setTag(mMatchHolder);
		}
		mMatchHolder.court.setText(record.getCourt());
		mMatchHolder.name.setText(record.getMatch());
		mMatchHolder.date.setText(record.getStrDate());
		mMatchHolder.level.setText(record.getLevel());
		mMatchHolder.place.setText(record.getRegion()+"/"+record.getMatchCountry()+"/"+record.getCity());

		String winner = record.getWinner();
		if (winner.equals(MultiUserManager.USER_DB_FLAG)) {
			winner = MultiUserManager.getInstance().getCurrentUser().getDisplayName();
		}
		mMatchHolder.score.setText(winner + " " + record.getScore());
		mMatchHolder.round.setText(record.getRound());

		ImageUtil.load("file://" + ImageFactory.getMatchHeadPath(record.getMatch(), record.getCourt()), mMatchHolder.image);

		return view;
	}

//	private class PlayerViewHolder {
//		public ImageView image;
//		public TextView pleayer1Name, pleayer1RankSeed;
//		public TextView pleayer2Name, pleayer2RankSeed;
//		public TextView winner, score;
//	}

	private class MatchHolder {
		public View bkView;
		public TextView name;
		public ImageView image;
		public TextView date, level, court, place, round;
		public TextView score;
		public Button leftButton, leftButton1, leftButton2;
		public Button rightButton, rightButton1, rightButton2;
	}

	private class H2HHolder {
		public View bkView;
		public TextView player;
		public ImageView image;
		public TextView player1Name, player1RS;
		public TextView player2Name, player2RS;
		public TextView playerH2H;
		public TextView playerNameEng;
		public TextView playerBirthday;
		public ListView detail;
		public Button rightButton;
	}

	public void setOnclickListener(OnClickListener listener) {
		buttonListener = listener;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
		final Record record = h2hList.get(position);
		new GloryMatchDialog(context, new CustomDialog.OnCustomDialogActionListener() {

			@Override
			public boolean onSave(Object object) {
				return false;
			}

			@Override
			public void onLoadData(HashMap<String, Object> data) {
				List<Record> list = new GloryController().loadMatchRecord(record.getMatch(), record.getStrDate());
				data.put(CustomDialog.OnCustomDialogActionListener.DATA_TYPE, list);
			}

			@Override
			public boolean onCancel() {
				return false;
			}
		}).show();
	}

	public void onColorChanged(String key, int newColor) {
		if (((DetailGallery) context).getCurrentPosition() == VIEW_MATCH) {
			if (key.equals(ColorRes.DETAIL_VIEWPAGE_MATCH)) {
				mMatchHolder.bkView.setBackgroundColor(newColor);
			}
			else if (key.equals(ColorRes.DETAIL_VIEWPAGE_MATCH_TITLE)) {
				mMatchHolder.name.setTextColor(newColor);
			}
			else if (key.equals(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN1)) {
				rightColor = newColor;
				updateRippleColor(mMatchHolder.rightButton, newColor);
			}
			else if (key.equals(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN2)) {
				leftColor = newColor;
				updateRippleColor(mMatchHolder.leftButton, newColor);
			}
			else if (key.equals(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN3)) {
				right1Color = newColor;
				updateRippleColor(mMatchHolder.rightButton1, newColor);
			}
			else if (key.equals(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN4)) {
				left1Color = newColor;
				updateRippleColor(mMatchHolder.leftButton1, newColor);
			}
			else if (key.equals(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN5)) {
				right2Color = newColor;
				updateRippleColor(mMatchHolder.rightButton2, newColor);
			}
			else if (key.equals(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN6)) {
				left2Color = newColor;
				updateRippleColor(mMatchHolder.leftButton2, newColor);
			}
			else if (key.equals(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN1_TEXT)) {
				mMatchHolder.date.setTextColor(newColor);
			}
			else if (key.equals(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN2_TEXT)) {
				mMatchHolder.level.setTextColor(newColor);
			}
			else if (key.equals(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN3_TEXT)) {
				mMatchHolder.court.setTextColor(newColor);
			}
			else if (key.equals(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN4_TEXT)) {
				mMatchHolder.place.setTextColor(newColor);
			}
			else if (key.equals(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN5_TEXT)) {
				mMatchHolder.round.setTextColor(newColor);
			}
			else if (key.equals(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN6_TEXT)) {
				mMatchHolder.score.setTextColor(newColor);
			}
		}
		else {
			if (key.equals(ColorRes.DETAIL_VIEWPAGE_H2H)) {
				mH2hHolder.bkView.setBackgroundColor(newColor);
			}
			else if (key.equals(ColorRes.DETAIL_VIEWPAGE_H2H_TITLE)) {
				mH2hHolder.player.setTextColor(newColor);
			}
			else if (key.equals(ColorRes.DETAIL_VIEWPAGE_H2H_BTN)) {
				h2hColor = newColor;
				updateRippleColor(mH2hHolder.rightButton, newColor);
			}
			else {
				h2hDetailAdapter.onColorChanged(key, newColor);
			}
		}
	}

	public void onApplyDefaultColors() {
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_MATCH);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_MATCH_TITLE);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN1);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN2);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN3);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN4);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN5);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN6);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN1_TEXT);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN2_TEXT);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN3_TEXT);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN4_TEXT);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN5_TEXT);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_MATCH_BTN6_TEXT);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_H2H);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_H2H_TITLE);
		JResource.removeColor(ColorRes.DETAIL_VIEWPAGE_H2H_BTN);
		h2hDetailAdapter.onApplyDefaultColors();
	}

	public void resetColor() {

		// 由于FlingGallery没有提供page变化回调方法，因此为避免麻烦，两处都reset
//		if (((DetailGallery) context).getCurrentPosition() == VIEW_MATCH) {
		mMatchHolder.bkView.setBackgroundColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH, R.color.detail_viewpage_match));
		mMatchHolder.name.setTextColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_TITLE, R.color.detail_viewpage_match_title));
		rightColor = JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN1, R.color.detail_viewpage_match_btn1);
		leftColor = JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN2, R.color.detail_viewpage_match_btn2);
		right1Color = JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN3, R.color.detail_viewpage_match_btn3);
		left1Color = JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN4, R.color.detail_viewpage_match_btn4);
		right2Color = JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN5, R.color.detail_viewpage_match_btn5);
		left2Color = JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN6, R.color.detail_viewpage_match_btn6);
		setMatchPageRippleColor();
		mMatchHolder.date.setTextColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN1_TEXT, R.color.detail_viewpage_match_btn1_text));
		mMatchHolder.level.setTextColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN2_TEXT, R.color.detail_viewpage_match_btn2_text));
		mMatchHolder.court.setTextColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN3_TEXT, R.color.detail_viewpage_match_btn3_text));
		mMatchHolder.place.setTextColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN4_TEXT, R.color.detail_viewpage_match_btn4_text));
		mMatchHolder.round.setTextColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN5_TEXT, R.color.detail_viewpage_match_btn5_text));
		mMatchHolder.score.setTextColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_MATCH_BTN6_TEXT, R.color.detail_viewpage_match_btn6_text));
//		}
//		else {
		mH2hHolder.bkView.setBackgroundColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_H2H, R.color.detail_viewpage_h2h));
		mH2hHolder.player.setTextColor(JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_H2H_TITLE, R.color.detail_viewpage_h2h_title));
		h2hColor = JResource.getColor(context
				, ColorRes.DETAIL_VIEWPAGE_H2H_BTN, R.color.detail_viewpage_h2h_btn);
		setH2hPageRippleColor();
		h2hDetailAdapter.resetColor();
//		}
	}

	private void setH2hPageRippleColor() {
		if (KApplication.isLollipop()) {
			Drawable drawable = RippleFactory.getRippleBackground(h2hColor, rippleColor);
			mH2hHolder.rightButton.setBackground(drawable);
		}
		else {
			mH2hHolder.rightButton.setBackgroundColor(h2hColor);
		}
	}
	private void setMatchPageRippleColor() {
		if (KApplication.isLollipop()) {
			Drawable drawable = RippleFactory.getRippleBackground(rightColor, rippleColor);
			mMatchHolder.rightButton.setBackground(drawable);
			drawable = RippleFactory.getRippleBackground(right1Color, rippleColor);
			mMatchHolder.rightButton1.setBackground(drawable);
			drawable = RippleFactory.getRippleBackground(right2Color, rippleColor);
			mMatchHolder.rightButton2.setBackground(drawable);
			drawable = RippleFactory.getRippleBackground(leftColor, rippleColor);
			mMatchHolder.leftButton.setBackground(drawable);
			drawable = RippleFactory.getRippleBackground(left1Color, rippleColor);
			mMatchHolder.leftButton1.setBackground(drawable);
			drawable = RippleFactory.getRippleBackground(left2Color, rippleColor);
			mMatchHolder.leftButton2.setBackground(drawable);
		}
		else {
			mMatchHolder.rightButton.setBackgroundColor(rightColor);
			mMatchHolder.leftButton.setBackgroundColor(leftColor);
			mMatchHolder.rightButton1.setBackgroundColor(right1Color);
			mMatchHolder.leftButton1.setBackgroundColor(left1Color);
			mMatchHolder.rightButton2.setBackgroundColor(right2Color);
			mMatchHolder.leftButton2.setBackgroundColor(left2Color);
		}
	}

	private void updateRippleColor(TextView view, int color) {
		if (KApplication.isLollipop()) {
			try {
				RippleFactory.setRippleColor(view, color, rippleColor);
			} catch (RippleParseException e) {
				view.setBackgroundColor(color);
				e.printStackTrace();
			}
		}
		else {
			view.setBackgroundColor(color);
		}
	}

}
