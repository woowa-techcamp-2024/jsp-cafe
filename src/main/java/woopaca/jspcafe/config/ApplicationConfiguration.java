package woopaca.jspcafe.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import woopaca.jspcafe.database.DatabaseInitializer;
import woopaca.jspcafe.database.JdbcTemplate;

@WebListener
public class ApplicationConfiguration implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DatabaseInitializer.initialize(InstanceFactory.jdbcTemplate());

        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("userService", InstanceFactory.userService());
        servletContext.setAttribute("postService", InstanceFactory.postService());
        servletContext.setAttribute("authService", InstanceFactory.authService());
        servletContext.setAttribute("replyService", InstanceFactory.replyService());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        JdbcTemplate jdbcTemplate = InstanceFactory.jdbcTemplate();
        jdbcTemplate.shutdownConnectionPool();
    }
}
