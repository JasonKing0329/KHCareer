package com.king.mytennis.pubdata.dao;

import com.king.mytennis.model.DatabaseStruct;
import com.king.mytennis.pubdata.bean.MatchBean;
import com.king.mytennis.pubdata.bean.MatchNameBean;

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
public class MatchDao {
    /**
     * order by week
     * @param connection
     * @param level
     * @return
     */
    public List<MatchNameBean> queryMatchList(Connection connection, String level) {
        List<MatchNameBean> list = new ArrayList<>();
        StringBuffer buffer = new StringBuffer("SELECT m.*, mn._id as name_id, mn.name FROM ");
        buffer.append(DatabaseStruct.TABLE_MATCH).append(" m, ").append(DatabaseStruct.TABLE_MATCH_NAME)
                .append(" mn WHERE m._id = mn.match_id");
        if (level != null) {
            buffer.append(" AND m.level='").append(level).append("'");
        }
        buffer.append(" ORDER BY m.week ASC");

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(buffer.toString());
            while (set.next()) {
                MatchNameBean bean = parseMatchBean(set);
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

    private MatchNameBean parseMatchBean(ResultSet set) throws SQLException {
        MatchNameBean nameBean = new MatchNameBean();
        MatchBean bean = new MatchBean();
        bean.setId(set.getInt(1));
        bean.setLevel(set.getString(2));
        bean.setCourt(set.getString(3));
        bean.setRegion(set.getString(4));
        bean.setCountry(set.getString(5));
        bean.setCity(set.getString(6));
        bean.setWeek(set.getInt(7));
        bean.setMonth(set.getInt(8));
        nameBean.setMatchBean(bean);
        nameBean.setId(set.getInt(9));
        nameBean.setName(set.getString(10));
        nameBean.setMatchId(bean.getId());
        return nameBean;
    }

    public void insertMatchBean(MatchBean bean, Connection connection) {
        String sql = "INSERT INTO " + DatabaseStruct.TABLE_MATCH +
                "(level,court,region,country,city,week,month) VALUES(?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, bean.getLevel());
            stmt.setString(2, bean.getCourt());
            stmt.setString(3, bean.getRegion());
            stmt.setString(4, bean.getCountry());
            stmt.setString(5, bean.getCity());
            stmt.setInt(6, bean.getWeek());
            stmt.setInt(7, bean.getMonth());
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

    public void insertMatchNameBean(MatchNameBean bean, Connection connection) {
        String sql = "INSERT INTO " + DatabaseStruct.TABLE_MATCH_NAME +
                "(name, match_id) VALUES(?,?)";
        PreparedStatement stmt = null;
        try {
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, bean.getName());
            stmt.setInt(2, bean.getMatchId());
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

    public void updateMatchBean(MatchBean bean, Connection connection) {
        StringBuffer buffer = new StringBuffer("UPDATE ");
        buffer.append(DatabaseStruct.TABLE_MATCH).append(" SET level='").append(bean.getLevel())
                .append("',court='").append(bean.getCourt())
                .append("', region='").append(bean.getRegion())
                .append("', country='").append(bean.getCountry())
                .append("', city='").append(bean.getCity())
                .append("', week=").append(bean.getWeek())
                .append(", month=").append(bean.getMonth())
                .append(" WHERE _id=").append(bean.getId());
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

    public void updateMatchNameBean(MatchNameBean bean, Connection connection) {
        StringBuffer buffer = new StringBuffer("UPDATE ");
        buffer.append(DatabaseStruct.TABLE_MATCH_NAME).append(" SET name='").append(bean.getName())
                .append("',match_id=").append(bean.getMatchId())
                .append(" WHERE _id=").append(bean.getId());
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

    public void deleteMatch(int matchId, Connection connection) {
        String sql = "DELETE FROM " + DatabaseStruct.TABLE_MATCH + " WHERE _id=" + matchId;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMatchNameByMatchId(int matchId, Connection connection) {
        String sql = "DELETE FROM " + DatabaseStruct.TABLE_MATCH_NAME + " WHERE match_id=" + matchId;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMatchName(int id, Connection connection) {
        String sql = "DELETE FROM " + DatabaseStruct.TABLE_MATCH_NAME + " WHERE _id=" + id;
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int queryLastMatchBeanSequence(Connection connection) {
        String sql = "SELECT * FROM " + DatabaseStruct.TABLE_SEQUENCE + " WHERE name='" + DatabaseStruct.TABLE_MATCH + "'";
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

    public int queryLastMatchNameBeanSequence(Connection connection) {
        String sql = "SELECT * FROM " + DatabaseStruct.TABLE_SEQUENCE + " WHERE name='" + DatabaseStruct.TABLE_MATCH_NAME + "'";
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

    public MatchNameBean queryMatchByName(String name, Connection connection) {
        MatchNameBean bean = null;
        String sql = "SELECT m.*, mn._id as name_id, mn.name FROM " +  DatabaseStruct.TABLE_MATCH + " m, "
                + DatabaseStruct.TABLE_MATCH_NAME + " mn WHERE m._id = mn.match_id AND mn.name='" + name + "'";
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            if (set.next()) {
                bean = parseMatchBean(set);
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

    public List<MatchNameBean> queryMatchNameList(int matchId, Connection connection) {
        List<MatchNameBean> list = new ArrayList<>();
        String sql = "SELECT * FROM " +  DatabaseStruct.TABLE_MATCH_NAME + " WHERE match_id = " + matchId;
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet set = stmt.executeQuery(sql);
            while (set.next()) {
                list.add(parseMatchNameBean(set));
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

    private MatchNameBean parseMatchNameBean(ResultSet set) throws SQLException {
        MatchNameBean nameBean = new MatchNameBean();
        nameBean.setId(set.getInt(1));
        nameBean.setName(set.getString(2));
        nameBean.setMatchId(set.getInt(3));
        return nameBean;
    }

}
