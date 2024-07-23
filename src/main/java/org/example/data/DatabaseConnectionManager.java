package org.example.data;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    private static String url = "jdbc:mysql://localhost:3306/mydb";
    private static String user = "user";
    private static String password = "userpassword";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
