package codesquad.jspcafe.servlet;

import codesquad.jspcafe.common.ApplicationProperties;
import codesquad.jspcafe.common.database.JDBCConnectionManager;
import codesquad.jspcafe.domain.article.repository.ArticleJdbcRepository;
import codesquad.jspcafe.domain.article.service.ArticleService;
import codesquad.jspcafe.domain.user.repository.UserJdbcRepository;
import codesquad.jspcafe.domain.user.service.UserService;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class DefaultServletContextListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(DefaultServletContextListener.class);

    private final ApplicationProperties applicationProperties = new ApplicationProperties();
    private final JDBCConnectionManager connectionManager = new JDBCConnectionManager(
        applicationProperties);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        jdbcDriverInit();
        sce.getServletContext().setAttribute("userService",
            new UserService(new UserJdbcRepository(connectionManager)));
        sce.getServletContext().setAttribute("articleService",
            new ArticleService(new ArticleJdbcRepository(connectionManager)));
    }

    private void jdbcDriverInit() {
        try {
            Class.forName(applicationProperties.getJdbcDriver());
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        }
    }
}
