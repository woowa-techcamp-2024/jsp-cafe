package com.woowa.hyeonsik.server.config;

import com.woowa.hyeonsik.application.dao.*;
import com.woowa.hyeonsik.application.service.ArticleService;
import com.woowa.hyeonsik.application.service.UserService;
import com.woowa.hyeonsik.server.database.DatabaseConnector;
import com.woowa.hyeonsik.server.database.property.MysqlProperty;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class ApplicationContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.debug("Context 초기화를 진행합니다.");

        DatabaseConnector connector = new DatabaseConnector(new MysqlProperty());
        UserDao userDao = new JdbcUserDao(connector);
        UserService userService = new UserService(userDao);
        ArticleDao articleDao = new JdbcArticleDao(connector);
        ArticleService articleService = new ArticleService(articleDao);

        sce.getServletContext().setAttribute("userDao", userDao);
        sce.getServletContext().setAttribute("userService", userService);
        sce.getServletContext().setAttribute("articleDao", articleDao);
        sce.getServletContext().setAttribute("articleService", articleService);
    }
}
