package com.king.mytennis.view;

import com.king.mytennis.interfc.H2HDAO;
import com.king.mytennis.model.ImageFactory;
import com.king.mytennis.model.Record;
import com.king.mytennis.multiuser.MultiUserManager;
import com.king.mytennis.service.ImageUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsDialog {

	private Context userActivity;
	private Record record;
	private H2HDAO h2hdao;
	
	private View view;
	
	private ImageView iv_player, iv_court;
	private TextView tv_match;
	private TextView tv_player, tv_competitor, tv_competitor_country, tv_winner;
	private TextView tv_date, tv_level, tv_court, tv_place, tv_round;
	private TextView tv_h2h, tv_h2h_all;
	
	public DetailsDialog(Context activity, Record record, H2HDAO dao) {
		
		this.userActivity = activity;
		this.record = record;
		this.h2hdao = dao;
		ImageUtil.initImageLoader(activity);
		init();
	}

	private void init(){
		
		view=LayoutInflater.from(userActivity).inflate(R.layout.dialog_recorddetails, null);

		iv_player=(ImageView)view.findViewById(R.id.details_image_player);
		tv_match=(TextView)view.findViewById(R.id.details_match);
		tv_player=(TextView)view.findViewById(R.id.details_player);
		tv_competitor=(TextView)view.findViewById(R.id.details_competitor);
		tv_competitor_country=(TextView)view.findViewById(R.id.details_competitor_country);
		tv_winner=(TextView)view.findViewById(R.id.details_winnerandscore);
		iv_court=(ImageView)view.findViewById(R.id.details_image_court);
		tv_date=(TextView)view.findViewById(R.id.details_date);
		tv_level=(TextView)view.findViewById(R.id.details_level);
		tv_court=(TextView)view.findViewById(R.id.details_court);
		tv_place=(TextView)view.findViewById(R.id.details_placeandmatch);
		tv_round=(TextView)view.findViewById(R.id.details_round);
		tv_h2h=(TextView)view.findViewById(R.id.details_h2h);
		tv_h2h_all=(TextView)view.findViewById(R.id.details_h2h_all);

		ImageUtil.load("file://" + ImageFactory.getDetailPlayerPath(record.getCompetitor()), iv_player);
		tv_match.setText(record.getMatch());
		tv_player.setText(MultiUserManager.getInstance().getCurrentUser().getDisplayName()
				+ "("+record.getRank()+"/"+record.getSeed()+")");
		tv_competitor.setText(record.getCompetitor()+"("+record.getCptRank()+"/"+record.getCptSeed()+")");
		tv_competitor_country.setText(record.getCptCountry());

		String winner = record.getWinner();
		if (winner.equals(MultiUserManager.USER_DB_FLAG)) {
			winner = MultiUserManager.getInstance().getCurrentUser().getDisplayName();
		}
		tv_winner.setText(winner +"    "+record.getScore());

		ImageUtil.load("file://" + ImageFactory.getMatchHeadPath(record.getMatch(), record.getCourt()), iv_court);
		
		tv_date.setText(record.getStrDate());
		tv_level.setText(record.getLevel());
		tv_court.setText(record.getCourt());
		tv_round.setText(record.getRound());
		tv_place.setText(record.getRegion()+"/"+record.getMatchCountry()+"/"+record.getCity());
		tv_h2h.setText(MultiUserManager.getInstance().getCurrentUser().getDisplayName()
				+ "   "+h2hdao.getWin()+" - "+h2hdao.getLose()+"   "+record.getCompetitor());
		tv_h2h_all.setText(h2hdao.getH2HDetail());
		
	}
	
	public void show() {
		new AlertDialog.Builder(userActivity)
		.setTitle(R.string.details_title)
		.setView(view)
		.show();
	}
}
