package com.jspcafe.test_util;

import com.jspcafe.util.DatabaseConnector;
import org.h2.tools.RunScript;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;

public class H2Initializer {
    private static final String INIT_SCRIPT_PATH = "src/test/resources/init.sql";

    public static void initializeDatabase(DatabaseConnector dataBaseConnector) throws SQLException {
        try(Connection connection = dataBaseConnector.getConnection()) {
            RunScript.execute(connection, new FileReader(INIT_SCRIPT_PATH));
        } catch (Exception e) {
            throw new SQLException("Failed to initialize H2 database", e);
        }
    }
}
