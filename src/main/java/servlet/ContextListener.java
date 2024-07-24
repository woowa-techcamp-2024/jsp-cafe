package servlet;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import repository.UserRepository;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@WebListener
public class ContextListener implements ServletContextListener {

    Logger log = Logger.getLogger(ContextListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("Context Initialized");
        UserRepository repository = new UserRepository(new ConcurrentHashMap<>());
        sce.getServletContext().setAttribute("repository", repository);
    }
}
