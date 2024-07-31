package codesqaud.app.config;

import codesqaud.app.dao.*;
import codesqaud.app.dao.article.ArticleDao;
import codesqaud.app.dao.article.DbArticleDao;
import codesqaud.app.dao.user.DbUserDao;
import codesqaud.app.dao.user.UserDao;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

@WebListener
public class ServletContextInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

        try {
            initContext(servletContext);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    private void initContext(ServletContext servletContext) throws NamingException {
        DataSource datasource = initDataSource(servletContext);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(datasource);

        initTable(servletContext, jdbcTemplate);
        initComponents(servletContext, jdbcTemplate);
    }

    private DataSource initDataSource(ServletContext servletContext) throws NamingException {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        DataSource datasource = (DataSource) envContext.lookup("jdbc/cafeDB");

        servletContext.setAttribute("dataSource", datasource);
        return datasource;
    }

    private void initTable(ServletContext servletContext, JdbcTemplate jdbcTemplate) {
        String ddlAuto = servletContext.getInitParameter("ddl-auto");

        switch (ddlAuto) {
            case "CREATE" -> executeCreateTable(jdbcTemplate);
            case "CREATE_DROP" -> {
                executeDropTable(jdbcTemplate);
                executeCreateTable(jdbcTemplate);
            }
        }
    }

    private static void executeDropTable(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("""
                DROP TABLE articles;
                """);

        jdbcTemplate.execute("""
                DROP TABLE users;
                """);
    }

    private void executeCreateTable(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("""
                 CREATE TABLE IF NOT EXISTS users (
                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                     user_id VARCHAR(50) NOT NULL UNIQUE,
                     password VARCHAR(255) NOT NULL,
                     name VARCHAR(100) NOT NULL,
                     email VARCHAR(100) NOT NULL
                 );
                """);

        jdbcTemplate.execute("""
                CREATE TABLE IF NOT EXISTS articles (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    title VARCHAR (200) NOT NULL,
                    contents TEXT NOT NULL,
                    author_id VARCHAR (50) NOT NULL
                );
                """);
    }

    private void initComponents(ServletContext servletContext, JdbcTemplate jdbcTemplate) {
        UserDao userDao = new DbUserDao(jdbcTemplate);
        servletContext.setAttribute("userDao", userDao);

        ArticleDao articleDao = new DbArticleDao(jdbcTemplate);
        servletContext.setAttribute("articleDao", articleDao);
    }
}