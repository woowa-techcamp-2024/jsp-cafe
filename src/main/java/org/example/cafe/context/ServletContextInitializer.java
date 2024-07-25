package org.example.cafe.context;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.function.Supplier;
import org.example.cafe.application.QuestionService;
import org.example.cafe.application.UserService;
import org.example.cafe.domain.QuestionRepository;
import org.example.cafe.domain.UserRepository;
import org.example.cafe.infrastructure.QuestionJdbcRepository;
import org.example.cafe.infrastructure.UserJdbcRepository;
import org.example.cafe.infrastructure.database.DbConnector;
import org.slf4j.Logger;

@WebListener
public class ServletContextInitializer implements ServletContextListener {

    private static final Logger log = getLogger(ServletContextInitializer.class);

    private ServletContext servletContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.servletContext = sce.getServletContext();

        registerBean();
    }

    private void registerBean() {
        try {
            setContext("DbConnector", () -> new DbConnector().init());

            setContext("UserRepository", () ->
                    new UserJdbcRepository(getBean("DbConnector", DbConnector.class)));
            setContext("UserService", () ->
                    new UserService(getBean("UserRepository", UserRepository.class)));

            setContext("QuestionRepository", () ->
                    new QuestionJdbcRepository(getBean("DbConnector", DbConnector.class)));
            setContext("QuestionService", () ->
                    new QuestionService(getBean("QuestionRepository", QuestionRepository.class)));
        } catch (Exception e) {
            log.error("Failed to register beans", e);
            System.exit(-1);
        }
    }

    private void setContext(String name, Supplier<Object> supplier) {
        servletContext.setAttribute(name, supplier.get());
        log.debug("Register bean: {}", name);
    }

    private <T> T getBean(String name, Class<T> clazz) {
        return clazz.cast(servletContext.getAttribute(name));
    }
}
