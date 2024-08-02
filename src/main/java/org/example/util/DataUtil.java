package org.example.util;

import java.sql.Connection;
import java.sql.SQLException;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Component;

@Component
public class DataUtil {

    private final DatabaseConnectionPool connectionPool;

    @Autowired
    public DataUtil(DatabaseConnectionPool connectionPool) throws SQLException {
        this.connectionPool = connectionPool;
    }

    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

}
