package codesqaud.app.db;

import codesqaud.app.db.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    private static final Logger log = LoggerFactory.getLogger(TransactionManager.class);

    private static final ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    public static Connection getInProgressConnection() {
        return connectionHolder.get();
    }

    public static void executeTransaction(DataSource dataSource, Runnable runnable) {
        executeTransaction(dataSource, Connection.TRANSACTION_REPEATABLE_READ, runnable);
    }

    public static void executeTransaction(DataSource dataSource, int isolationLevel, Runnable runnable) {
        try {
            startTransaction(dataSource, isolationLevel);
            runnable.run();
            commit();
        } catch (Exception e) {
            try {
                rollback();
                throw e;
            } catch (SQLException rollbackException) {
                log.error(rollbackException.getMessage(), rollbackException);
            }
        } finally {
            closeConnection();
        }
    }

    public static void startTransaction(DataSource dataSource, int isolationLevel) throws SQLException {
        Connection connection = connectionHolder.get();
        if (connection != null) {
            log.error("이전 요청의 트랜잭션 커넥션이 제거되지 않았거나 트랜잭션을 중첩해서 시작하려고 시도했습니다.");
            throw new TransactionException("Transaction is already started");
        }
        connection = dataSource.getConnection();
        connection.setAutoCommit(false);
        connection.setTransactionIsolation(isolationLevel);
        connectionHolder.set(connection);
    }

    public static void commit() throws SQLException {
        Connection connection = connectionHolder.get();
        if(connection == null) {
            throw new TransactionException("진행중인 트랜잭션이 없습니다.");
        }

        connection.commit();
    }

    public static void rollback() throws SQLException {
        Connection connection = connectionHolder.get();
        if(connection == null) {
            throw new TransactionException("진행중인 트랜잭션이 없습니다.");
        }

        connection.rollback();
    }


    private static void closeConnection() {
        Connection connection = connectionHolder.get();
        try {
            connection.close();
        } catch (SQLException closeException) {
            log.error(closeException.getMessage(), closeException);
        } finally {
            connectionHolder.remove();
        }
    }
}
