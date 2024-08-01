package codesquad.container.initializer;

import codesquad.common.db.connection.ConnectionManager;
import codesquad.domain.article.ArticleDao;
import codesquad.domain.user.UserDao;
import codesquad.infra.*;
import codesquad.servlet.dao.ArticleQuery;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DatabaseRegister implements AppInit {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseRegister.class);

    @Override
    public void onStartUp(ServletContext servletContext) throws NamingException {
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        DataSource ds = (DataSource) envContext.lookup("jdbc/cafe-db");
        ConnectionManager connectionManager = new ServerConnectionManager(ds);
        UserDao userDao = new MySqlUserDao(connectionManager);
        ArticleDao articleDao = new MySqlArticleDao(connectionManager);
        ArticleQuery articleQuery = new MySqlArticleQuery(connectionManager);
        servletContext.setAttribute("userDao", userDao);
        servletContext.setAttribute("articleDao", articleDao);
        servletContext.setAttribute("articleQuery", articleQuery);
        logger.info("Database registered on context");
    }
}
