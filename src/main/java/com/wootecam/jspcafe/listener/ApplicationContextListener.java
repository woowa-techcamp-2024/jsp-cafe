package com.wootecam.jspcafe.listener;

import com.wootecam.jspcafe.domain.QuestionRepository;
import com.wootecam.jspcafe.domain.UserRepository;
import com.wootecam.jspcafe.repository.InMemoryQuestionRepository;
import com.wootecam.jspcafe.repository.InMemoryUserRepository;
import com.wootecam.jspcafe.service.QuestionService;
import com.wootecam.jspcafe.service.UserService;
import com.wootecam.jspcafe.servlet.HomeServlet;
import com.wootecam.jspcafe.servlet.question.QuestionDetailServlet;
import com.wootecam.jspcafe.servlet.question.QuestionServlet;
import com.wootecam.jspcafe.servlet.user.SignupFormServlet;
import com.wootecam.jspcafe.servlet.user.UserEditServlet;
import com.wootecam.jspcafe.servlet.user.UserProfileServlet;
import com.wootecam.jspcafe.servlet.user.UserServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener("applicationContextListener")
public class ApplicationContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        UserRepository userRepository = new InMemoryUserRepository();
        QuestionRepository questionRepository = new InMemoryQuestionRepository();

        UserService userService = new UserService(userRepository);
        QuestionService questionService = new QuestionService(questionRepository);

        servletContext.addServlet("homeServlet", new HomeServlet(questionService))
                .addMapping("/");
        servletContext.addServlet("signupFormServlet", new SignupFormServlet())
                .addMapping("/users/signup");
        servletContext.addServlet("userServlet", new UserServlet(userService))
                .addMapping("/users");
        servletContext.addServlet("userProfileServlet", new UserProfileServlet(userService))
                .addMapping("/users/*");
        servletContext.addServlet("userEditServlet", new UserEditServlet(userService))
                .addMapping("/users/edit/*");

        servletContext.addServlet("questionServlet", new QuestionServlet(questionService))
                .addMapping("/questions");
        servletContext.addServlet("questionDetailServlet", new QuestionDetailServlet(questionService))
                .addMapping("/questions/*");
    }
}
