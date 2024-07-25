package woowa.camp.jspcafe.configuration;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.repository.article.ArticleRepository;
import woowa.camp.jspcafe.repository.article.InMemoryArticleRepository;
import woowa.camp.jspcafe.repository.InMemoryUserRepository;
import woowa.camp.jspcafe.repository.UserRepository;
import woowa.camp.jspcafe.service.ArticleService;
import woowa.camp.jspcafe.service.UserService;
import woowa.camp.jspcafe.utils.time.DateTimeProvider;
import woowa.camp.jspcafe.utils.time.LocalDateTimeProvider;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(AppContextListener.class);

    private static final DateTimeProvider dateTimeProvider = new LocalDateTimeProvider();

    private static final UserRepository userRepository = new InMemoryUserRepository();
    private static final UserService userService = new UserService(userRepository, dateTimeProvider);

    private static final ArticleRepository articleRepository = new InMemoryArticleRepository();
    private static final ArticleService articleService = new ArticleService(articleRepository, userRepository, dateTimeProvider);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("AppContextListener - contextInitialized start");
        ServletContext context = sce.getServletContext();
        context.setAttribute("userRepository", userRepository);
        context.setAttribute("userService", userService);

        context.setAttribute("articleRepository", articleRepository);
        context.setAttribute("articleService", articleService);

        log.debug("AppContextListener - contextInitialized end");
    }
}
