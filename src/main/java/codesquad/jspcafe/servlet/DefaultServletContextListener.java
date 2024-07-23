package codesquad.jspcafe.servlet;

import codesquad.jspcafe.domain.article.service.ArticleService;
import codesquad.jspcafe.domain.user.service.UserService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class DefaultServletContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().setAttribute("userService", new UserService());
        sce.getServletContext().setAttribute("articleService", new ArticleService());
    }
}
