package woowa.camp.jspcafe.configuration;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.infra.DatabaseConnector;
import woowa.camp.jspcafe.infra.time.DateTimeProvider;
import woowa.camp.jspcafe.infra.time.LocalDateTimeProvider;
import woowa.camp.jspcafe.repository.article.ArticleRepository;
import woowa.camp.jspcafe.repository.article.DBArticleRepository;
import woowa.camp.jspcafe.repository.reply.DBReplyRepository;
import woowa.camp.jspcafe.repository.user.DBUserRepository;
import woowa.camp.jspcafe.repository.user.UserRepository;
import woowa.camp.jspcafe.service.ArticleService;
import woowa.camp.jspcafe.service.ReplyService;
import woowa.camp.jspcafe.service.UserService;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("AppContextListener - contextInitialized start");
        ServletContext context = sce.getServletContext();

        DateTimeProvider dateTimeProvider = new LocalDateTimeProvider();

        DatabaseConnector connector = new DatabaseConnector(context);

//        InMemoryDBInitializer.createUserTable(connector);
//        InMemoryDBInitializer.createArticleTable(connector);
//        InMemoryDBInitializer.createReplyTable(connector);

        UserRepository userRepository = new DBUserRepository(connector);
        UserService userService = new UserService(userRepository, dateTimeProvider);

        DBArticleRepository articleRepository = new DBArticleRepository(connector);
        articleRepository.initializeCacheTotalArticleCount();

        DBReplyRepository replyRepository = new DBReplyRepository(connector);
        ReplyService replyService = new ReplyService(replyRepository, userRepository, dateTimeProvider);
        ArticleService articleService = new ArticleService(articleRepository, userRepository, replyRepository,
                dateTimeProvider);

        context.setAttribute("userRepository", userRepository);
        context.setAttribute("userService", userService);

        context.setAttribute("articleRepository", articleRepository);
        context.setAttribute("articleService", articleService);

        context.setAttribute("replyRepository", replyRepository);
        context.setAttribute("replyService", replyService);

        log.debug("AppContextListener - contextInitialized end");
    }
}
