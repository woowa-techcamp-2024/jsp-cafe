package codesquad.jspcafe.common.database;

import codesquad.jspcafe.common.ApplicationProperties;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBCConnectionManager {

    private static final Logger log = LoggerFactory.getLogger(JDBCConnectionManager.class);

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        }
    }

    private final String url;
    private final String username;
    private final String password;

    public JDBCConnectionManager(ApplicationProperties properties) {
        url = properties.getJdbcUrl();
        username = properties.getJdbcUsername();
        password = properties.getJdbcPassword();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

}
