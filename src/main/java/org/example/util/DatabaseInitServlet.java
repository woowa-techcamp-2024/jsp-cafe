package org.example.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(urlPatterns = "/!@!(#()@*#()!@#!@", loadOnStartup = 1)
public class DatabaseInitServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInitServlet.class);
    private DataUtil dataUtil;

    @Override
    public void init() throws ServletException {
        dataUtil = new DataUtil();
        try (Connection conn = dataUtil.getConnection();
             Statement stmt = conn.createStatement()) {

            logger.info("테이블 초기화");
            stmt.execute(createUserTable());
            stmt.execute(createPostTable());
            stmt.execute(createReplyTable());

        } catch (SQLException e) {
            throw new ServletException("Unable to initialize database", e);
        }
    }

    private String createUserTable() {
        return "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "userId VARCHAR(50) NOT NULL UNIQUE," +
                "name VARCHAR(50) NOT NULL UNIQUE," +
                "email VARCHAR(100) NOT NULL UNIQUE," +
                "password VARCHAR(255) NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
    }

    private String createPostTable() {
        return "CREATE TABLE IF NOT EXISTS posts (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "writer VARCHAR(50) NOT NULL," +
                "title VARCHAR(50) NOT NULL," +
                "contents TEXT NOT NULL," +
                "status VARCHAR(50) NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
    }

    private String createReplyTable() {
        return "CREATE TABLE IF NOT EXISTS replies (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                "post_id BIGINT," +
                "writer VARCHAR(50) NOT NULL," +
                "contents TEXT NOT NULL," +
                "status VARCHAR(50) NOT NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
    }
}