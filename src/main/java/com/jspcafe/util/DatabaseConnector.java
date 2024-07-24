package com.jspcafe.util;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnector {
    Connection getConnection() throws SQLException;
    void closeConnection(Connection conn);
}
