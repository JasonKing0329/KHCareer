package com.king.mytennis.view.player;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.king.mytennis.glory.ActionBar;
import com.king.mytennis.glory.ActionBar.ActionBarListener;
import com.king.mytennis.net.html.ATPParser;
import com.king.mytennis.net.html.H2HEntity;
import com.king.mytennis.net.html.Parser;
import com.king.mytennis.net.html.ParserHttpThread;
import com.king.mytennis.net.html.WTAParser;
import com.king.mytennis.view.BaseActivity;
import com.king.mytennis.view.R;
import com.king.mytennis.view.settings.CacheController;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class H2HSearcher extends BaseActivity implements OnClickListener
		, OnLongClickListener, ActionBarListener, OnFocusChangeListener {

	private int atpOrWta;
	private ActionBar actionBar;
	
	private AutoCompleteTextView player1Edit, player2Edit;
	private TextView viewH2hButton;
	private ImageView player1ImageView, player2ImageView;
	private TextView name1View, wins1View, name2View, wins2View;
	private TextView age1View, age2View, bPlace1View, bPlace2View, risidence1View, risidence2View;
	private TextView height1View, weight1View, plays1View, backhand1View, turnedPro1View, ytdWonLost1View, country1View
		, ytdTitles1View, careerWonLost1View, careerTitles1View, careerPrize1View, ytdPrize1View;
	private TextView height2View, weight2View, plays2View, backhand2View, turnedPro2View, ytdWonLost2View, country2View
		, ytdTitles2View, careerWonLost2View, careerTitles2View, careerPrize2View, ytdPrize2View;
	private TableRow risidenceRow, countryRow, ytdPrizeRow, backHandRow;
	private LinearLayout scrollLayout;
	private TableLayout tableLayout;
	private ProgressDialog progressDialog;
	
	private AdapterProvider adapterProvider;
	private WorldPlayer player1, player2;
	private Controller controller;
	private H2HEntity h2hEntity;
	private Parser parser;
	private CacheController cacheController;
	private AnimationDrawable hourglassAnimDrawable1, hourglassAnimDrawable2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.atpOrWta = getIntent().getExtras().getInt("mode");;
		
		setContentView(R.layout.h2hsearcher_main);

		hourglassAnimDrawable1 = (AnimationDrawable) getResources().getDrawable(R.anim.hourglass);
		hourglassAnimDrawable2 = (AnimationDrawable) getResources().getDrawable(R.anim.hourglass);
		
		player1Edit = (AutoCompleteTextView) findViewById(R.id.h2h_edit1);
		player2Edit = (AutoCompleteTextView) findViewById(R.id.h2h_edit2);
		viewH2hButton = (TextView) findViewById(R.id.h2h_submit);
		scrollLayout = (LinearLayout) findViewById(R.id.h2h_scroll_layout);
		tableLayout = (TableLayout) findViewById(R.id.h2hsearcher_tablelayout);

		name1View = (TextView) findViewById(R.id.h2h_name_player1);
		player1ImageView = (ImageView) findViewById(R.id.h2h_image_player1);
		wins1View = (TextView) findViewById(R.id.h2h_playerwins1);
		age1View = (TextView) findViewById(R.id.h2h_age_player1);
		bPlace1View = (TextView) findViewById(R.id.h2h_birthplace_player1);
		risidence1View = (TextView) findViewById(R.id.h2h_risidence_player1);
		height1View = (TextView) findViewById(R.id.h2h_height_player1);
		weight1View = (TextView) findViewById(R.id.h2h_weight_player1);
		plays1View = (TextView) findViewById(R.id.h2h_plays_player1);
		backhand1View = (TextView) findViewById(R.id.h2h_backhand_player1);
		turnedPro1View = (TextView) findViewById(R.id.h2h_turnedpro_player1);
		ytdWonLost1View = (TextView) findViewById(R.id.h2h_ytdwonlost_player1);
		ytdTitles1View = (TextView) findViewById(R.id.h2h_ytdtitles_player1);
		careerWonLost1View = (TextView) findViewById(R.id.h2h_careerwonlost_player1);
		careerTitles1View = (TextView) findViewById(R.id.h2h_careertitles_player1);
		ytdPrize1View = (TextView) findViewById(R.id.h2h_ytdprize_player1);
		country1View = (TextView) findViewById(R.id.h2h_country_player1);
		careerPrize1View = (TextView) findViewById(R.id.h2h_careerprize_player1);

		name2View = (TextView) findViewById(R.id.h2h_name_player2);
		player2ImageView = (ImageView) findViewById(R.id.h2h_image_player2);
		wins2View = (TextView) findViewById(R.id.h2h_playerwins2);
		age2View = (TextView) findViewById(R.id.h2h_age_player2);
		bPlace2View = (TextView) findViewById(R.id.h2h_birthplace_player2);
		risidence2View = (TextView) findViewById(R.id.h2h_risidence_player2);
		height2View = (TextView) findViewById(R.id.h2h_height_player2);
		weight2View = (TextView) findViewById(R.id.h2h_weight_player2);
		plays2View = (TextView) findViewById(R.id.h2h_plays_player2);
		backhand2View = (TextView) findViewById(R.id.h2h_backhand_player2);
		turnedPro2View = (TextView) findViewById(R.id.h2h_turnedpro_player2);
		ytdWonLost2View = (TextView) findViewById(R.id.h2h_ytdwonlost_player2);
		ytdTitles2View = (TextView) findViewById(R.id.h2h_ytdtitles_player2);
		careerWonLost2View = (TextView) findViewById(R.id.h2h_careerwonlost_player2);
		careerTitles2View = (TextView) findViewById(R.id.h2h_careertitles_player2);
		ytdPrize2View = (TextView) findViewById(R.id.h2h_ytdprize_player2);
		country2View = (TextView) findViewById(R.id.h2h_country_player2);
		careerPrize2View = (TextView) findViewById(R.id.h2h_careerprize_player2);

		risidenceRow = (TableRow) findViewById(R.id.h2h_row_atp_risidence);
		countryRow = (TableRow) findViewById(R.id.h2h_row_wta_country);
		ytdPrizeRow = (TableRow) findViewById(R.id.h2h_row_wta_ytdprize);
		backHandRow = (TableRow) findViewById(R.id.h2h_row_wta_backhand);
		if (atpOrWta == Controller.ATP) {
			risidenceRow.setVisibility(View.VISIBLE);
			backHandRow.setVisibility(View.VISIBLE);
		}
		else if (atpOrWta == Controller.WTA) {
			countryRow.setVisibility(View.VISIBLE);
			ytdPrizeRow.setVisibility(View.VISIBLE);
		}
		
		viewH2hButton.setOnClickListener(this);
		
		controller = new Controller(this);
		
		adapterProvider = new AdapterProvider(this);
		player1Edit.setAdapter(adapterProvider.getPlayerAutoCompleteAdapter(atpOrWta));
		player1Edit.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView tv = (TextView) view.findViewById(android.R.id.text1);
				String engName = tv.getText().toString();
				player1 = controller.getPlayerByName(engName, H2HSearcher.this.atpOrWta);
			}
		});
		player1Edit.setOnFocusChangeListener(this);

		player2Edit.setAdapter(adapterProvider.getPlayerAutoCompleteAdapter(atpOrWta));
		player2Edit.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView tv = (TextView) view.findViewById(android.R.id.text1);
				String engName = tv.getText().toString();
				player2 = controller.getPlayerByName(engName, H2HSearcher.this.atpOrWta);
			}
		});
		player2Edit.setOnFocusChangeListener(this);
		
		player1ImageView.setOnClickListener(this);
		player1ImageView.setOnLongClickListener(this);
		player2ImageView.setOnClickListener(this);
		player2ImageView.setOnLongClickListener(this);

		if (atpOrWta == Controller.ATP) {
			parser = new ATPParser(this, parseListener);
		}
		else {
			parser = new WTAParser(this, parseListener);
		}
		
		actionBar = new ActionBar(this, this);
		actionBar.setTitle(getResources().getString(R.string.h2h_title));
	}

	Parser.OnParseListener parseListener = new Parser.OnParseListener() {

		@Override
		public void onParseOk(int requestCode, Object object) {
			if (requestCode == ParserHttpThread.PARSER_MODE_H2H) {
				h2hEntity = (H2HEntity) object;
				initializePage(h2hEntity);
				progressDialog.dismiss();
			}
			else if (requestCode == ParserHttpThread.PARSER_MODE_GET_IMAGE) {
				Bundle bundle = (Bundle) object;
				Bitmap bitmap = bundle.getParcelable("bitmap");
				if (bitmap == null) {
					if (bundle.getInt("playerIndex") == 1) {
						setImageAsLoadError(player1ImageView);
						hourglassAnimDrawable1.stop();
					}
					else {
						setImageAsLoadError(player2ImageView);
						hourglassAnimDrawable2.stop();
					}
				}
				else {
					if (bundle.getInt("playerIndex") == 1) {
						player1ImageView.setImageBitmap(bitmap);
						cacheController.cacheImage(bitmap, player1.getEngName());
					}
					else {
						player2ImageView.setImageBitmap(bitmap);
						cacheController.cacheImage(bitmap, player2.getEngName());
					}
				}
			}
		}
		
		@Override
		public void onParseFail(int requestCode, Object object) {
			if (requestCode == ParserHttpThread.PARSER_MODE_H2H) {
				Toast.makeText(H2HSearcher.this, object.toString(), Toast.LENGTH_LONG).show();
				progressDialog.dismiss();
			}
			else if (requestCode == ParserHttpThread.PARSER_MODE_GET_IMAGE) {
				int index = (Integer) object;
				if (index == 1) {
					setImageAsLoadError(player1ImageView);
				}
				else {
					setImageAsLoadError(player2ImageView);
				}
			}
		}
	};

	@Override
	public void onFocusChange(View view, boolean focused) {
		if (view instanceof EditText) {
			EditText edit = (EditText) view;
			if (focused) {
				edit.setSelectAllOnFocus(true);
			}
		}
	}

	@Override
	public boolean onLongClick(View v) {
		if (v == player1ImageView) {
			if (h2hEntity != null) {
				if (h2hEntity.getImgUrl1() != null) {
					//TODO save
				}
			}
		}
		else if (v == player2ImageView) {
			if (h2hEntity != null) {
				if (h2hEntity.getImgUrl2() != null) {
					//TODO save
				}
			}
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v == viewH2hButton) {
			
			if (player1 == null) {
				player1Edit.setError("Player not available! Please select from dropdown list!");
				return;
			}
			else {
				String name = player1Edit.getText().toString();
				if (!player1.getEngName().equals(name)) {
					player1Edit.setError("Player not available! Please select from dropdown list!");
					return;
				}
			}
			if (player2 == null) {
				player2Edit.setError("Player not available! Please select from dropdown list!");
				return;
			}
			else {
				String name = player2Edit.getText().toString();
				if (!player2.getEngName().equals(name)) {
					player2Edit.setError("Player not available! Please select from dropdown list!");
					return;
				}
			}
			
			hideSoftKeyboard();

			if (cacheController == null) {
				cacheController = new CacheController();
			}

			setImageAsLoading(player1ImageView, 1);
			setImageAsLoading(player2ImageView, 2);
			
			Bitmap bitmap1 = cacheController.getPlayerImage(player1.getEngName());
			Bitmap bitmap2 = cacheController.getPlayerImage(player2.getEngName());
			boolean getP1Image = true;
			boolean getP2Image = true;
			if (bitmap1 != null) {
				player1ImageView.setImageBitmap(bitmap1);
				getP1Image = false;
			}
			if (bitmap2 != null) {
				player2ImageView.setImageBitmap(bitmap2);
				getP2Image = false;
			}
			
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("loading");
			progressDialog.show();
			parser.searchH2h(player1, player2, getP1Image, getP2Image);
			
		}
		else if (v == player1ImageView) {
			if (h2hEntity != null) {
				if (h2hEntity.getImgUrl1() != null) {
					setImageAsLoading(player1ImageView, 1);
					parser.loadImage(h2hEntity.getImgUrl1(), 1);
				}
			}
		}
		else if (v == player2ImageView) {
			if (h2hEntity != null) {
				if (h2hEntity.getImgUrl2() != null) {
					setImageAsLoading(player2ImageView, 2);
					parser.loadImage(h2hEntity.getImgUrl2(), 2);
				}
			}
		}
	}

	private void hideSoftKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
		if (imm.isActive()) {
			View focusedView = player1Edit;
			if (player2Edit.isFocused()) {
				focusedView = player2Edit;
			}
			imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
		}
	}

	private void setImageAsLoading(ImageView imageView, final int playerIndex) {
		if (playerIndex == 1) {
			imageView.setImageDrawable(hourglassAnimDrawable1);
		}
		else {
			imageView.setImageDrawable(hourglassAnimDrawable2);
		}
		//imageView.setImageResource(R.drawable.image_loading);
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if (playerIndex == 1) {
					hourglassAnimDrawable1.start();
				}
				else {
					hourglassAnimDrawable2.start();
				}
			}
		}, 100);
	}

	private void setImageAsLoadError(ImageView imageView) {
		imageView.setImageResource(R.drawable.image_load_error);
	}

	protected void initializePage(H2HEntity entity) {
		
		if (tableLayout.getVisibility() == View.GONE) {
			tableLayout.setVisibility(View.VISIBLE);
		}
		
		//v5.6.1 change, atpworldtour website updated, no rank anymore
		String nameRow = player1.getEngName();
		nameRow = entity.getRank1() + "  " + player1.getEngName();
		
		name1View.setText(nameRow);
		wins1View.setText("" + entity.getWins1());
		age1View.setText(entity.getAge1());
		bPlace1View.setText(entity.getBirthPlace1());
		if (atpOrWta == Controller.ATP) {
			risidence1View.setText(entity.getRisidence1());
			backhand1View.setText(entity.getBackhand1());
		}
		height1View.setText(entity.getHeight1());
		weight1View.setText(entity.getWeight1());
		plays1View.setText(entity.getPlays1());
		turnedPro1View.setText(entity.getTurnedPro1());
		ytdWonLost1View.setText(entity.getYtdWonLost1());
		ytdTitles1View.setText(entity.getYtdTitles1());
		careerWonLost1View.setText(entity.getCareerWonLost1());
		careerTitles1View.setText(entity.getCareerTitles1());
		careerPrize1View.setText(entity.getCareerPrize1());
		if (atpOrWta == Controller.WTA) {
			country1View.setText(entity.getCountry1());
			ytdPrize1View.setText(entity.getYtdPrize1());
		}
		nameRow = player2.getEngName();
		nameRow = entity.getRank2() + "  " + player2.getEngName();
		name2View.setText(nameRow);
		wins2View.setText("" + entity.getWins2());
		age2View.setText(entity.getAge2());
		bPlace2View.setText(entity.getBirthPlace2());
		if (atpOrWta == Controller.ATP) {
			risidence2View.setText(entity.getRisidence2());
			backhand2View.setText(entity.getBackhand2());
		}
		height2View.setText(entity.getHeight2());
		weight2View.setText(entity.getWeight2());
		plays2View.setText(entity.getPlays2());
		turnedPro2View.setText(entity.getTurnedPro2());
		ytdWonLost2View.setText(entity.getYtdWonLost2());
		ytdTitles2View.setText(entity.getYtdTitles2());
		careerWonLost2View.setText(entity.getCareerWonLost2());
		careerTitles2View.setText(entity.getCareerTitles2());
		careerPrize2View.setText(entity.getCareerPrize2());
		if (atpOrWta == Controller.WTA) {
			country2View.setText(entity.getCountry2());
			ytdPrize2View.setText(entity.getYtdPrize2());
		}
		
		scrollLayout.removeAllViews();
		H2HDetailAdapter adapter = new H2HDetailAdapter(this, entity.getRecordList());
		for (int i = 0; i < adapter.getCount(); i ++) {
			scrollLayout.addView(adapter.getView(i, null, null));
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	private String fmtString(String str) {
		String notice = "";
		try {
			notice = URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException ex) {
		}
		return notice;
	}

	@Override
	public void onBack() {
		finish();
	}

	@Override
	public void onDelete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onOk() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onIconClick(View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ListAdapter createMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListAdapter onPrepareMenu() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onMenuSelected(int index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(String text, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

}
