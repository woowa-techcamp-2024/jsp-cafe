package org.example.jspcafe.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public class JdbcTemplate {
    private static String URL;
    private static String MYSQL_USERNAME;
    private static String MYSQL_PASSWORD;

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, MYSQL_USERNAME, MYSQL_PASSWORD);
    }

    public static void initializeDatabase() throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");

        URL = System.getenv("MYSQL_URL");
        MYSQL_USERNAME = System.getenv("MYSQL_USERNAME");
        MYSQL_PASSWORD = System.getenv("MYSQL_PASSWORD");

        try (Connection conn = DriverManager.getConnection(URL, MYSQL_USERNAME, MYSQL_PASSWORD);
             Statement stmt = conn.createStatement()) {

            String sql = readSqlFile("/init.sql");
            stmt.execute(sql);

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static String readSqlFile(String resourcePath) throws IOException {
        try (InputStream is = JdbcTemplate.class.getResourceAsStream(resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
