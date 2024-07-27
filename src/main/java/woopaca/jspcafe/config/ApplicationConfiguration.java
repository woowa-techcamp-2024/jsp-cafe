package woopaca.jspcafe.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import woopaca.jspcafe.database.DatabaseInitializer;

@WebListener
public class ApplicationConfiguration implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DatabaseInitializer.initialize(InstanceFactory.jdbcTemplate());

        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("userService", InstanceFactory.userService());
        servletContext.setAttribute("postService", InstanceFactory.postService());
    }
}
