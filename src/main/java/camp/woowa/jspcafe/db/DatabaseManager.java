package camp.woowa.jspcafe.db;

import camp.woowa.jspcafe.utils.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);

    private DatabaseManager() {
    }

    public static Connection getConnection(){
        Connection conn = null;
        Properties props = ConfigLoader.loadConfig();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(props.getProperty("DB_URL"), props.getProperty("DB_USER"), props.getProperty("DB_PASSWORD"));
        } catch (ClassNotFoundException e) {
            logger.error("DB 드라이버 로딩 실패", e);
        } catch (SQLException e) {
            logger.error("DB 접속 실패", e);
        }

        return conn;
    }
}
