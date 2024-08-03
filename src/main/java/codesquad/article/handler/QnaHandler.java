package codesquad.article.handler;

import codesquad.article.service.DeleteArticleService;
import codesquad.article.service.UpdateArticleService;
import codesquad.article.service.UpdateArticleService.Command;
import codesquad.common.exception.CommentExistException;
import codesquad.common.exception.NoSuchElementException;
import codesquad.common.exception.UnauthorizedRequestException;
import codesquad.common.handler.RequestHandler;
import codesquad.common.handler.annotation.Authorized;
import codesquad.global.dao.ArticleQuery;
import codesquad.global.dao.ArticleQuery.ArticleDetailResponse;
import codesquad.global.servlet.annotation.RequestMapping;
import codesquad.user.domain.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

@RequestMapping("^/questions/\\d+$")
public class QnaHandler extends HttpServlet implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(QnaHandler.class);

    private final ArticleQuery articleQuery;
    private final UpdateArticleService updateArticleService;
    private final DeleteArticleService deleteArticleService;

    public QnaHandler(ArticleQuery articleQuery, UpdateArticleService updateArticleService, DeleteArticleService deleteArticleService) {
        this.articleQuery = articleQuery;
        this.updateArticleService = updateArticleService;
        this.deleteArticleService = deleteArticleService;
    }

    /**
     * 질문 수정 폼 요청
     * 질문 정보 요청
     */
    @Authorized
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("requesting article info");
        long articleId;
        try {
            articleId = extractIdFromPath(req);
        } catch (NumberFormatException e) {
            req.setAttribute("errorMsg", "올바르지 않은 요청입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        Optional<ArticleDetailResponse> articleResponse = articleQuery.findDetailById(articleId);
        if (articleResponse.isEmpty()) {
            req.setAttribute("errorMsg", "존재하지 않는 글입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("articleDetailResponse", articleResponse.get());
        req.getRequestDispatcher("/WEB-INF/views/qna/show.jsp").forward(req, resp);
    }

    /**
     * 질문 수정 요청
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("requesting article update");
        long articleId;
        String content = req.getParameter("contents");
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        try {
            articleId = extractIdFromPath(req);
        } catch (NumberFormatException e) {
            req.setAttribute("errorMsg", "올바르지 않은 요청입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        Command command = new Command(articleId, content, loginUser.getUserId());
        try {
            updateArticleService.update(command);
        } catch (NoSuchElementException e) {
            req.setAttribute("errorMsg", "존재하지 않는 글입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        } catch (UnauthorizedRequestException e) {
            req.setAttribute("errorMsg", "다른 사람의 글을 수정할 수 없습니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/questions/" + articleId);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long articleId;
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        try {
            articleId = extractIdFromPath(req);
        } catch (NumberFormatException e) {
            req.setAttribute("errorMsg", "올바르지 않은 요청입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        DeleteArticleService.Command command = new DeleteArticleService.Command(articleId, loginUser.getUserId());
        try {
            deleteArticleService.delete(command);
        } catch (NoSuchElementException e) {
            req.setAttribute("errorMsg", "존재하지 않는 글입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        } catch (UnauthorizedRequestException e) {
            HttpServletRequestWrapper request = new HttpServletRequestWrapper(req) {
                @Override
                public String getMethod() {
                    return "GET";
                }
            };
            request.setAttribute("errorMsg", "다른 사람의 글을 삭제할 수 없습니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(request, resp);
            return;
        } catch (CommentExistException e) {
            HttpServletRequestWrapper request = new HttpServletRequestWrapper(req) {
                @Override
                public String getMethod() {
                    return "GET";
                }
            };
            request.setAttribute("errorMsg", "다른 사람이 작성한 댓글이 있습니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(request, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/");
    }

    private long extractIdFromPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || !pathInfo.startsWith("/")) {
            throw new NumberFormatException("Invalid path info");
        }
        if (pathInfo.endsWith("/update-form")) {
            return Long.parseLong(pathInfo.substring(1, pathInfo.indexOf("/update-form")));
        }
        return Long.parseLong(pathInfo.substring(1));
    }

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }
}
