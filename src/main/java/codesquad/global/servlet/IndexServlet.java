package codesquad.global.servlet;

import codesquad.article.domain.vo.Status;
import codesquad.global.dao.ArticleQuery;
import codesquad.global.dao.ArticleQuery.ArticleResponse;
import codesquad.global.dao.ArticleQuery.QueryRequest;
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
import java.util.List;

@WebServlet(urlPatterns = "/")
public class IndexServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(IndexServlet.class);

    private ArticleQuery articleQuery;

    public IndexServlet() {
    }

    public IndexServlet(ArticleQuery articleQuery) {
        this.articleQuery = articleQuery;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        articleQuery = (ArticleQuery) servletContext.getAttribute("ArticleQuery");
        logger.info("IndexServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("IndexServlet doGet");
        QueryRequest queryRequest = new QueryRequest(1, 10, Status.PUBLISHED);
        List<ArticleResponse> articleResponses = articleQuery.findAll(queryRequest);
        req.setAttribute("articleResponses", articleResponses);
        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
    }
}
