package com.king.mytennis.net.html;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.king.mytennis.view.player.WorldPlayer;

public class SqlOperator {

	public SqlOperator() {
		try {
			Class.forName("org.sqldroid.SqldroidDriver").newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public Connection connect(String dbFile) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:sqldroid:" + dbFile);
			return connection;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean insertPlayer(WorldPlayer player, Connection connection, String table) {

		if (connection != null && player != null) {
			String sql = null;
			if (player.getSupportMyName() == null) {
				sql = "INSERT INTO " + table + "(pa_id, pa_name_eng)  VALUES(?,?)";
			}
			else {
				sql = "INSERT INTO " + table + "(pa_id, pa_name_eng, pa_support_my)  VALUES(?,?,?)";
			}
			PreparedStatement stmt = null;
			try {
				stmt = connection.prepareStatement(sql);
				stmt.setString(1, player.getId());
				stmt.setString(2, player.getEngName());
				if (player.getSupportMyName() != null) {
					stmt.setString(3, player.getSupportMyName());
				}

				stmt.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return false;
	}

	public boolean updatePlayer(WorldPlayer player, Connection connection, String table) {
		if (connection != null && player != null) {

			//普通Statement''需要加，PreparedStatement不能加
			String sql = null;
			if (player.getSupportMyName() == null) {
				sql = "UPDATE " + table + " SET pa_name_eng=? WHERE pa_id=?";
			}
			else {
				sql = "UPDATE " + table + " SET pa_name_eng=?, pa_support_my=? WHERE pa_id=?";
			}
			PreparedStatement stmt = null;
			try {
				stmt = connection.prepareStatement(sql);
				stmt.setString(1, player.getEngName());
				if (player.getSupportMyName() == null) {
					stmt.setString(2, player.getId());
				}
				else {
					stmt.setString(2, player.getSupportMyName());
					stmt.setString(3, player.getId());
				}

				stmt.executeUpdate();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} finally {
				if (stmt != null) {
					try {
						stmt.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return false;
	}

	public List<WorldPlayer> matchPlayerByKey(String name, Connection connection, String table) {
		List<WorldPlayer> list = null;
		Statement stmt = null;
		WorldPlayer player = null;
		try {
			stmt = connection.createStatement();
			ResultSet set = null;

			set = stmt.executeQuery("SELECT * FROM " + table + " WHERE pa_name_eng LIKE '%" + name + "%'");

			while (set.next()) {
				if (list == null) {
					list = new ArrayList<WorldPlayer>();
				}
				player = new WorldPlayer();
				player.setId(set.getString("pa_id"));
				player.setEngName(name);
				player.setSupportMyName(set.getString("pa_support_my"));
				player.setInfor(set.getString("pa_infor"));
				player.setMore(set.getString("pa_more"));
				list.add(player);
			}
			set.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public WorldPlayer queryPlayerByEngName(String name, Connection connection, String table) {
		Statement stmt = null;
		WorldPlayer player = null;
		try {
			stmt = connection.createStatement();
			ResultSet set = null;

			set = stmt.executeQuery("SELECT * FROM " + table + " WHERE pa_name_eng = '" + name + "'");

			if (set.next()) {
				player = new WorldPlayer();
				player.setId(set.getString("pa_id"));
				player.setEngName(name);
				player.setSupportMyName(set.getString("pa_support_my"));
				player.setInfor(set.getString("pa_infor"));
				player.setMore(set.getString("pa_more"));
			}
			set.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return player;
	}
	public List<WorldPlayer> queryAllPlayers(Connection connection, String table) {
		List<WorldPlayer> list = null;
		Statement stmt = null;
		WorldPlayer player = null;
		try {
			stmt = connection.createStatement();
			ResultSet set = null;

			set = stmt.executeQuery("SELECT * FROM " + table + " ORDER BY pa_name_eng");

			while (set.next()) {
				if (list == null) {
					list = new ArrayList<WorldPlayer>();
				}
				player = new WorldPlayer();
				player.setId(set.getString("pa_id"));
				player.setEngName(set.getString("pa_name_eng"));
				player.setSupportMyName(set.getString("pa_support_my"));
				player.setInfor(set.getString("pa_infor"));
				player.setMore(set.getString("pa_more"));
				list.add(player);
			}
			set.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
