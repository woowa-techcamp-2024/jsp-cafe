package cafe.config;

import cafe.controller.handler.articles.*;
import cafe.controller.handler.DefaultHandler;
import cafe.controller.handler.comments.CommentCreateHandler;
import cafe.controller.handler.comments.CommentDeleteHandler;
import cafe.controller.handler.comments.CommentListHandler;
import cafe.controller.handler.users.UserInfoEditHandler;
import cafe.controller.handler.users.UserInfoHandler;
import cafe.controller.handler.users.UserInfoListHandler;
import cafe.controller.handler.users.UserSignInHandler;
import cafe.controller.handler.users.UserSignOutHandler;
import cafe.controller.handler.users.UserSignUpHandler;
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
        commentHandlerInit(servletContext);
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
        servletContext.setAttribute("articleUpdateHandler", new ArticleUpdateHandler(servletContext));
        servletContext.setAttribute("articleDeleteHandler", new ArticleDeleteHandler(servletContext));
    }

    private void commentHandlerInit(ServletContext servletContext) {
        servletContext.setAttribute("commentListHandler", new CommentListHandler(servletContext));
        servletContext.setAttribute("commentCreateHandler", new CommentCreateHandler(servletContext));
        servletContext.setAttribute("commentDeleteHandler", new CommentDeleteHandler(servletContext));
    }
}
