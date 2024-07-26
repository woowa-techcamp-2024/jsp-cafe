package codesquad.container;

import codesquad.domain.article.ArticleDao;
import codesquad.infra.*;
import codesquad.servlet.dao.ArticleQueryDao;
import codesquad.domain.user.UserDao;
import jakarta.servlet.ServletContext;

public class DatabaseRegister implements AppInit {
    @Override
    public void onStartUp(ServletContext servletContext) {
        UserDao userDao = new MySqlUserDao();
        ArticleDao articleDao = new MySqlArticleDao();
        ArticleQueryDao articleQueryDao = new MySqlArticleQueryDao();
        servletContext.setAttribute("userDao", userDao);
        servletContext.setAttribute("articleDao", articleDao);
        servletContext.setAttribute("articleQueryDao", articleQueryDao);
    }
}
