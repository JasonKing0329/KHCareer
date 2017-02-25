package com.king.mytennis.pubdata.dao;

import com.king.mytennis.model.DatabaseStruct;
import com.king.mytennis.pubdata.bean.PlayerBean;
import com.king.mytennis.pubdata.bean.PlayerBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * <p/>作者：景阳
 * <p/>创建时间: 2017/2/24 14:11
 */
public class PlayerDao {
    public List<PlayerBean> queryPlayerList(Connection connection, boolean orderByPinyin) {
        List<PlayerBean> list = new ArrayList<>();
        String sql = "SELECT * FROM " + DatabaseStruct.TABLE_PLAYER;
        if (orderByPinyin) {
            sql = sql.concat(" ORDER BY name_pinyin ASC");
        }
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            while (set.next()) {
                PlayerBean bean = parsePlayerBean(set);
                list.add(bean);
            }
        } catch (SQLException e) {
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
        return list;
    }

    private PlayerBean parsePlayerBean(ResultSet set) throws SQLException {
        PlayerBean bean = new PlayerBean();
        bean.setId(set.getInt(1));
        bean.setNameEng(set.getString(2));
        bean.setNameChn(set.getString(3));
        bean.setNamePinyin(set.getString(4));
        bean.setCountry(set.getString(5));
        bean.setCity(set.getString(6));
        bean.setBirthday(set.getString(7));
        return bean;
    }

    public void inserPlayerBean(PlayerBean bean, Connection connection) {
        String sql = "INSERT INTO " + DatabaseStruct.TABLE_PLAYER +
                "(name_eng,name_chn,name_pinyin,country,city,birthday) VALUES(?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, bean.getNameEng());
            stmt.setString(2, bean.getNameChn());
            stmt.setString(3, bean.getNamePinyin());
            stmt.setString(4, bean.getCountry());
            stmt.setString(5, bean.getCity());
            stmt.setString(6, bean.getBirthday());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void updatePlayerBean(PlayerBean bean, Connection connection) {
        StringBuffer buffer = new StringBuffer("UPDATE ");
        buffer.append(DatabaseStruct.TABLE_PLAYER).append(" SET name_eng='").append(bean.getNameEng())
                .append("',name_chn='").append(bean.getNameChn())
                .append("', name_pinyin='").append(bean.getNamePinyin())
                .append("', country='").append(bean.getCountry())
                .append("', city='").append(bean.getCity())
                .append("', birthday='").append(bean.getBirthday())
                .append("' WHERE _id=").append(bean.getId());
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(buffer.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void deletePlayer(int playerId, Connection connection) {
        String sql = "DELETE FROM " + DatabaseStruct.TABLE_PLAYER + " WHERE _id=" + playerId;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int queryLastPlayerBeanSequence(Connection connection) {
        String sql = "SELECT * FROM " + DatabaseStruct.TABLE_SEQUENCE + " WHERE name='" + DatabaseStruct.TABLE_PLAYER + "'";
        Statement statement = null;
        int id = 0;
        try {
            statement = connection.createStatement();
            ResultSet set = statement.executeQuery(sql);
            if (set.next()) {
                id = set.getInt(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    public PlayerBean queryPlayerByChnName(String name, Connection connection) {
        PlayerBean bean = null;
        String sql = "SELECT * FROM " + DatabaseStruct.TABLE_PLAYER  + " WHERE name_chn='" + name + "'" ;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            if (set.next()) {
                bean = parsePlayerBean(set);
            }
        } catch (SQLException e) {
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
        return bean;
    }
}
