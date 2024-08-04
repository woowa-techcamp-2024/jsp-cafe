package codesquad.common.db.transaction;

import codesquad.common.db.connection.ConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcTransactionManager implements TxManager {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTransactionManager.class);

    private final ConnectionManager connectionManager;

    private ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public JdbcTransactionManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void begin() {
        logger.info("beginning transaction");
        if (connectionHolder.get() == null) {
            Connection connection = connectionManager.getConnection();
            connectionHolder.set(connection);
            logger.info("acquired connection");
            try {
                connection.setAutoCommit(false);
                logger.info("set autoCommit off {}", connection);
            } catch (SQLException e) {
                logger.info("could not set autoCommit off", e);
                closeConnection();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void commit() {
        logger.info("commiting transaction");
        Connection connection = connectionHolder.get();
        try {
            if (connection == null) {
                throw new RuntimeException("connection not found");
            }
            connection.commit();
            logger.info("transaction committed");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    @Override
    public void rollback() {
        logger.info("rolling back transaction");
        Connection connection = connectionHolder.get();
        try {
            if (connection == null) {
                throw new RuntimeException("connection not found");
            }
            connection.rollback();
            logger.info("transaction rolled back");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
    }

    private void closeConnection() {
        Connection connection = connectionHolder.get();
        if (connection != null) {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            connectionManager.close(connection);
            connectionHolder.remove();
        }
    }

    @Override
    public Connection getConnection() {
        logger.info("getting connection");
        if (connectionHolder.get() == null) {
            return connectionManager.getConnection();
        }
        logger.info("open transaction found, return open connection");
        return connectionHolder.get();
    }

    @Override
    public void close(Connection connection) {
        close(connection, null, null);
    }

    @Override
    public void close(Connection connection, Statement statement) {
        close(connection, statement, null);
    }

    @Override
    public void close(Connection connection, Statement statement, ResultSet resultSet) {
        if (resultSet != null) {
            logger.info("closing result set");
            try {
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (statement != null) {
            logger.info("closing statement");
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (connection != null) {
            logger.info("closing connection");
            if (connectionHolder.get() != null) {
                logger.info("open transaction found, stop closing connection");
                return;
            }
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
