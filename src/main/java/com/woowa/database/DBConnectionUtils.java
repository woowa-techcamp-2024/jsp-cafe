package com.woowa.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnectionUtils {

    private static final String JDBC_URL = "jdbc:mysql://host.docker.internal:3306/cafe";
    private static final String USER = "foxrain";
    private static final String PASSWORD = "test1234";
    private static final Logger log = LoggerFactory.getLogger(DBConnectionUtils.class);

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new IllegalArgumentException("커넥션 획득에 실패했습니다.", e);
        }
    }

    public static void closeConnection(Connection con, Statement stmt, ResultSet rs) {
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.error("ResultSet close 에러", e);
            }
        }

        if(stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.error("Statement close 에러", e);
            }
        }

        if(con != null) {
            try {
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e) {
                log.error("Connection close 에러", e);
            }
        }
    }

    public static void rollback(Connection con) {
        try {
            con.rollback();
        } catch (SQLException e) {
            throw new IllegalArgumentException("롤백 에러", e);
        }
    }

    public static void startTransaction(Connection con) {
        try {
            con.setAutoCommit(false);
        } catch (SQLException e) {
            throw new IllegalArgumentException("트랜잭션 에러", e);
        }
    }

    public static void commit(Connection con) {
        try {
            con.commit();
        } catch (SQLException e) {
            throw new IllegalArgumentException("커밋 에러", e);
        }
    }
}
