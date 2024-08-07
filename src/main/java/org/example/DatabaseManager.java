package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.example.util.LoggerUtil;
import org.slf4j.Logger;

public class DatabaseManager {
    private static final Logger logger = LoggerUtil.getLogger();
    private static final String DB_URL = "jdbc:mysql://localhost:3306/test";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";


    public static Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            logger.error("DB 드라이버 로딩 실패", e);
        } catch (SQLException e) {
            logger.error("DB 접속 실패", e);
        }
        return conn;
    }

    public static void executeSqlScript(String script) {
        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement()) {

            String[] sqlCommands = script.split(";");

            for (String sqlCommand : sqlCommands) {
                if (!sqlCommand.trim().isEmpty()) {
                    stmt.execute(sqlCommand.trim());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}