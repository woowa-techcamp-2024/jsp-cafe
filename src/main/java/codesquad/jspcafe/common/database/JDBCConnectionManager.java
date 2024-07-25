package codesquad.jspcafe.common.database;

import codesquad.jspcafe.common.ApplicationProperties;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCConnectionManager {

    private static final Logger log = LoggerFactory.getLogger(JDBCConnectionManager.class);

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        }
    }

    private final MysqlConnectionPoolDataSource connectionPoolDataSource;

    public JDBCConnectionManager(ApplicationProperties properties) {
        this.connectionPoolDataSource = new MysqlConnectionPoolDataSource();
        connectionPoolDataSource.setUrl(properties.getJdbcUrl());
        connectionPoolDataSource.setUser(properties.getJdbcUsername());
        connectionPoolDataSource.setPassword(properties.getJdbcPassword());
    }

    public Connection getConnection() throws SQLException {
        return connectionPoolDataSource.getPooledConnection().getConnection();
    }

}
