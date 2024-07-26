package servlet;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import repository.ArticleRepository;
import repository.MemoryArticleRepository;
import repository.MemoryUserRepository;
import repository.UserRepository;
import service.ArticleService;
import service.UserService;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@WebListener
public class ContextListener implements ServletContextListener {

    Logger log = Logger.getLogger(ContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Context Initialized");
        UserRepository userRepository = new MemoryUserRepository(new ConcurrentHashMap<>());
        ArticleRepository articleRepository = new MemoryArticleRepository(new ConcurrentHashMap<>());

        UserService userService = new UserService(userRepository);
        ArticleService articleService = new ArticleService(articleRepository);

        sce.getServletContext().setAttribute("userService", userService);
        sce.getServletContext().setAttribute("articleService", articleService);
    }
}
