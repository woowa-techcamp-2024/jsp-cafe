package codesquad.javacafe.common.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection {
    private static final Logger log = LoggerFactory.getLogger(MySqlConnection.class);
    private static final String URL = "jdbc:mysql://localhost:3306/mydatabase";
    private static final String USERNAME = "seungh1024";
    private static final String PASSWORD = "1234";

    private MySqlConnection(){}

    public static Connection getConnection() {
        try {
            var connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            log.info("[Connection]  connection info = {}, class = {}", connection, connection.getClass());
            return connection;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

}
