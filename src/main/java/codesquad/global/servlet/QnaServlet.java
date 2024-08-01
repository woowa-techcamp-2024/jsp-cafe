package codesquad.global.servlet;

import codesquad.article.domain.Article;
import codesquad.article.repository.ArticleRepository;
import codesquad.common.authorization.annotation.Authorized;
import codesquad.common.exception.UnauthorizedRequestException;
import codesquad.global.dao.ArticleQuery;
import codesquad.global.dao.ArticleQuery.ArticleResponse;
import codesquad.user.domain.User;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@WebServlet(urlPatterns = "/questions/*")
public class QnaServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(QnaServlet.class);

    private ArticleQuery articleQuery;
    private ArticleRepository articleRepository;

    public QnaServlet() {
    }

    public QnaServlet(ArticleQuery articleQuery, ArticleRepository articleRepository) {
        this.articleQuery = articleQuery;
        this.articleRepository = articleRepository;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        articleQuery = (ArticleQuery) servletContext.getAttribute("ArticleQuery");
        articleRepository = (ArticleRepository) servletContext.getAttribute("ArticleRepository");
        logger.info("QnaServlet initialized");
    }

    /**
     * 질문 수정 폼 요청
     * 질문 정보 요청
     */
    @Authorized
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("requesting article info");
        String pathInfo = req.getPathInfo();
        if (pathInfo.endsWith("/update-form")) {
            logger.info("requesting user update form");
            processUpdateForm(req, resp);
        } else {
            logger.info("requesting user info");
            processArticleInfo(req, resp);
        }
    }

    /**
     * 질문 수정 요청
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("requesting article update");
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        String content = req.getParameter("contents");
        long articleId;
        try {
            articleId = getArticleId(req);
        } catch (NumberFormatException e) {
            req.setAttribute("errorMsg", "올바르지 않은 요청입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        Optional<Article> findArticle = articleRepository.findById(articleId);
        if (findArticle.isEmpty()) {
            req.setAttribute("errorMsg", "존재하지 않는 글입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        Article article = findArticle.get();
        try {
            article.update(loginUser.getUserId(), content);
        } catch (UnauthorizedRequestException e) {
            req.setAttribute("errorMsg", "다른 사람의 글을 수정할 수 없습니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        articleRepository.update(article);
        resp.sendRedirect(req.getContextPath() + "/questions/" + article.getId());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long articleId;
        try {
            articleId = getArticleId(req);
        } catch (NumberFormatException e) {
            req.setAttribute("errorMsg", "올바르지 않은 요청입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        Optional<Article> findArticle = articleRepository.findById(articleId);
        if (findArticle.isEmpty()) {
            req.setAttribute("errorMsg", "존재하지 않는 글입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        Article article = findArticle.get();
        try {
            article.delete(loginUser.getUserId());
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
        }
        articleRepository.update(article);
        resp.sendRedirect(req.getContextPath() + "/");
    }

    private void processUpdateForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long articleId;
        try {
            articleId = getArticleId(req);
        } catch (NumberFormatException e) {
            req.setAttribute("errorMsg", "올바르지 않은 요청입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        Optional<ArticleResponse> findArticleResponse = articleQuery.findById(articleId);
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

    private void processArticleInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long articleId;
        try {
            articleId = getArticleId(req);
        } catch (NumberFormatException e) {
            req.setAttribute("errorMsg", "올바르지 않은 요청입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        Optional<ArticleResponse> articleResponse = articleQuery.findById(articleId);
        if (articleResponse.isEmpty()) {
            req.setAttribute("errorMsg", "존재하지 않는 글입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("articleResponse", articleResponse.get());
        req.getRequestDispatcher("/WEB-INF/views/qna/show.jsp").forward(req, resp);
    }

    private long getArticleId(HttpServletRequest req) {
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
