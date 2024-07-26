package servlet;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import repository.article.ArticleRepository;
import repository.article.JDBCArticleRepository;
import repository.users.JDBCUserRepository;
import repository.users.UserRepository;
import service.ArticleService;
import service.UserService;

import java.util.logging.Logger;

@WebListener
public class ContextListener implements ServletContextListener {

    Logger log = Logger.getLogger(ContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Context Initialized");
        UserRepository userRepository = new JDBCUserRepository();
        ArticleRepository articleRepository = new JDBCArticleRepository();

        UserService userService = new UserService(userRepository);
        ArticleService articleService = new ArticleService(articleRepository, userRepository);

        sce.getServletContext().setAttribute("userService", userService);
        sce.getServletContext().setAttribute("articleService", articleService);
    }
}
