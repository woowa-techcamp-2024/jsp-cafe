package cafe.database;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class RealJdbcConnectionPool implements ConnectionPool {
    private final MysqlConnectionPoolDataSource dataSource;

    public RealJdbcConnectionPool(MysqlConnectionPoolDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return dataSource.getPooledConnection().getConnection();
    }

    @Override
    public void releaseConnection(PooledConnection connection) {

    }
}
