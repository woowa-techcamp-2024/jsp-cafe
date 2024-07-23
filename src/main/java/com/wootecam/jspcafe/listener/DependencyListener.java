package com.wootecam.jspcafe.listener;

import com.wootecam.jspcafe.repository.UserRepository;
import com.wootecam.jspcafe.service.UserService;
import com.wootecam.jspcafe.servlet.SignupFormServlet;
import com.wootecam.jspcafe.servlet.UserServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener("dependencyListener")
public class DependencyListener implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);

        servletContext.addServlet("signupFormServlet", new SignupFormServlet())
                .addMapping("/users/signup");
        servletContext.addServlet("userServlet", new UserServlet(userService))
                .addMapping("/users");
    }
}
