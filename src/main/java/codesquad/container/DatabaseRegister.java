package codesquad.container;

import codesquad.domain.article.ArticleDao;
import codesquad.infra.InMemoryArticleDao;
import codesquad.servlet.dao.ArticleQueryDao;
import codesquad.servlet.dao.InMemoryArticleQueryDao;
import codesquad.infra.InMemoryUserDao;
import codesquad.domain.user.UserDao;
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
