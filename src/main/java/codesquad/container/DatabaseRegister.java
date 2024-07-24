package codesquad.container;

import codesquad.article.ArticleDao;
import codesquad.article.InMemoryArticleDao;
import codesquad.servlet.dao.ArticleQueryDao;
import codesquad.servlet.dao.InMemoryArticleQueryDao;
import codesquad.user.InMemoryUserDao;
import codesquad.user.UserDao;
import jakarta.servlet.ServletContext;

public class DatabaseRegister implements AppInit {
    @Override
    public void onStartUp(ServletContext servletContext) {
        UserDao userDao = new InMemoryUserDao();
        ArticleDao articleDao = new InMemoryArticleDao();
        ArticleQueryDao articleQueryDao = new InMemoryArticleQueryDao(articleDao, userDao);
        servletContext.setAttribute("userDao", userDao);
        servletContext.setAttribute("articleDao", articleDao);
        servletContext.setAttribute("articleQueryDao", articleQueryDao);
    }
}
