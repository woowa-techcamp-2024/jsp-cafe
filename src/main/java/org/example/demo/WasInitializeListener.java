package org.example.demo;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.example.demo.db.DbConfig;
import org.example.demo.exception.InternalServerError;
import org.example.demo.handler.CommentHandler;
import org.example.demo.handler.HomeHandler;
import org.example.demo.handler.PostHandler;
import org.example.demo.handler.UserHandler;
import org.example.demo.repository.CommentRepository;
import org.example.demo.repository.PostRepository;
import org.example.demo.repository.UserRepository;
import org.example.demo.validator.AuthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.example.demo.db.DbConfig.createDatabaseIfNotExists;

@WebListener
public class WasInitializeListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(WasInitializeListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new InternalServerError("Failed to load JDBC driver " + e.getMessage());
        }

        // db 초기화
        createDatabaseIfNotExists("jdbc:mysql://localhost", "root", "");

        // JdbcUrl 에 데이터베이스 명시 필요
        DbConfig dbConfig = new DbConfig("jdbc:mysql://localhost/cafe", "root", "");
        dbConfig.initializeSchema();
        UserRepository userRepository = new UserRepository(dbConfig);
        PostRepository postRepository = new PostRepository(dbConfig);
        CommentRepository commentRepository = new CommentRepository(dbConfig);

        AuthValidator authValidator = new AuthValidator();

        HomeHandler homeHandler = new HomeHandler(postRepository);
        UserHandler userHandler = new UserHandler(userRepository);
        PostHandler postHandler = new PostHandler(postRepository, authValidator);
        CommentHandler commentHandler = new CommentHandler(commentRepository, authValidator);

        sce.getServletContext().setAttribute("homeHandler", homeHandler);
        sce.getServletContext().setAttribute("userHandler", userHandler);
        sce.getServletContext().setAttribute("postHandler", postHandler);
        sce.getServletContext().setAttribute("commentHandler", commentHandler);

        logger.info("WasInitializeListener.contextInitialized finish!");
    }
}
