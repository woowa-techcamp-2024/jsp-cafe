package woowa.camp.jspcafe.configuration;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.repository.InMemoryUserRepository;
import woowa.camp.jspcafe.repository.UserRepository;

@WebListener
public class AppContextListener implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(AppContextListener.class);

    private static final UserRepository userRepository = new InMemoryUserRepository();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.debug("AppContextListener - contextInitialized start");
        ServletContext context = sce.getServletContext();
        context.setAttribute("userRepository", userRepository);
        log.debug("AppContextListener - contextInitialized end");
    }
}
