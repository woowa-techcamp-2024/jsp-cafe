package com.woowa.hyeonsik.server.config;

import com.woowa.hyeonsik.application.dao.*;
import com.woowa.hyeonsik.application.service.ArticleService;
import com.woowa.hyeonsik.application.service.UserService;
import com.woowa.hyeonsik.application.servlet.LoginServlet;
import com.woowa.hyeonsik.application.servlet.LogoutServlet;
import com.woowa.hyeonsik.application.servlet.QnaPathServlet;
import com.woowa.hyeonsik.application.servlet.QnaServlet;
import com.woowa.hyeonsik.application.servlet.UserPathServlet;
import com.woowa.hyeonsik.application.servlet.UserServlet;
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

        sce.getServletContext().addServlet("qnaPathServlet", new QnaPathServlet(articleService))
                .addMapping("/questions/*");
        sce.getServletContext().addServlet("qnaServlet", new QnaServlet(articleService))
                .addMapping("/questions", "");
        sce.getServletContext().addServlet("userPathServlet", new UserPathServlet(userService))
                .addMapping("/users/*");
        sce.getServletContext().addServlet("userServlet", new UserServlet(userService))
                .addMapping("/users");
        sce.getServletContext().addServlet("loginServlet", new LoginServlet(userService))
            .addMapping("/auth/login");
        sce.getServletContext().addServlet("logoutServlet", new LogoutServlet())
            .addMapping("/auth/logout");
    }
}
