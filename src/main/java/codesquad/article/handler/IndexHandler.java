package codesquad.article.handler;

import codesquad.article.domain.vo.Status;
import codesquad.article.handler.dao.ArticleQuery;
import codesquad.article.handler.dto.response.ArticleResponse;
import codesquad.common.handler.HttpServletRequestHandler;
import codesquad.common.handler.annotation.RequestMapping;
import codesquad.common.handler.annotation.Response;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@Response
@RequestMapping("/")
public class IndexHandler extends HttpServletRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(IndexHandler.class);

    private final ArticleQuery articleQuery;

    public IndexHandler(ArticleQuery articleQuery) {
        this.articleQuery = articleQuery;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("IndexHandler doGet");
        String pageNumber = req.getParameter("pageNumber");
        String pageSize = req.getParameter("pageSize");
        if (pageNumber == null || pageNumber.isEmpty() || pageSize == null || pageSize.isEmpty()) {
            pageNumber = "1";
            pageSize = "15";
        }
        ArticleQuery.QueryRequest queryRequest = new ArticleQuery.QueryRequest(Integer.parseInt(pageNumber), Integer.parseInt(pageSize), Status.PUBLISHED);
        List<ArticleResponse> articleResponses = articleQuery.findAll(queryRequest);
        req.setAttribute("articleResponses", articleResponses);
        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
    }
}
