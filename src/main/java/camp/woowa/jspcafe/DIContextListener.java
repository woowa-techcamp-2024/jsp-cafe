package camp.woowa.jspcafe;

import camp.woowa.jspcafe.db.DatabaseManager;
import camp.woowa.jspcafe.repository.*;
import camp.woowa.jspcafe.services.QuestionService;
import camp.woowa.jspcafe.services.UserService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import java.sql.Connection;;

public class DIContextListener implements ServletContextListener {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DIContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext sc = sce.getServletContext();

        // DB Connection
        Connection conn = DatabaseManager.getConnection();

        if (conn == null) {
            logger.error("DB Connection Error");
            throw new RuntimeException("DB Connection Error");
        }

        // UserRepository를 생성하고 UserService에 주입
        UserRepository userRepository = new MySQLUserRepository(conn);
        UserService userService = new UserService(userRepository);
        sc.setAttribute("userService", userService);

        // QuestionRepository를 생성하고 QuestionService에 주입
        QuestionRepository questionRepository = new MySQLQuestionRepository(conn);
        QuestionService questionService = new QuestionService(questionRepository);
        sc.setAttribute("questionService", questionService);
    }
}
