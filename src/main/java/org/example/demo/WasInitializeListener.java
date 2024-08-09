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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.example.demo.db.DbConfig.createDatabaseIfNotExists;

@WebListener
public class WasInitializeListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(WasInitializeListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("WasInitializeListener.contextInitialized start!");

        try {
            // JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new InternalServerError("Failed to load JDBC driver " + e.getMessage());
        }

        String dbHost = System.getenv("DB_HOST");
        if (dbHost == null || dbHost.isEmpty()) {
            dbHost = "localhost"; // 기본값 설정
        }

        String jdbcUrl = "jdbc:mysql://" + dbHost + ":3306";
        String dbUser = System.getenv("DB_USER");
        String dbPassword = System.getenv("DB_PASSWORD");

        if (dbUser == null || dbUser.isEmpty()) {
            dbUser = "root"; // 기본값 설정
        }

        if (dbPassword == null) {
            dbPassword = ""; // 기본값 설정
        }

        createDatabaseIfNotExists(jdbcUrl, dbUser, dbPassword);

        DbConfig dbConfig = new DbConfig(jdbcUrl + "/cafe", dbUser, dbPassword);

        dbConfig.initializeSchema();

        UserRepository userRepository = new UserRepository(dbConfig);
        PostRepository postRepository = new PostRepository(dbConfig);
        CommentRepository commentRepository = new CommentRepository(dbConfig);

        HomeHandler homeHandler = new HomeHandler(postRepository);
        UserHandler userHandler = new UserHandler(userRepository);
        PostHandler postHandler = new PostHandler(postRepository, commentRepository);
        CommentHandler commentHandler = new CommentHandler(commentRepository);

        sce.getServletContext().setAttribute("homeHandler", homeHandler);
        sce.getServletContext().setAttribute("userHandler", userHandler);
        sce.getServletContext().setAttribute("postHandler", postHandler);
        sce.getServletContext().setAttribute("commentHandler", commentHandler);

        logger.info("WasInitializeListener.contextInitialized finish!");
    }
}
