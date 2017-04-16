package com.king.khcareer.model.sql.pubdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by JingYang on 2016/7/1 0001.
 * Description:
 */
public class SqlConnection {

    private Connection connection;
    private static SqlConnection instance;

    public static SqlConnection getInstance() {
        if (instance == null) {
            instance = new SqlConnection();
        }
        return  instance;
    }

    private SqlConnection() {
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
            connection = DriverManager.getConnection("jdbc:sqldroid:" + dbFile);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
