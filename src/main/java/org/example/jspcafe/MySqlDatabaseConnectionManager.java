package org.example.jspcafe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class MySqlDatabaseConnectionManager implements DatabaseConnectionManager {

    private static final String URL = "jdbc:mysql://localhost:3306/jsp_cafe";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("JDBC 드라이버를 찾을 수 없습니다.");
        }
    }
    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
