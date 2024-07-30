package org.example.demo.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.example.demo.exception.InternalServerError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DbConfig {
    private static final Logger logger = LoggerFactory.getLogger(DbConfig.class);
    private DataSource dataSource;

    public DbConfig(String jdbcUrl, String user, String password) {
        try {
            // JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new InternalServerError("Failed to load JDBC driver " + e.getMessage());
        }

        // 초기 데이터 소스 설정 (cafe 데이터베이스 없이)
        HikariConfig initialConfig = new HikariConfig();
        initialConfig.setJdbcUrl(removeDatabaseName(jdbcUrl));
        initialConfig.setUsername(user);
        initialConfig.setPassword(password);

        this.dataSource = new HikariDataSource(initialConfig);

        createDatabaseIfNotExists();
        initializeSchema();

        // 데이터 소스 설정 (cafe 데이터베이스 사용)
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(user);
        config.setPassword(password);

        // Tomcat 의 maxThread 개수 만큼 Pool Size 설정
        config.setMaximumPoolSize(200);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        this.dataSource = new HikariDataSource(config);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    private static String removeDatabaseName(String jdbcUrl) {
        int lastSlashIndex = jdbcUrl.lastIndexOf('/');
        if (lastSlashIndex != -1) {
            return jdbcUrl.substring(0, lastSlashIndex);
        }
        return jdbcUrl;
    }

    private void initializeSchema() {
        logger.info("initializing database...");
        try {
            // 데이터베이스 연결 (기본 연결)
            try (Connection conn = dataSource.getConnection();
                 Statement stmt = conn.createStatement()) {

                // 스키마 파일 읽기
                String schema = readSchemaFile();

                // 데이터베이스 사용 및 테이블 생성
                stmt.execute("USE cafe;");
                for (String sql : schema.split(";")) {
                    if (!sql.trim().isEmpty()) {
                        stmt.execute(sql);
                    }
                }

                logger.info("Database initialized successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerError("Failed to initialize database: " + e.getMessage());
        }
    }

    private void createDatabaseIfNotExists() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE DATABASE IF NOT EXISTS cafe");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerError("Failed to create database: " + e.getMessage());
        }
    }

    private String readSchemaFile() {
        try (InputStream inputStream = getClass().getResourceAsStream("/schema.sql");
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new InternalServerError("Failed to read schema.sql file" + e.getMessage());
        }
    }
}
