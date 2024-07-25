package com.woowa.hyeonsik.server.config;

import com.woowa.hyeonsik.application.dao.InMemoryArticleDao;
import com.woowa.hyeonsik.application.dao.InMemoryUserDao;
import com.woowa.hyeonsik.application.service.ArticleService;
import com.woowa.hyeonsik.application.service.UserService;
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

        InMemoryUserDao userDao = new InMemoryUserDao();
        UserService userService = new UserService(userDao);
        InMemoryArticleDao articleDao = new InMemoryArticleDao();
        ArticleService articleService = new ArticleService(articleDao);

        sce.getServletContext().setAttribute("userDao", userDao);
        sce.getServletContext().setAttribute("userService", userService);
        sce.getServletContext().setAttribute("articleDao", articleDao);
        sce.getServletContext().setAttribute("articleService", articleService);
    }
}
