package org.example.jspcafe.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.annotation.WebListener;
import org.example.jspcafe.question.repository.MemoryQuestionRepository;
import org.example.jspcafe.user.repository.MemoryUserRepository;
import org.example.jspcafe.user.service.UserService;

@WebListener
public class ServletContextListener implements jakarta.servlet.ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        MemoryUserRepository memoryUserRepository = new MemoryUserRepository();

        MemoryQuestionRepository questionRepository = new MemoryQuestionRepository();
        context.setAttribute("UserRepository", memoryUserRepository);
        context.setAttribute("UserService",new UserService(memoryUserRepository));
        context.setAttribute("QuestionRepository",questionRepository);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 애플리케이션 종료 시 필요한 작업 수행 (예: 리소스 정리)
    }
}
