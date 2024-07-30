package org.example.demo.db;

import org.example.demo.exception.InternalServerError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DbConfig {
    private static final Logger logger = LoggerFactory.getLogger(DbConfig.class);
    private String jdbcUrl;
    private String user;
    private String password;

    public DbConfig(String jdbcUrl, String user, String password) {
        this.jdbcUrl = jdbcUrl;
        this.user = user;
        this.password = password;
        initializeDatabase();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, user, password);
    }

    private void initializeDatabase() {
        logger.info("initializing database...");
        try {
            // JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");
            createDatabaseIfNotExists();

            // 데이터베이스 연결 (기본 연결)
            try (Connection conn = DriverManager.getConnection(jdbcUrl, user, password);
                 Statement stmt = conn.createStatement()) {

                // 스키마 파일 읽기
                String schema = readSchemaFile();

                // 데이터베이스 사용 및 테이블 생성
                stmt.execute("USE test;");
                for (String sql : schema.split(";")) {
                    if (!sql.trim().isEmpty()) {
                        stmt.execute(sql);
                    }
                }

                logger.info("Database initialized successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createDatabaseIfNotExists() {
        String urlWithoutDb = "jdbc:mysql://localhost";
        try (Connection conn = DriverManager.getConnection(urlWithoutDb, user, password);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE DATABASE IF NOT EXISTS test");
        } catch (SQLException e) {
            e.printStackTrace();
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
