package com.wootecam.jspcafe.listener;

import com.wootecam.jspcafe.config.DataSourceManager;
import com.wootecam.jspcafe.config.JdbcTemplate;
import com.wootecam.jspcafe.domain.QuestionRepository;
import com.wootecam.jspcafe.domain.UserRepository;
import com.wootecam.jspcafe.repository.JdbcQuestionRepository;
import com.wootecam.jspcafe.repository.JdbcUserRepository;
import com.wootecam.jspcafe.service.QuestionService;
import com.wootecam.jspcafe.service.UserService;
import com.wootecam.jspcafe.servlet.HomeServlet;
import com.wootecam.jspcafe.servlet.question.QuestionDetailHttpServlet;
import com.wootecam.jspcafe.servlet.question.QuestionEditHttpServlet;
import com.wootecam.jspcafe.servlet.question.QuestionHttpServlet;
import com.wootecam.jspcafe.servlet.user.SignInFormHttpServlet;
import com.wootecam.jspcafe.servlet.user.SignOutHttpServlet;
import com.wootecam.jspcafe.servlet.user.SignupFormHttpServlet;
import com.wootecam.jspcafe.servlet.user.UserEditHttpServlet;
import com.wootecam.jspcafe.servlet.user.UserHttpServlet;
import com.wootecam.jspcafe.servlet.user.UserProfileHttpServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener("applicationContextListener")
public class ApplicationContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        DataSourceManager dataSourceManager = new DataSourceManager();
        servletContext.setAttribute("dataSourceManager", dataSourceManager);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceManager);

        UserRepository userRepository = new JdbcUserRepository(jdbcTemplate);
        QuestionRepository questionRepository = new JdbcQuestionRepository(jdbcTemplate);

        UserService userService = new UserService(userRepository);
        QuestionService questionService = new QuestionService(questionRepository);

        servletContext.addServlet("homeServlet", new HomeServlet(questionService))
                .addMapping("/");
        servletContext.addServlet("signupFormServlet", new SignupFormHttpServlet())
                .addMapping("/users/signup");
        servletContext.addServlet("userServlet", new UserHttpServlet(userService))
                .addMapping("/users");
        servletContext.addServlet("userProfileServlet", new UserProfileHttpServlet(userService))
                .addMapping("/users/*");
        servletContext.addServlet("userEditServlet", new UserEditHttpServlet(userService))
                .addMapping("/users/edit/*");
        servletContext.addServlet("signInFormServlet", new SignInFormHttpServlet(userService))
                .addMapping("/users/sign-in");
        servletContext.addServlet("signOutServlet", new SignOutHttpServlet())
                .addMapping("/users/sign-out");

        servletContext.addServlet("questionServlet", new QuestionHttpServlet(questionService))
                .addMapping("/questions");
        servletContext.addServlet("questionDetailServlet", new QuestionDetailHttpServlet(questionService))
                .addMapping("/questions/*");
        servletContext.addServlet("questionEditServlet", new QuestionEditHttpServlet(questionService))
                .addMapping("/questions/edit/*");
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        DataSourceManager dataSourceManager = (DataSourceManager) sce.getServletContext()
                .getAttribute("dataSourceManager");

        dataSourceManager.shutdown();
    }
}
