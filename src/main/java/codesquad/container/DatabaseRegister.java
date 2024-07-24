package codesquad.container;

import codesquad.article.InMemoryArticleDao;
import codesquad.user.InMemoryUserDao;
import jakarta.servlet.ServletContext;

public class DatabaseRegister implements AppInit {
    @Override
    public void onStartUp(ServletContext servletContext) {
        servletContext.setAttribute("userDao", new InMemoryUserDao());
        servletContext.setAttribute("articleDao", new InMemoryArticleDao());
    }
}
