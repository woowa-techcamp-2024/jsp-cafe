package camp.woowa.jspcafe;

import camp.woowa.jspcafe.core.ServiceLocator;
import camp.woowa.jspcafe.db.DatabaseManager;
import camp.woowa.jspcafe.db.MySQLDatabaseManager;
import camp.woowa.jspcafe.repository.*;
import camp.woowa.jspcafe.service.QuestionService;
import camp.woowa.jspcafe.service.ReplyService;
import camp.woowa.jspcafe.service.UserService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

import javax.sql.DataSource;

public class DIContextListener implements ServletContextListener {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(DIContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // DB Connection
        DatabaseManager dm = new MySQLDatabaseManager();

        // TODO: 코드 리오더링에 따른 순서 보장 불가 고려

        // UserRepository 를 생성하고 UserService 에 주입
        UserRepository userRepository = RepositoryFactory.createUserRepository(dm);
        UserService userService = new UserService(userRepository);
        ServiceLocator.registerService(UserService.class, userService);

        // ReplyRepository 를 생성하고 ReplyService 에 주입
        ReplyRepository replyRepository = RepositoryFactory.createReplyRepository(dm);
        ReplyService replyService = new ReplyService(replyRepository);
        ServiceLocator.registerService(ReplyService.class, replyService);

        // QuestionRepository 를 생성하고 QuestionService 에 주입
        QuestionRepository questionRepository = RepositoryFactory.createQuestionRepository(dm);
        QuestionService questionService = new QuestionService(questionRepository, replyService);
        ServiceLocator.registerService(QuestionService.class, questionService);
    }
}
