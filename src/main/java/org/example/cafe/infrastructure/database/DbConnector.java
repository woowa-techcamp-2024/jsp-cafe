package org.example.cafe.infrastructure.database;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.example.cafe.common.error.CafeException;
import org.slf4j.Logger;

public class DbConnector {

    private static final Logger log = getLogger(DbConnector.class);
    private static final String HEALTH_CHECK = "SELECT 1";
    private HikariConfig config;
    private DataSource dataSource;

    public DbConnector init() {
        this.config = new HikariConfig("/setting.properties");
        this.dataSource = new HikariDataSource(config);

        return healthCheck();
    }

    private DbConnector healthCheck() {
        try (Statement statement = getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(HEALTH_CHECK)) {
            if (resultSet.next() && resultSet.getInt(1) == 1) {
                log.info(config.getJdbcUrl() + " connected.");
                return this;
            }
            return null;
        } catch (SQLException e) {
            throw new CafeException("Cannot execute health check query", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }
}
