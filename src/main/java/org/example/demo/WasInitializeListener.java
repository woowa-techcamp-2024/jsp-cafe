package org.example.demo;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.demo.db.DbConfig;
import org.example.demo.repository.PostRepository;
import org.example.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class WasInitializeListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(WasInitializeListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // JdbcUrl 에 데이터베이스 명시 필요
        DbConfig dbConfig = new DbConfig("jdbc:mysql://localhost/cafe", "root", "");
        UserRepository.init(dbConfig);
        PostRepository.init(dbConfig, UserRepository.getInstance());

        logger.info("WasInitializeListener.contextInitialized finish!");
    }
}
