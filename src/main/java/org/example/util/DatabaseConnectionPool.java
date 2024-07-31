package org.example.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;

@Component
public class DatabaseConnectionPool {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/cafe";
    private static final String DB_USER = "cafeuser";
    private static final String DB_PASSWORD = "cafeuser_password";
    private static final int MAX_POOL_SIZE = 10;

    private final List<Connection> connectionPool;
    private final List<Connection> usedConnections = new ArrayList<>();
    private final Lock lock = new ReentrantLock();

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("MySQL JDBC Driver not found. " + e.getMessage());
        }
    }

    @Autowired
    public DatabaseConnectionPool() throws SQLException {
        connectionPool = new ArrayList<>(MAX_POOL_SIZE);
        for (int i = 0; i < MAX_POOL_SIZE; i++) {
            connectionPool.add(createConnection());
        }
    }

    public Connection getConnection() throws SQLException {
        lock.lock();
        try {
            if (connectionPool.isEmpty()) {
                if (usedConnections.size() < MAX_POOL_SIZE) {
                    connectionPool.add(createConnection());
                } else {
                    throw new SQLException("Connection limit reached, no available connections!");
                }
            }

            Connection connection = connectionPool.remove(connectionPool.size() - 1);
            usedConnections.add(connection);
            return connection;
        } finally {
            lock.unlock();
        }
    }

    public boolean releaseConnection(Connection connection) {
        lock.lock();
        try {
            if (usedConnections.remove(connection)) {
                connectionPool.add(connection);
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
    }

    public int getSize() {
        return connectionPool.size() + usedConnections.size();
    }

    public void shutdown() throws SQLException {
        usedConnections.forEach(this::releaseConnection);
        for (Connection c : connectionPool) {
            c.close();
        }
        connectionPool.clear();
    }
}