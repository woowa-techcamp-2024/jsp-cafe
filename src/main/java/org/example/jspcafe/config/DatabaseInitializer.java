package org.example.jspcafe.config;


import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.sql.SQLException;

import static org.example.jspcafe.database.JdbcTemplate.initializeDatabase;
import static org.example.jspcafe.database.SimpleConnectionPool.shutdown;

@WebListener
public class DatabaseInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            shutdown();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        // 필요한 경우 정리 작업 수행
    }
}