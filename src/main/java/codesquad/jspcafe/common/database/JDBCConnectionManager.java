package codesquad.jspcafe.common.database;

import codesquad.jspcafe.common.ApplicationProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;
import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCConnectionManager {

    private static final Logger log = LoggerFactory.getLogger(JDBCConnectionManager.class);
    
    private static final int DEFAULT_MAX_CONNECTIONS = 10;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        }
    }

    private final HikariPool connectionPool;

    public JDBCConnectionManager(ApplicationProperties properties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(properties.getJdbcUrl());
        config.setUsername(properties.getJdbcUsername());
        config.setPassword(properties.getJdbcPassword());
        config.setMaximumPoolSize(DEFAULT_MAX_CONNECTIONS);
        connectionPool = new HikariPool(config);
    }

    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

}
