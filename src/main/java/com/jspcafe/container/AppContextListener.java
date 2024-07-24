package com.jspcafe.container;

import com.jspcafe.user.model.UserDao;
import com.jspcafe.user.service.UserService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        initUserDao(ctx);
        initUserService(ctx);
    }

    private void initUserDao(ServletContext ctx) {
        ctx.setAttribute("userDao", new UserDao());
    }

    private void initUserService(ServletContext ctx) {
        ctx.setAttribute("userService", new UserService((UserDao) ctx.getAttribute("userDao")));
    }
}
