package codesquad.servlet;

import codesquad.servlet.dao.ArticleQueryDao;
import codesquad.servlet.dto.ArticleResponse;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

@WebServlet(urlPatterns = "/questions/*")
public class QnaServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(QnaServlet.class);

    private ArticleQueryDao articleQueryDao;

    public QnaServlet() {
    }

    public QnaServlet(ArticleQueryDao articleQueryDao) {
        this.articleQueryDao = articleQueryDao;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        articleQueryDao = (ArticleQueryDao) servletContext.getAttribute("articleQueryDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("requesting article info");
        long articleId;
        try {
            articleId = getArticleId(req);
        } catch (NumberFormatException e) {
            req.setAttribute("errorMsg", "올바르지 않은 요청입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        Optional<ArticleResponse> articleResponse = articleQueryDao.findById(articleId);
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
        return Long.parseLong(pathInfo.substring(1));
    }
}
