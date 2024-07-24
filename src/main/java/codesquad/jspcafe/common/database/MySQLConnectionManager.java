package codesquad.jspcafe.common.database;

import codesquad.jspcafe.common.ApplicationProperties;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLConnectionManager {

    private static final Logger log = LoggerFactory.getLogger(MySQLConnectionManager.class);

    private final String url;
    private final String username;
    private final String password;

    public MySQLConnectionManager(ApplicationProperties properties) {
        url = properties.getJdbcUrl();
        username = properties.getJdbcUsername();
        password = properties.getJdbcPassword();
        init(properties.getJdbcSqlSchema());
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

    private void init(String schemaPath) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
            InputStream inputStream = getClass().getResourceAsStream(schemaPath);
            Statement statement = connection.createStatement()) {
            String schema = new String(Objects.requireNonNull(inputStream).readAllBytes());
            statement.execute(schema);
        } catch (SQLException | IOException e) {
            log.error(e.getMessage());
        }
    }
}
