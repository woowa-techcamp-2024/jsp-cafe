package woowa.camp.jspcafe.web.servlet.article;

import static woowa.camp.jspcafe.web.utils.PathVariableExtractor.extractPathVariables;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.domain.exception.ArticleException;
import woowa.camp.jspcafe.service.ArticleService;
import woowa.camp.jspcafe.service.dto.ArticleDetailsResponse;
import woowa.camp.jspcafe.service.dto.ArticlePreviewResponse;

@WebServlet(name = "articleServlet", value = {"/articles/*", ""})
public class ArticleServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ArticleServlet.class);
    private ArticleService articleService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        this.articleService = (ArticleService) context.getAttribute("articleService");
        if (this.articleService == null) {
            String errorMessage = "[ServletException] ArticleServlet -> ArticleService not initialized";
            log.error(errorMessage);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("ArticleServlet doGet start");
        try {
            String contextPath = req.getContextPath();
            Map<String, String> pathVariables =
                    extractPathVariables(contextPath + "/articles/{id}", req.getRequestURI());

            if (pathVariables.containsKey("id")) {
                handleDetailArticle(req, resp, pathVariables);
                return;
            }
            handleArticles(req, resp);
        } catch (ArticleException e) {
            log.warn("[ArticleException]", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void handleArticles(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int currentPage = 1;
        int totalPage = 10;
        String pageParam = req.getParameter("page");
        log.info("pageParam: " + pageParam);

        if (pageParam != null && !pageParam.isEmpty()) {
            currentPage = Integer.parseInt(pageParam);
        }
        List<ArticlePreviewResponse> articles = articleService.findArticleList(currentPage);
        log.info("articles = {}", articles);

        req.setAttribute("currentPage", currentPage);
        req.setAttribute("totalPages", totalPage);
        req.setAttribute("articles", articles);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/views/index.jsp");
        requestDispatcher.forward(req, resp);
        log.debug("ArticleServlet doGet end");
    }

    private void handleDetailArticle(HttpServletRequest req, HttpServletResponse resp,
                                     Map<String, String> pathVariables)
            throws ServletException, IOException {

        Long id = Long.parseLong(pathVariables.get("id"));
        ArticleDetailsResponse articleDetails = articleService.findArticleDetails(id);
        req.setAttribute("article", articleDetails);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/views/article/show.jsp");
        requestDispatcher.forward(req, resp);
    }
}
