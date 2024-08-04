package codesquad.article.handler;

import codesquad.common.handler.HttpServletRequestHandler;
import codesquad.global.dao.ArticleQuery;
import codesquad.global.servlet.annotation.RequestMapping;
import codesquad.user.domain.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RequestMapping("^/questions/\\d+/update-form$")
public class QnaUpdateFormHandler extends HttpServletRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(QnaUpdateFormHandler.class);

    private final ArticleQuery articleQuery;

    public QnaUpdateFormHandler(ArticleQuery articleQuery) {
        this.articleQuery = articleQuery;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("QnaUpdateForm serve");
        long articleId;
        try {
            articleId = extractIdFromPath(req);
        } catch (NumberFormatException e) {
            req.setAttribute("errorMsg", "올바르지 않은 요청입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        Optional<ArticleQuery.ArticleResponse> findArticleResponse = articleQuery.findById(articleId);
        if (findArticleResponse.isEmpty()) {
            req.setAttribute("errorMsg", "존재하지 않는 글입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        if (!Objects.equals(findArticleResponse.get().writerId(), loginUser.getId())) {
            req.setAttribute("errorMsg", "다른 사람의 글을 수정할 수 없습니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("articleResponse", findArticleResponse.get());
        req.getRequestDispatcher("/WEB-INF/views/qna/update-form.jsp").forward(req, resp);
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
}
