package codesquad.global.container.initializer;

import codesquad.article.infra.MySqlArticleRepository;
import codesquad.comment.infra.MySqlCommentRepository;
import codesquad.common.db.connection.ConnectionManager;
import codesquad.common.db.connection.ServerConnectionManager;
import codesquad.common.db.transaction.JdbcTransactionManager;
import codesquad.global.dao.MySqlArticleQuery;
import codesquad.global.dao.MySqlUserQuery;
import codesquad.user.infra.MySqlUserRepository;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class RepositoryRegister implements AppInit {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryRegister.class);

    @Override
    public void onStartUp(ServletContext servletContext) throws NamingException {
        // tomcat이 관리하는 DBCP(org.apache.tomcat.jdbc.pool) 조회
        Context initContext = new InitialContext();
        Context envContext = (Context) initContext.lookup("java:/comp/env");
        DataSource ds = (DataSource) envContext.lookup("jdbc/cafe-db");
        // ConnectionManager
        ConnectionManager connectionManager = new ServerConnectionManager(ds);
        // JdbcTransactionManager
        JdbcTransactionManager jdbcTransactionManager = new JdbcTransactionManager(connectionManager);
        // Repository 등록
        servletContext.setAttribute("UserRepository", new MySqlUserRepository(jdbcTransactionManager));
        servletContext.setAttribute("UserQuery", new MySqlUserQuery(jdbcTransactionManager));
        servletContext.setAttribute("ArticleRepository", new MySqlArticleRepository(jdbcTransactionManager));
        servletContext.setAttribute("ArticleQuery", new MySqlArticleQuery(jdbcTransactionManager));
        servletContext.setAttribute("CommentRepository", new MySqlCommentRepository(jdbcTransactionManager));
        logger.info("Repository registered on context");
    }

    @Override
    public int order() {
        return 0;
    }
}
