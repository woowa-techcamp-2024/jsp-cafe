package org.example.jspcafe.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.annotation.WebListener;
import org.example.jspcafe.question.repository.JdbcQuestionRepository;
import org.example.jspcafe.question.repository.MemoryQuestionRepository;
import org.example.jspcafe.question.repository.QuestionRepository;
import org.example.jspcafe.question.service.QuestionService;
import org.example.jspcafe.user.repository.JdbcUserRepository;
import org.example.jspcafe.user.repository.MemoryUserRepository;
import org.example.jspcafe.user.repository.UserRepository;
import org.example.jspcafe.user.service.UserService;

@WebListener
public class ServletContextListener implements jakarta.servlet.ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        UserRepository userRepository = new JdbcUserRepository();
        UserService userService = new UserService(userRepository);

        QuestionRepository questionRepository = new JdbcQuestionRepository();
        QuestionService questionService = new QuestionService(questionRepository);

        context.setAttribute("UserRepository", userRepository);
        context.setAttribute("UserService",userService);

        context.setAttribute("QuestionRepository",questionRepository);
        context.setAttribute("QuestionService",questionService);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

        // 애플리케이션 종료 시 필요한 작업 수행 (예: 리소스 정리)
    }
}
