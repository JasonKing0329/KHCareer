package com.king.mytennis.view.player;

import com.king.mytennis.view.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PlayerUpdateView implements android.view.View.OnClickListener {

	private final int MODE_INSERT = 0;
	private final int MODE_UPDATE = 1;
	private int mode;
	private int atpOrWta;
	
	private Context mContext;
	private TextView modeView;
	private Button continueButton, resetButton;
	private EditText idEdit, supportMyEdit;
	private AutoCompleteTextView engNameEdit;
	private Controller controller;
	private AdapterProvider adapterProvider;
	private WorldPlayer updatePlayer;
	
	public PlayerUpdateView(Context context, int atpOrWta) {
		mContext = context;
		this.atpOrWta = atpOrWta;
	}
	
	public void show() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Update player");
		
		View view = LayoutInflater.from(mContext).inflate(R.layout.player_update, null);
		continueButton = (Button) view.findViewById(R.id.player_update_continue);
		resetButton = (Button) view.findViewById(R.id.player_update_reset);
		modeView = (TextView) view.findViewById(R.id.player_update_mode);
		idEdit = (EditText) view.findViewById(R.id.player_update_id);
		engNameEdit = (AutoCompleteTextView) view.findViewById(R.id.player_update_eng_name);
		supportMyEdit = (EditText) view.findViewById(R.id.player_update_support_my);
		
		resetButton.setOnClickListener(this);
		continueButton.setOnClickListener(this);
		modeView.setText("MODE_INSERT");
		
		controller = new Controller(mContext);
		adapterProvider = new AdapterProvider(mContext);
		engNameEdit.setAdapter(adapterProvider.getPlayerAutoCompleteAdapter(atpOrWta));
		engNameEdit.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				TextView tv = (TextView) view.findViewById(android.R.id.text1);
				String engName = tv.getText().toString();
				updatePlayer = controller.getPlayerByName(engName, atpOrWta);
				idEdit.setText(updatePlayer.getId());
				supportMyEdit.setText(updatePlayer.getSupportMyName());
				mode = MODE_UPDATE;
				modeView.setText("MODE_UPDATE");
			}
		});
		
		builder.setView(view);
		builder.setPositiveButton("OK", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				insertOrUpdate();
			}
		});
		builder.setNegativeButton("Cancel", null);
		builder.show();
	}

	private void insertOrUpdate() {
		String id = idEdit.getText().toString();
		String nameEng = engNameEdit.getText().toString();
		String nameMySuport = supportMyEdit.getText().toString();
		
		if (mode == MODE_INSERT) {
			WorldPlayer player = new WorldPlayer();
			player.setId(id);
			player.setEngName(nameEng);
			player.setSupportMyName(nameMySuport);
			
			controller.insertPlayer(player, atpOrWta);
			adapterProvider.updatePlayerAutoCompleteAdapter(atpOrWta);
		}
		else if (mode == MODE_UPDATE) {
			updatePlayer.setId(id);
			updatePlayer.setEngName(nameEng);
			updatePlayer.setSupportMyName(nameMySuport);
			controller.updatePlayer(updatePlayer, atpOrWta);
			
			adapterProvider.updatePlayerAutoCompleteAdapter(atpOrWta);
		}
	}
	
	@Override
	public void onClick(View v) {
		if (v == continueButton) {
			
			insertOrUpdate();
			
			idEdit.setText("");
			engNameEdit.setText("");
			supportMyEdit.setText("");
			modeView.setText("MODE_INSERT");
		}
		else if (v == resetButton) {
			
			mode = MODE_INSERT;
			
			idEdit.setText("");
			engNameEdit.setText("");
			supportMyEdit.setText("");
			modeView.setText("MODE_INSERT");
		}
	}
}
