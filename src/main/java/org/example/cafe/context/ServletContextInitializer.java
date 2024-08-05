package org.example.cafe.context;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.util.function.Supplier;
import org.example.cafe.application.AuthService;
import org.example.cafe.application.QuestionService;
import org.example.cafe.application.ReplyService;
import org.example.cafe.application.UserService;
import org.example.cafe.domain.QuestionRepository;
import org.example.cafe.domain.ReplyRepository;
import org.example.cafe.domain.UserRepository;
import org.example.cafe.infrastructure.QuestionJdbcRepository;
import org.example.cafe.infrastructure.ReplyJdbcRepository;
import org.example.cafe.infrastructure.UserJdbcRepository;
import org.example.cafe.infrastructure.database.DbConnector;
import org.example.cafe.infrastructure.jdbc.JdbcTemplate;
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
            setContext("ObjectMapper", () -> {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                return mapper;
            });

            setContext("DbConnector", () -> new DbConnector().init());
            setContext("JdbcTemplate", () ->
                    new JdbcTemplate(getBean("DbConnector", DbConnector.class).getDataSource()));

            setContext("UserRepository", () ->
                    new UserJdbcRepository(getBean("JdbcTemplate", JdbcTemplate.class)));
            setContext("ReplyRepository", () ->
                    new ReplyJdbcRepository(getBean("JdbcTemplate", JdbcTemplate.class)));
            setContext("QuestionRepository", () ->
                    new QuestionJdbcRepository(getBean("JdbcTemplate", JdbcTemplate.class)));

            setContext("PostCountCache", () ->
                    new QuestionCountCache(getBean("QuestionRepository", QuestionRepository.class)));

            setContext("UserService", () ->
                    new UserService(getBean("UserRepository", UserRepository.class)));
            setContext("AuthService", () ->
                    new AuthService(getBean("UserRepository", UserRepository.class)));
            setContext("QuestionService", () ->
                    new QuestionService(getBean("QuestionRepository", QuestionRepository.class),
                            getBean("ReplyRepository", ReplyRepository.class)));
            setContext("ReplyService", () ->
                    new ReplyService(getBean("ReplyRepository", ReplyRepository.class)));
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

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        getBean("DbConnector", DbConnector.class).close();
        getBean("PostCountCache", QuestionCountCache.class).close();
    }
}
