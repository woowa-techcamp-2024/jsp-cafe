package camp.woowa.jspcafe;

import camp.woowa.jspcafe.repository.InMemQuestionRepository;
import camp.woowa.jspcafe.repository.InMemUserRepository;
import camp.woowa.jspcafe.repository.QuestionRepository;
import camp.woowa.jspcafe.repository.UserRepository;
import camp.woowa.jspcafe.services.QuestionService;
import camp.woowa.jspcafe.services.UserService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class DIContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();

        // UserRepository를 생성하고 UserService에 주입
        UserRepository userRepository = new InMemUserRepository();
        UserService userService = new UserService(userRepository);
        sc.setAttribute("userService", userService);

        // QuestionRepository를 생성하고 QuestionService에 주입
        QuestionRepository questionRepository = new InMemQuestionRepository();
        QuestionService questionService = new QuestionService(questionRepository);
        sc.setAttribute("questionService", questionService);
    }
}
