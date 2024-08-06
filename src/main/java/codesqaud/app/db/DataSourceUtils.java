package codesqaud.app.db;

import codesqaud.app.db.exception.ConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DataSourceUtils {
    private static final Logger log = LoggerFactory.getLogger(DataSourceUtils.class);

    /**
     * Transcation이 진행중인 Connection이 있다면 해당 Connection을 반환합니다.
     * 그렇지 않다면 DataSource 에서 새로운 Connection을 받아와서 반환합니다.
     *
     * @param dataSource
     * @return
     */
    public static Connection getConnection(DataSource dataSource) {
        try {
            return doGetConnection(dataSource);
        } catch (SQLException e) {
            throw new ConnectionException("커넥션을 가져오는데 실패했습니다.");
        }
    }

    private static Connection doGetConnection(DataSource dataSource) throws SQLException {
        Connection connection = TransactionManager.getInProgressConnection();
        if(connection == null) {
            connection = dataSource.getConnection();
        } else {
            log.info("트랜잭션이 진행중인 커넥션을 가져왔습니다.");
        }
        return connection;
    }

    /**
     * 반환하려는 Connection이 Transaction 진행 중이라면 아무런 작업을 하지 않습니다.
     * Transcation 이 진행중이지 않다면 Connection을 close 합니다.
     *
     * @param connection
     */
    public static void releaseConnection(Connection connection) {
        try {
            doReleaseConnection(connection);
        } catch (SQLException e) {
            throw new ConnectionException("커넥션을 반환하는데 실패했습니다.");
        }
    }

    private static void doReleaseConnection(Connection connection) throws SQLException {
        Connection inProgressConnection = TransactionManager.getInProgressConnection();
        if(!connection.equals(inProgressConnection)) {
            connection.close();
        } else {
            log.info("트랜잭션이 진행중이기 때문에 커넥션을 반환하지 않았습니다.");
        }
    }
}
