package org.example.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;

@Component
public class DatabaseConnectionPool implements AutoCloseable {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/cafe";
    private static final String DB_USER = "cafeuser";
    private static final String DB_PASSWORD = "cafeuser_password";
    private static final int MAX_POOL_SIZE = 100;

    private final HikariDataSource dataSource;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("MySQL JDBC Driver not found. " + e.getMessage());
        }
    }

    @Autowired
    public DatabaseConnectionPool() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(JDBC_URL);
        hikariConfig.setUsername(DB_USER);
        hikariConfig.setPassword(DB_PASSWORD);
        hikariConfig.setMaximumPoolSize(MAX_POOL_SIZE);
        hikariConfig.setLeakDetectionThreshold(60000); // 60 seconds
        hikariConfig.setConnectionTimeout(30000); // 30 seconds
        this.dataSource = new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}