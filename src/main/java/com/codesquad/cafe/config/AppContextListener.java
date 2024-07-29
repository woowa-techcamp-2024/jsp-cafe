package com.codesquad.cafe.config;

import com.codesquad.cafe.db.JdbcTemplate;
import com.codesquad.cafe.db.PostDao;
import com.codesquad.cafe.db.UserDao;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class AppContextListener implements ServletContextListener {

    Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DataSource ds = null;
        try {
            Context initContext = new InitialContext();
            ds = (DataSource) initContext.lookup("java:/comp/env/jdbc/cafe");
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }

        registerRepository(sce.getServletContext(), ds);
        createTable(ds);
    }

    public void registerRepository(ServletContext sce, DataSource ds) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

        sce.setAttribute("jdbcTemplate", jdbcTemplate);
        sce.setAttribute("userRepository", new UserDao(jdbcTemplate));
        sce.setAttribute("postRepository", new PostDao(jdbcTemplate));
    }

    public void createTable(DataSource ds) {
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
        } catch (
                Exception e) {
            log.warn("Fail to init table: {}", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContextListener.super.contextDestroyed(sce);
    }

}
