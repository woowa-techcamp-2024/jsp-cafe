package org.example.jspcafe;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnectionManager {
    Connection getConnection() throws SQLException;

}
