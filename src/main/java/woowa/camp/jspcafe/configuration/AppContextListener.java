package woowa.camp.jspcafe.configuration;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.infra.DatabaseConnector;
import woowa.camp.jspcafe.repository.article.ArticleRepository;
import woowa.camp.jspcafe.repository.article.DBArticleRepository;
import woowa.camp.jspcafe.repository.user.DBUserRepository;
import woowa.camp.jspcafe.repository.user.UserRepository;
import woowa.camp.jspcafe.service.ArticleService;
import woowa.camp.jspcafe.service.UserService;
import woowa.camp.jspcafe.infra.time.DateTimeProvider;
import woowa.camp.jspcafe.infra.time.LocalDateTimeProvider;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(AppContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("AppContextListener - contextInitialized start");
        ServletContext context = sce.getServletContext();

        DateTimeProvider dateTimeProvider = new LocalDateTimeProvider();

        DatabaseConnector connector = new DatabaseConnector(context);

        UserRepository userRepository = new DBUserRepository(connector);
        UserService userService = new UserService(userRepository, dateTimeProvider);

        ArticleRepository articleRepository = new DBArticleRepository(connector);
        ArticleService articleService = new ArticleService(articleRepository, userRepository, dateTimeProvider);

        context.setAttribute("userRepository", userRepository);
        context.setAttribute("userService", userService);

        context.setAttribute("articleRepository", articleRepository);
        context.setAttribute("articleService", articleService);

        log.debug("AppContextListener - contextInitialized end");
    }
}
