package org.example.jspcafe.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SimpleConnectionPool {
    private static final int INITIAL_POOL_SIZE = 10;
    private static final int MAX_POOL_SIZE = 20;
    private static List<Connection> connectionPool;
    private static List<Connection> usedConnections = new ArrayList<>();

    private static SimpleConnectionPool instance;

    private SimpleConnectionPool() {
        connectionPool = new ArrayList<>(INITIAL_POOL_SIZE);
        for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
            connectionPool.add(createConnection());
        }
    }

    public static synchronized SimpleConnectionPool getInstance() {
        if (instance == null) {
            instance = new SimpleConnectionPool();
        }
        return instance;
    }

    public synchronized Connection getConnection() throws SQLException {
        if (connectionPool.isEmpty()) {
            if (usedConnections.size() < MAX_POOL_SIZE) {
                connectionPool.add(createConnection());
            } else {
                throw new SQLException("Maximum pool size reached, no available connections!");
            }
        }

        Connection connection = connectionPool.remove(connectionPool.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    public static synchronized boolean releaseConnection(Connection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    private Connection createConnection() {
        try {
            return JdbcTemplate.getConnection();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to create a new connection!");
        }
    }

    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }

    public static void shutdown() throws SQLException {
        usedConnections.forEach(SimpleConnectionPool::releaseConnection);
        for (Connection c : connectionPool) {
            c.close();
        }
        connectionPool.clear();
    }
}