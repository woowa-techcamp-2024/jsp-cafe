package codesquad.global.container.initializer;

import codesquad.article.infra.MySqlArticleRepository;
import codesquad.article.repository.ArticleRepository;
import codesquad.common.db.connection.ConnectionManager;
import codesquad.common.db.connection.ServerConnectionManager;
import codesquad.common.db.transaction.JdbcTransactionManager;
import codesquad.global.dao.ArticleQuery;
import codesquad.global.dao.MySqlArticleQuery;
import codesquad.user.infra.MySqlUserRepository;
import codesquad.user.repository.UserRepository;
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
        JdbcTransactionManager jdbcTransactionManager = new JdbcTransactionManager(connectionManager);
        UserRepository userRepository = new MySqlUserRepository(jdbcTransactionManager);
        ArticleRepository articleRepository = new MySqlArticleRepository(jdbcTransactionManager);
        ArticleQuery articleQuery = new MySqlArticleQuery(jdbcTransactionManager);
        servletContext.setAttribute("UserRepository", userRepository);
        servletContext.setAttribute("ArticleRepository", articleRepository);
        servletContext.setAttribute("ArticleQuery", articleQuery);
        servletContext.setAttribute("JdbcTransactionManager", jdbcTransactionManager);
        logger.info("Database registered on context");
    }
}
