package cafe.config;

import cafe.handler.articles.ArticleCreateHandler;
import cafe.handler.articles.ArticleHandler;
import cafe.handler.DefaultHandler;
import cafe.handler.articles.ArticleListHandler;
import cafe.handler.users.*;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class DIHandlerInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        servletContext.setAttribute("defaultHandler", new DefaultHandler(servletContext));
        userHandlerInit(servletContext);
        articleHandlerInit(servletContext);
    }

    private void userHandlerInit(ServletContext servletContext) {
        servletContext.setAttribute("userInfoHandler", new UserInfoHandler(servletContext));
        servletContext.setAttribute("userInfoListHandler", new UserInfoListHandler(servletContext));
        servletContext.setAttribute("userSignUpHandler", new UserSignUpHandler(servletContext));
        servletContext.setAttribute("userInfoEditHandler", new UserInfoEditHandler(servletContext));
        servletContext.setAttribute("userSignInHandler", new UserSignInHandler(servletContext));
        servletContext.setAttribute("userSignOutHandler", new UserSignOutHandler(servletContext));
    }

    private void articleHandlerInit(ServletContext servletContext) {
        servletContext.setAttribute("articleHandler", new ArticleHandler(servletContext));
        servletContext.setAttribute("articleListHandler", new ArticleListHandler(servletContext));
        servletContext.setAttribute("articleCreateHandler", new ArticleCreateHandler(servletContext));
    }
}
