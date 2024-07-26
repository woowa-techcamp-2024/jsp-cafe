package cafe.config;

import cafe.domain.db.ArticleDatabase;
import cafe.domain.db.SessionDatabase;
import cafe.domain.db.UserDatabase;
import cafe.service.ArticleService;
import cafe.service.SessionService;
import cafe.service.UserService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class DIContainerInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        UserDatabase userDatabase = new UserDatabase();
        ArticleDatabase articleDatabase = new ArticleDatabase();
        SessionDatabase sessionDatabase = new SessionDatabase();

        UserService userService = new UserService(userDatabase, sessionDatabase);
        ArticleService articleService = new ArticleService(articleDatabase);
        SessionService sessionService = new SessionService(userDatabase, sessionDatabase);

        servletContext.setAttribute("userService", userService);
        servletContext.setAttribute("articleService", articleService);
        servletContext.setAttribute("sessionService", sessionService);
    }
}
