package com.king.mytennis.view.player;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import android.content.Context;

import com.king.mytennis.model.Configuration;
import com.king.mytennis.net.html.SqlOperator;
import com.king.mytennis.service.ExternalRecordTool;

public class Controller {

	public static final int ATP = 0;
	public static final int WTA = 1;
	
	private String TABLE_ATP = "player_atp";
	private String TABLE_WTA = "player_wta";
	
	public Controller(Context context) {

	}
	
	public void insertPlayer(WorldPlayer player, int atpOrWta) {

		SqlOperator operator = new SqlOperator();
		Connection connection = operator.connect(Configuration.DATABASE_CONTENT + ExternalRecordTool.DATABASE_PLAYER);

		if (atpOrWta == ATP) {
			operator.insertPlayer(player, connection, TABLE_ATP);
		}
		else {
			operator.insertPlayer(player, connection, TABLE_WTA);
		}

		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updatePlayer(WorldPlayer player, int atpOrWta) {
		SqlOperator operator = new SqlOperator();
		Connection connection = operator.connect(Configuration.DATABASE_CONTENT + ExternalRecordTool.DATABASE_PLAYER);

		if (atpOrWta == ATP) {
			operator.updatePlayer(player, connection, TABLE_ATP);
		}
		else {
			operator.updatePlayer(player, connection, TABLE_WTA);
		}

		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public WorldPlayer getPlayerByName(String engName, int atpOrWta) {

		WorldPlayer player = null;
		SqlOperator operator = new SqlOperator();
		Connection connection = operator.connect(Configuration.DATABASE_CONTENT + ExternalRecordTool.DATABASE_PLAYER);

		if (atpOrWta == ATP) {
			player = operator.queryPlayerByEngName(engName, connection, TABLE_ATP);
		}
		else {
			player = operator.queryPlayerByEngName(engName, connection, TABLE_WTA);
		}
		
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return player;
	}
	
	public List<WorldPlayer> getAllPlayer(int atpOrWta) {

		List<WorldPlayer> list = null;
		SqlOperator operator = new SqlOperator();
		Connection connection = operator.connect(Configuration.DATABASE_CONTENT + ExternalRecordTool.DATABASE_PLAYER);

		if (atpOrWta == ATP) {
			list = operator.queryAllPlayers(connection, TABLE_ATP);
		}
		else {
			list = operator.queryAllPlayers(connection, TABLE_WTA);
		}
		
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
