package cafe.controller.handler.articles;

import cafe.controller.handler.Handler;
import cafe.domain.entity.User;
import cafe.service.ArticleService;
import cafe.service.CommentService;
import cafe.service.SessionService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ArticleDeleteHandler implements Handler {
    private final ArticleService articleService;
    private final SessionService sessionService;
    private final CommentService commentService;

    public ArticleDeleteHandler(ServletContext context) {
        this.articleService = (ArticleService) context.getAttribute("articleService");
        this.sessionService = (SessionService) context.getAttribute("sessionService");
        this.commentService = (CommentService) context.getAttribute("commentService");
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String session = req.getSession(true).getId();
        User user = sessionService.findUserBySession(session);
        articleService.verifyArticleId(user, req.getRequestURI());
        commentService.verifyCommentIdByArticleId(user, req.getRequestURI());

        articleService.deleteById(req.getRequestURI());
        commentService.deleteByArticleId(req.getRequestURI());
    }
}
