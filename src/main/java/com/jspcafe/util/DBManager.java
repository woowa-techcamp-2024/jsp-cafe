package com.jspcafe.util;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DBManager {
    private static final String URL = "jdbc:mysql://mysql:3306/jspcafe?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "jspcafe";
    private static final String PASSWORD = "jspcafe";

    private static MysqlConnectionPoolDataSource dataSource;

    static {
        try {
            dataSource = new MysqlConnectionPoolDataSource();
            dataSource.setURL(URL);
            dataSource.setUser(USER);
            dataSource.setPassword(PASSWORD);

            dataSource.setMaxReconnects(5);
            dataSource.setInitialTimeout(2);
            dataSource.setAutoReconnect(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
