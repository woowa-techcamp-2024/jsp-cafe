package cafe.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class JdbcConnectionPool implements ConnectionPool {
    private final String jdbcUrl;
    private final String user;
    private final String password;
    private final int maxPoolSize;
    private final Queue<PooledConnection> availableConnections = new LinkedList<>();

    public JdbcConnectionPool(String jdbcUrl, String user, String password, int maxPoolSize) {
        this.jdbcUrl = jdbcUrl;
        this.user = user;
        this.password = password;
        this.maxPoolSize = maxPoolSize;
        initializeConnections();
    }

    private void initializeConnections() {
        for (int i = 0; i < maxPoolSize; i++) {
            try {
                Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
                availableConnections.add(new PooledConnection(conn, this));
            } catch (SQLException e) {
                // 적절한 예외 처리: 에러 로깅 추가
                System.err.println("Error initializing connection: " + e.getMessage());
            }
        }
    }

    @Override
    public synchronized Connection getConnection() throws SQLException {
        while (availableConnections.isEmpty()) {
            try {
                wait(); // Wait until a connection becomes available
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new SQLException("Interrupted while waiting for a connection", e);
            }
        }
        PooledConnection connection = availableConnections.poll();
        if (isConnectionValid(connection)) {
            connection.isClosed = false; // Reset the closed status when reusing
            return connection;
        } else {
            // If the connection is not valid, create a new one
            return new PooledConnection(DriverManager.getConnection(jdbcUrl, user, password), this);
        }
    }

    private boolean isConnectionValid(PooledConnection connection) {
        try {
            return connection != null && !connection.getOriginalConnection().isClosed() &&
                    connection.getOriginalConnection().isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public synchronized void releaseConnection(PooledConnection connection) {
        if (connection != null) {
            availableConnections.add(connection);
            notifyAll(); // Notify waiting threads
        }
    }

    // 추가: 풀을 닫는 메서드
    public synchronized void shutdown() {
        for (PooledConnection pooledConnection : availableConnections) {
            try {
                pooledConnection.getOriginalConnection().close();
            } catch (SQLException e) {
                // 적절한 예외 처리: 에러 로깅 추가
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
        availableConnections.clear();
    }
}
