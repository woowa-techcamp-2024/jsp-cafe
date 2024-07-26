package com.jspcafe.test_util;

import com.jspcafe.util.DatabaseConnector;

import org.h2.jdbcx.JdbcConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;

public class H2Connector implements DatabaseConnector {
    public static final DatabaseConnector INSTANCE = new H2Connector();
    private static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private final JdbcConnectionPool connectionPool;

    public H2Connector() {
        connectionPool = JdbcConnectionPool.create(URL, USER, PASSWORD);
        connectionPool.setMaxConnections(10);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

    @Override
    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
