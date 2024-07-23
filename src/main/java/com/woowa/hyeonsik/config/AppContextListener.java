package com.woowa.hyeonsik.config;

import com.woowa.hyeonsik.dao.UserDao;
import com.woowa.hyeonsik.service.UserService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class AppContextListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.debug("Context 초기화를 진행합니다.");

        UserDao userDao = new UserDao();
        UserService userService = new UserService(userDao);

        sce.getServletContext().setAttribute("userDao", userDao);
        sce.getServletContext().setAttribute("userService", userService);
    }
}
