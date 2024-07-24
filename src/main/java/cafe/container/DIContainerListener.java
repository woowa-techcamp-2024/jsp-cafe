package cafe.container;

import cafe.domain.db.ArticleDatabase;
import cafe.domain.db.UserDatabase;
import cafe.service.ArticleService;
import cafe.service.UserService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class DIContainerListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        userInit(servletContext);
        articleInit(servletContext);
    }

    private void userInit(ServletContext servletContext) {
        UserDatabase userDatabase = new UserDatabase();
        UserService userService = new UserService(userDatabase);
        servletContext.setAttribute("userService", userService);
    }

    private void articleInit(ServletContext servletContext) {
        ArticleDatabase articleDatabase = new ArticleDatabase();
        ArticleService articleService = new ArticleService(articleDatabase);
        servletContext.setAttribute("articleService", articleService);
    }
}
