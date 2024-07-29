package cafe.domain;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {
    private static String PORT = "3306";
    private static final String DB_URL = "jdbc:mysql://mysql:" + PORT + "/cafe";
    private static final String DB_USER = "cafe";
    private static final String DB_PASSWORD = "cafe";
    private static MysqlConnectionPoolDataSource dataSource;

    static {
        try {
            dataSource = new MysqlConnectionPoolDataSource();
            dataSource.setURL(DB_URL);
            dataSource.setUser(DB_USER);
            dataSource.setPassword(DB_PASSWORD);

            dataSource.setMaxReconnects(5);
            dataSource.setInitialTimeout(2);
            dataSource.setAutoReconnect(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection connect() throws SQLException {
        return dataSource.getConnection();
    }

    public static void setDB_URL(String DB_URL) {
        DatabaseManager.PORT = DB_URL;
    }
}
