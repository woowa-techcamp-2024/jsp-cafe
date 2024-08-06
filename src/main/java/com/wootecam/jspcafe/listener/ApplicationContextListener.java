package com.wootecam.jspcafe.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.wootecam.jspcafe.config.DataSourceManager;
import com.wootecam.jspcafe.config.DataSourceProperty;
import com.wootecam.jspcafe.config.JdbcTemplate;
import com.wootecam.jspcafe.domain.QuestionRepository;
import com.wootecam.jspcafe.domain.ReplyRepository;
import com.wootecam.jspcafe.domain.UserRepository;
import com.wootecam.jspcafe.repository.JdbcQuestionRepository;
import com.wootecam.jspcafe.repository.JdbcReplyRepository;
import com.wootecam.jspcafe.repository.JdbcUserRepository;
import com.wootecam.jspcafe.service.QuestionService;
import com.wootecam.jspcafe.service.ReplyService;
import com.wootecam.jspcafe.service.UserService;
import com.wootecam.jspcafe.servlet.HomeServlet;
import com.wootecam.jspcafe.servlet.question.QuestionDeleteServlet;
import com.wootecam.jspcafe.servlet.question.QuestionDetailServlet;
import com.wootecam.jspcafe.servlet.question.QuestionEditServlet;
import com.wootecam.jspcafe.servlet.question.QuestionServlet;
import com.wootecam.jspcafe.servlet.reply.ReplyDeleteServlet;
import com.wootecam.jspcafe.servlet.reply.ReplyServlet;
import com.wootecam.jspcafe.servlet.user.SignInServlet;
import com.wootecam.jspcafe.servlet.user.SignOutServlet;
import com.wootecam.jspcafe.servlet.user.SignupServlet;
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
        DataSourceProperty dataSourceProperty = new DataSourceProperty("PROFILE");
        DataSourceManager dataSourceManager = new DataSourceManager(dataSourceProperty);
        servletContext.setAttribute("dataSourceManager", dataSourceManager);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourceManager);

        UserRepository userRepository = new JdbcUserRepository(jdbcTemplate);
        QuestionRepository questionRepository = new JdbcQuestionRepository(jdbcTemplate);
        ReplyRepository replyRepository = new JdbcReplyRepository(jdbcTemplate);

        UserService userService = new UserService(userRepository);
        QuestionService questionService = new QuestionService(questionRepository, replyRepository);
        ReplyService replyService = new ReplyService(replyRepository);

        servletContext.addServlet("homeServlet", new HomeServlet(questionService))
                .addMapping("/");
        servletContext.addServlet("signupServlet", new SignupServlet())
                .addMapping("/users/signup");
        servletContext.addServlet("userServlet", new UserServlet(userService))
                .addMapping("/users");
        servletContext.addServlet("userProfileServlet", new UserProfileServlet(userService))
                .addMapping("/users/*");
        servletContext.addServlet("userEditServlet", new UserEditServlet(userService))
                .addMapping("/users/edit/*");
        servletContext.addServlet("signInServlet", new SignInServlet(userService))
                .addMapping("/users/sign-in");
        servletContext.addServlet("signOutServlet", new SignOutServlet())
                .addMapping("/users/sign-out");

        servletContext.addServlet("questionServlet", new QuestionServlet(questionService))
                .addMapping("/questions");
        servletContext.addServlet("questionDetailServlet", new QuestionDetailServlet(questionService, replyService))
                .addMapping("/questions/*");
        servletContext.addServlet("questionEditServlet", new QuestionEditServlet(questionService))
                .addMapping("/questions/edit/*");
        servletContext.addServlet("questionDeleteServlet", new QuestionDeleteServlet(questionService))
                .addMapping("/questions/delete/*");

        servletContext.addServlet("replyServlet", new ReplyServlet(replyService, objectMapper))
                .addMapping("/replies");
        servletContext.addServlet("replyDeleteServlet", new ReplyDeleteServlet(replyService))
                .addMapping("/replies/delete/*");
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        DataSourceManager dataSourceManager = (DataSourceManager) sce.getServletContext()
                .getAttribute("dataSourceManager");

        dataSourceManager.shutdown();
    }
}
