package codesquad.global.container.listener;

import codesquad.article.handler.QnaHandler;
import codesquad.article.handler.QnaRegisterFormHandler;
import codesquad.article.handler.QnaUpdateFormHandler;
import codesquad.article.handler.QnasHandler;
import codesquad.article.service.DeleteArticleService;
import codesquad.article.service.RegisterArticleService;
import codesquad.article.service.UpdateArticleService;
import codesquad.comment.handler.CommentHandler;
import codesquad.comment.handler.CommentsHandler;
import codesquad.comment.service.DeleteCommentService;
import codesquad.comment.service.RegisterCommentService;
import codesquad.common.handler.HandlerMapping;
import codesquad.common.handler.RequestHandler;
import codesquad.global.dao.ArticleQuery;
import codesquad.global.dao.UserQuery;
import codesquad.global.filter.AuthenticationFilter;
import codesquad.global.servlet.annotation.RequestMapping;
import codesquad.user.handler.UserHandler;
import codesquad.user.handler.UserRegisterFormHandler;
import codesquad.user.handler.UserUpdateFormHandler;
import codesquad.user.handler.UsersHandler;
import codesquad.user.service.SignInService;
import codesquad.user.service.SignUpService;
import codesquad.user.service.UpdateUserService;
import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Pattern;

public class HandlerRegister implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(HandlerRegister.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        // Repository 조회
        UserQuery userQuery = (UserQuery) servletContext.getAttribute("UserQuery");
        ArticleQuery articleQuery = (ArticleQuery) servletContext.getAttribute("ArticleQuery");
        // Service 조회
        SignInService signInService = (SignInService) servletContext.getAttribute("SignInService");
        SignUpService signUpService = (SignUpService) servletContext.getAttribute("SignUpService");
        UpdateUserService updateUserService = (UpdateUserService) servletContext.getAttribute("UpdateUserService");
        UpdateArticleService updateArticleService = (UpdateArticleService) servletContext.getAttribute("UpdateArticleService");
        RegisterArticleService registerArticleService = (RegisterArticleService) servletContext.getAttribute("RegisterArticleService");
        DeleteArticleService deleteArticleService = (DeleteArticleService) servletContext.getAttribute("DeleteArticleService");
        RegisterCommentService registerCommentService = (RegisterCommentService) servletContext.getAttribute("RegisterCommentService");
        DeleteCommentService deleteCommentService = (DeleteCommentService) servletContext.getAttribute("DeleteCommentService");
        // Handler 등록
        List<HandlerMapping> handlerMappings = new ArrayList<>();
        registerHandlerMapping(handlerMappings, new QnaHandler(articleQuery, updateArticleService, deleteArticleService));
        registerHandlerMapping(handlerMappings, new QnasHandler(registerArticleService));
        registerHandlerMapping(handlerMappings, new QnaRegisterFormHandler());
        registerHandlerMapping(handlerMappings, new QnaUpdateFormHandler(articleQuery));
        registerHandlerMapping(handlerMappings, new UserHandler(userQuery));
        registerHandlerMapping(handlerMappings, new UsersHandler(userQuery, signUpService, updateUserService));
        registerHandlerMapping(handlerMappings, new UserRegisterFormHandler());
        registerHandlerMapping(handlerMappings, new UserUpdateFormHandler(userQuery));
        registerHandlerMapping(handlerMappings, new CommentsHandler(registerCommentService));
        registerHandlerMapping(handlerMappings, new CommentHandler(deleteCommentService));
        servletContext.setAttribute("HandlerMappings", handlerMappings);
        logger.info("HandlerMapping registered on context");

        FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("AuthenticationFilter", new AuthenticationFilter());
        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
        logger.info("AuthenticationFilter registered");
    }

    private void registerHandlerMapping(List<HandlerMapping> handlerMappings, RequestHandler handler) {
        RequestMapping annotation = handler.getClass().getAnnotation(RequestMapping.class);
        if (annotation == null) {
            return;
        }
        Pattern[] patterns = toPatterns(annotation.value());
        for (Pattern pattern : patterns) {
            handlerMappings.add(new HandlerMapping(pattern, handler));
        }
    }

    private Pattern[] toPatterns(String... requestMaps) {
        Pattern[] patterns = new Pattern[requestMaps.length];
        for (int i = 0; i < requestMaps.length; i++) {
            patterns[i] = Pattern.compile(requestMaps[i]);
        }
        return patterns;
    }
}
