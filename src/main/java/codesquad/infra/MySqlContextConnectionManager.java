package codesquad.infra;

import codesquad.common.db.connection.ConnectionManager;
import codesquad.exception.ExternalConnectionException;
import com.mysql.cj.jdbc.MysqlDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class MySqlContextConnectionManager implements ConnectionManager {
    private MysqlDataSource dataSource;

    public MySqlContextConnectionManager() {
        Properties properties = new Properties();
        try (InputStream input = MySqlContextConnectionManager.class.getResourceAsStream("/application.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }

        dataSource = new MysqlDataSource();
        dataSource.setServerName(properties.getProperty("mysql.servername"));
        dataSource.setUser(properties.getProperty("mysql.username"));
        dataSource.setPassword(properties.getProperty("mysql.password"));
        dataSource.setDatabaseName(properties.getProperty("mysql.dbname"));

        // Try to use environment variables if set, otherwise fall back to properties file
        String serverName = System.getenv("MYSQL_SERVERNAME");
        if (serverName == null || serverName.isEmpty()) {
            serverName = properties.getProperty("mysql.servername");
        }
        dataSource.setServerName(serverName);

        String username = System.getenv("MYSQL_USERNAME");
        if (username == null || username.isEmpty()) {
            username = properties.getProperty("mysql.username");
        }
        dataSource.setUser(username);

        String password = System.getenv("MYSQL_PASSWORD");
        if (password == null || password.isEmpty()) {
            password = properties.getProperty("mysql.password");
        }
        dataSource.setPassword(password);

        String databaseName = System.getenv("MYSQL_DBNAME");
        if (databaseName == null || databaseName.isEmpty()) {
            databaseName = properties.getProperty("mysql.dbname");
        }
        dataSource.setDatabaseName(databaseName);
    }

    public Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new ExternalConnectionException(e);
        }
    }
}
