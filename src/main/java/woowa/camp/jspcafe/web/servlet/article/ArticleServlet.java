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
import woowa.camp.jspcafe.repository.dto.response.ReplyResponse;
import woowa.camp.jspcafe.service.ArticleService;
import woowa.camp.jspcafe.service.PageVO;
import woowa.camp.jspcafe.service.ReplyService;
import woowa.camp.jspcafe.service.dto.response.ArticleDetailsResponse;
import woowa.camp.jspcafe.service.dto.response.ArticlePreviewResponse;

@WebServlet(name = "articleServlet", value = {"/articles/*", ""})
public class ArticleServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ArticleServlet.class);
    private ArticleService articleService;
    private ReplyService replyService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        this.articleService = (ArticleService) context.getAttribute("articleService");
        this.replyService = (ReplyService) context.getAttribute("replyService");

        if (this.articleService == null) {
            String errorMessage = "[ServletException] ArticleServlet -> ArticleService not initialized";
            log.error(errorMessage);
        }
        if (this.replyService == null) {
            String errorMessage = "[ServletException] ArticleServlet -> ReplyService not initialized";
            log.error(errorMessage);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("ArticleServlet doGet start");
        String contextPath = req.getContextPath();
        Map<String, String> pathVariables;

        pathVariables = extractPathVariables(contextPath + "/articles/{id}", req.getRequestURI());
        if (pathVariables.containsKey("id")) {
            handleDetailArticle(req, resp, pathVariables);
            return;
        }
        handleArticles(req, resp);
    }

    private void handleArticles(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long currentPage = getCurrentPage(req);
        List<ArticlePreviewResponse> articles = articleService.findArticleList(currentPage);
        Long totalArticleCounts = articleService.findTotalArticleCounts();
        PageVO page = new PageVO(currentPage, totalArticleCounts);
        log.info("page - {}", page);

        req.setAttribute("articles", articles);
        req.setAttribute("totalArticleCounts", totalArticleCounts);
        req.setAttribute("page", page);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/views/index.jsp");
        requestDispatcher.forward(req, resp);
        log.debug("ArticleServlet doGet end");
    }

    private long getCurrentPage(HttpServletRequest req) {
        String pageParam = req.getParameter("page");
        if (pageParam != null && !pageParam.isEmpty()) {
            return Long.parseLong(pageParam);
        }
        return 1;
    }

    private void handleDetailArticle(HttpServletRequest req, HttpServletResponse resp,
                                     Map<String, String> pathVariables) throws ServletException, IOException {
        log.info("handleDetailArticle start");
        Long articleId = Long.parseLong(pathVariables.get("id"));
        Long lastReplyId = Long.parseLong(pathVariables.get("lastReplyId"));
        ArticleDetailsResponse articleDetails = articleService.findArticleDetails(articleId);
        List<ReplyResponse> replies = replyService.findReplyList(articleId, lastReplyId);

        req.setAttribute("article", articleDetails);
        req.setAttribute("comments", replies);

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/views/article/show.jsp");
        requestDispatcher.forward(req, resp);
        log.info("handleDetailArticle end");
    }
}
