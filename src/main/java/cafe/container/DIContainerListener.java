package cafe.container;

import cafe.domain.db.UserDatabase;
import cafe.service.UserService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class DIContainerListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        UserDatabase userDatabase = new UserDatabase();
        UserService userService = new UserService(userDatabase);

        servletContext.setAttribute("userService", userService);
    }
}
