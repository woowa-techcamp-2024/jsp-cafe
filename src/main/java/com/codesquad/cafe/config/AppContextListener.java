package com.codesquad.cafe.config;

import com.codesquad.cafe.db.InMemoryPostRepository;
import com.codesquad.cafe.db.InMemoryUserRepository;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        InMemoryUserRepository userRepository = new InMemoryUserRepository();
        InMemoryPostRepository postRepository = new InMemoryPostRepository();
        sce.getServletContext().setAttribute("userRepository", userRepository);
        sce.getServletContext().setAttribute("postRepository", postRepository);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }
}
