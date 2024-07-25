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

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        registerBean(sce.getServletContext());
    }

    // todo: 생성자 필요한 빈들 등록 과정 리팩토링
    private void registerBean(ServletContext servletContext) {
        try {
            setContext(servletContext, "DbConnector", () -> new DbConnector().init());

            setContext(servletContext, "UserRepository", () ->
                    new UserJdbcRepository((DbConnector) servletContext.getAttribute("DbConnector")));
            setContext(servletContext, "UserService", () ->
                    new UserService((UserRepository) servletContext.getAttribute("UserRepository")));

            setContext(servletContext, "QuestionRepository", () ->
                    new QuestionJdbcRepository((DbConnector) servletContext.getAttribute("DbConnector")));
            setContext(servletContext, "QuestionService", () ->
                    new QuestionService((QuestionRepository) servletContext.getAttribute("QuestionRepository")));
        } catch (Exception e) {
            log.error("Failed to register beans", e);
            System.exit(-1);
        }
    }

    public void setContext(ServletContext servletContext, String name, Supplier<Object> supplier) {
        servletContext.setAttribute(name, supplier.get());
        log.debug("Register bean: {}", name);
    }
}
