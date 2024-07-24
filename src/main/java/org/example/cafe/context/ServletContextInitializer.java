package org.example.cafe.context;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.function.Supplier;
import org.example.cafe.domain.user.UserRepository;
import org.slf4j.Logger;

@WebListener
public class ServletContextInitializer implements ServletContextListener {

    private static final Logger log = getLogger(ServletContextInitializer.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        registerBean(sce.getServletContext());
    }

    private void registerBean(ServletContext servletContext) {
        setContext(servletContext, "UserRepository", UserRepository::new);
    }

    public void setContext(ServletContext servletContext, String name, Supplier<Object> supplier) {
        servletContext.setAttribute(name, supplier.get());
        log.debug("Register bean: {}", name);
    }
}
