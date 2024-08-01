package codesquad.container;

import codesquad.domain.article.ArticleDao;
import codesquad.domain.user.UserDao;
import codesquad.infra.*;
import codesquad.servlet.dao.ArticleQuery;
import jakarta.servlet.ServletContext;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DatabaseRegister implements AppInit {
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
    }
}
