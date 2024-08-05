package codesquad.global.container.listener;

import codesquad.article.handler.*;
import codesquad.article.handler.dao.ArticleQuery;
import codesquad.article.service.DeleteArticleService;
import codesquad.article.service.QueryArticleService;
import codesquad.article.service.RegisterArticleService;
import codesquad.article.service.UpdateArticleService;
import codesquad.auth.handler.LoginHandler;
import codesquad.auth.handler.LogoutHandler;
import codesquad.comment.handler.CommentAjaxHandler;
import codesquad.comment.handler.CommentHandler;
import codesquad.comment.handler.CommentsAjaxHandler;
import codesquad.comment.handler.CommentsHandler;
import codesquad.comment.service.DeleteCommentService;
import codesquad.comment.service.RegisterCommentService;
import codesquad.common.handler.HandlerMapping;
import codesquad.common.handler.RequestHandler;
import codesquad.common.handler.annotation.RequestMapping;
import codesquad.common.handler.annotation.Response;
import codesquad.global.filter.AuthenticationFilter;
import codesquad.user.handler.UserHandler;
import codesquad.user.handler.UserRegisterFormHandler;
import codesquad.user.handler.UserUpdateFormHandler;
import codesquad.user.handler.UsersHandler;
import codesquad.user.handler.dao.UserQuery;
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
        QueryArticleService queryArticleService = (QueryArticleService) servletContext.getAttribute("QueryArticleService");
        // Handler 등록
        List<HandlerMapping> handlerMappings = new ArrayList<>();
        registerHandlerMapping(handlerMappings, new QnaHandler(queryArticleService, updateArticleService, deleteArticleService));
        registerHandlerMapping(handlerMappings, new QnasHandler(registerArticleService));
        registerHandlerMapping(handlerMappings, new QnaRegisterFormHandler());
        registerHandlerMapping(handlerMappings, new QnaUpdateFormHandler(queryArticleService));
        registerHandlerMapping(handlerMappings, new UserHandler(userQuery));
        registerHandlerMapping(handlerMappings, new UsersHandler(userQuery, signUpService, updateUserService));
        registerHandlerMapping(handlerMappings, new UserRegisterFormHandler());
        registerHandlerMapping(handlerMappings, new UserUpdateFormHandler(userQuery));
        registerHandlerMapping(handlerMappings, new CommentsHandler(registerCommentService));
        registerHandlerMapping(handlerMappings, new CommentHandler(deleteCommentService));
        registerHandlerMapping(handlerMappings, new CommentAjaxHandler(deleteCommentService));
        registerHandlerMapping(handlerMappings, new CommentsAjaxHandler(registerCommentService));
        registerHandlerMapping(handlerMappings, new LoginHandler(signInService));
        registerHandlerMapping(handlerMappings, new LogoutHandler());
        registerHandlerMapping(handlerMappings, new IndexHandler(queryArticleService));
        servletContext.setAttribute("HandlerMappings", handlerMappings);
        logger.info("HandlerMapping registered on context");

        FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("AuthenticationFilter", new AuthenticationFilter());
        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
        logger.info("AuthenticationFilter registered");
    }

    private void registerHandlerMapping(List<HandlerMapping> handlerMappings, RequestHandler handler) {
        RequestMapping requestMappingAnnotation = handler.getClass().getAnnotation(RequestMapping.class);
        if (requestMappingAnnotation == null) {
            throw new RuntimeException("No RequestMapping annotation found");
        }
        Response responseAnnotation = getResponseAnnotation(handler);
        Pattern[] patterns = toPatterns(requestMappingAnnotation.value());
        for (Pattern pattern : patterns) {
            handlerMappings.add(new HandlerMapping(pattern, handler, responseAnnotation.returnType()));
        }
    }

    private Response getResponseAnnotation(RequestHandler handler) {
        Response[] responses = handler.getClass().getAnnotationsByType(Response.class);
        if (responses.length == 0) {
            throw new RuntimeException("No Response annotation found");
        }
        if (responses.length > 1) {
            throw new RuntimeException("Multi Response annotation found");
        }
        return responses[0];
    }

    private Pattern[] toPatterns(String... requestMaps) {
        Pattern[] patterns = new Pattern[requestMaps.length];
        for (int i = 0; i < requestMaps.length; i++) {
            patterns[i] = Pattern.compile(requestMaps[i]);
        }
        return patterns;
    }
}
