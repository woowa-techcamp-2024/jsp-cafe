package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtils {

    // proxy pattern 사용도 고려해보기
    private static final String jdbcURL = "jdbc:mysql://localhost:3306/servlet_cafe";
    private static final String dbUser = "root";
    private static final String dbPassword = "1q2w3e4r"; // 실제 비밀번호로 교체

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("MySQL JDBC Driver not found.");
        }
    }

    public static Connection getConnection() throws SQLException {
        // driver manager 대신에 connection pool을 사용하는 것이 좋다?
        return DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

}
