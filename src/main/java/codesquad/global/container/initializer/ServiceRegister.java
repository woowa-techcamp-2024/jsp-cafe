package codesquad.global.container.initializer;

import codesquad.article.handler.dao.ArticleQuery;
import codesquad.article.repository.ArticleRepository;
import codesquad.article.service.DeleteArticleServiceImpl;
import codesquad.article.service.QueryArticleService;
import codesquad.article.service.RegisterArticleService;
import codesquad.article.service.UpdateArticleService;
import codesquad.comment.repository.CommentRepository;
import codesquad.comment.service.DeleteCommentService;
import codesquad.comment.service.RegisterCommentServiceImpl;
import codesquad.user.repository.UserRepository;
import codesquad.user.service.SignInService;
import codesquad.user.service.SignUpService;
import codesquad.user.service.UpdateUserService;
import jakarta.servlet.ServletContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceRegister implements AppInit {
    private static final Logger logger = LoggerFactory.getLogger(ServiceRegister.class);

    @Override
    public void onStartUp(ServletContext servletContext) throws Exception {
        // Repository 조회
        UserRepository userRepository = (UserRepository) servletContext.getAttribute("UserRepository");
        ArticleRepository articleRepository = (ArticleRepository) servletContext.getAttribute("ArticleRepository");
        CommentRepository commentRepository = (CommentRepository) servletContext.getAttribute("CommentRepository");
        ArticleQuery articleQuery = (ArticleQuery) servletContext.getAttribute("ArticleQuery");
        // user 관련 서비스
        servletContext.setAttribute("SignInService", new SignInService(userRepository));
        servletContext.setAttribute("SignUpService", new SignUpService(userRepository));
        servletContext.setAttribute("UpdateUserService", new UpdateUserService(userRepository));
        // article 관련 서비스
        servletContext.setAttribute("UpdateArticleService", new UpdateArticleService(articleRepository));
        servletContext.setAttribute("RegisterArticleService", new RegisterArticleService(articleRepository));
        servletContext.setAttribute("DeleteArticleService", new DeleteArticleServiceImpl(articleRepository, commentRepository));
        servletContext.setAttribute("QueryArticleService", new QueryArticleService(articleQuery));
        // comment 관련 서비스
        servletContext.setAttribute("RegisterCommentService", new RegisterCommentServiceImpl(articleRepository, commentRepository));
        servletContext.setAttribute("DeleteCommentService", new DeleteCommentService(commentRepository));
        logger.info("Service registered on context");
    }

    @Override
    public int order() {
        return 1;
    }
}
