package com.codesquad.cafe.config;

import com.codesquad.cafe.db.dao.CommentDao;
import com.codesquad.cafe.db.dao.JdbcTemplate;
import com.codesquad.cafe.db.dao.PostDao;
import com.codesquad.cafe.db.dao.UserDao;
import com.codesquad.cafe.service.PostService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class AppContextListener implements ServletContextListener {

    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Context initContext = new InitialContext();
            DataSource ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/cafe");
            registerRepository(sce.getServletContext(), ds);
            createTable(ds);
        } catch (Exception e) {
            log.warn("error occurred while initializing datasource");
            System.exit(1);
        }
    }

    public void registerRepository(ServletContext sce, DataSource ds) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

        PostDao postDao = new PostDao(jdbcTemplate);
        CommentDao commentDao = new CommentDao(jdbcTemplate);
        PostService postService = new PostService(postDao, commentDao);

        sce.setAttribute("jdbcTemplate", jdbcTemplate);
        sce.setAttribute("userRepository", new UserDao(jdbcTemplate));
        sce.setAttribute("postRepository", postDao);
        sce.setAttribute("commentDao", commentDao);
        sce.setAttribute("postService", postService);
    }

    public void createTable(DataSource ds) throws SQLException, IOException {
        try (Connection conn = ds.getConnection();
             Statement stmt = conn.createStatement()) {
            // init.sql 파일 읽기
            InputStream inputStream = getClass().getResourceAsStream("/init.sql");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String sql = sb.toString();
            for (String sqlStatement : sql.split(";")) {
                if (!sqlStatement.trim().isEmpty()) {
                    stmt.execute(sqlStatement);
                }
            }
            log.info("SQL executed: {}", sql);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
        DataSource dataSource = (DataSource) sce.getServletContext().getAttribute("dataSource");
        if (dataSource != null && dataSource instanceof BasicDataSource) {
            try {
                ((BasicDataSource) dataSource).close();
            } catch (SQLException e) {
                log.warn("error occurred while destroying datasource");
            }
        }
    }

}
