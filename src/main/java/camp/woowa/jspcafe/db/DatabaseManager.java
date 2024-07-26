package camp.woowa.jspcafe.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private static final String DB_URL = "jdbc:mysql://localhost:3306/jspcafe";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    private DatabaseManager() {
    }

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
}
