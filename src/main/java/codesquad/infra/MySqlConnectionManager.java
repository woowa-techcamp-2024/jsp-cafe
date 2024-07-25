package codesquad.infra;

import com.mysql.cj.jdbc.MysqlDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public final class MySqlConnectionManager {
    private static MysqlDataSource dataSource;

    static {
        dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("root");
        dataSource.setPassword("");
        dataSource.setDatabaseName("jsp_cafe");
    }

    private MySqlConnectionManager() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
