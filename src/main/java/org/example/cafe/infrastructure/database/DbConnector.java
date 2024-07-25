package org.example.cafe.infrastructure.database;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.example.cafe.common.error.CafeException;
import org.slf4j.Logger;

public class DbConnector {

    private static final Logger log = getLogger(DbConnector.class);

    private static final String DATASOURCE_URL = "jdbc:mysql://localhost:3306/cafe";
    private static final String DATASOURCE_USER = "test";
    private static final String DATASOURCE_PASSWORD = "qlalfqjsgh";
    private static final String HEALTH_CHECK = "SELECT 1";

    public DbConnector init() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new CafeException("Cannot load MySQL JDBC Driver", e);
        }

        return healthCheck();
    }

    private DbConnector healthCheck() {
        try (Statement statement = getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(HEALTH_CHECK)) {
            if (resultSet.next() && resultSet.getInt(1) == 1) {
                log.info(DATASOURCE_URL + " connected.");
                return this;
            }
            return null;
        } catch (SQLException e) {
            throw new CafeException("Cannot execute health check query", e);
        }
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATASOURCE_URL, DATASOURCE_USER, DATASOURCE_PASSWORD);
    }
}
