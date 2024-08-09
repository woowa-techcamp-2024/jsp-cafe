package woowa.camp.jspcafe.web.servlet.article;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.repository.dto.request.ArticleUpdateRequest;
import woowa.camp.jspcafe.service.ArticleService;
import woowa.camp.jspcafe.service.dto.response.ArticleUpdateResponse;
import woowa.camp.jspcafe.web.utils.PathVariableExtractor;

@WebServlet(name = "ArticleEditServlet", value = "/articles/edit/*")
public class ArticleEditServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ArticleEditServlet.class);
    private ArticleService articleService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        this.articleService = (ArticleService) context.getAttribute("articleService");
        if (this.articleService == null) {
            String errorMessage = "[ServletException] ArticleEditServlet -> ArticleService not initialized";
            log.error(errorMessage);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> pathVariables = PathVariableExtractor.extractPathVariables("/articles/edit/{articleId}",
                req.getRequestURI());
        Long articleId = Long.parseLong(pathVariables.get("articleId"));

        HttpSession session = req.getSession();
        User sessionUser = (User) session.getAttribute("WOOWA_SESSIONID");

        ArticleUpdateResponse updateArticle = articleService.findUpdateArticle(sessionUser, articleId);
        req.setAttribute("article", updateArticle);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/views/article/update_form.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("_method");
        if ("PUT".equalsIgnoreCase(method)) {
            doPut(req, resp);
            return;
        }

        if ("DELETE".equalsIgnoreCase(method)) {
            doDelete(req, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User sessionUser = (User) session.getAttribute("WOOWA_SESSIONID");

        Map<String, String> pathVariables = PathVariableExtractor.extractPathVariables("/articles/edit/{articleId}",
                req.getRequestURI());
        Long articleId = Long.parseLong(pathVariables.get("articleId"));

        String title = req.getParameter("title");
        String content = req.getParameter("content");
        ArticleUpdateRequest articleUpdateRequest = new ArticleUpdateRequest(title, content);
        articleService.updateArticle(sessionUser, articleId, articleUpdateRequest);

        resp.sendRedirect(req.getContextPath() + "/articles/" + articleId);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User sessionUser = (User) session.getAttribute("WOOWA_SESSIONID");

        Map<String, String> pathVariables = PathVariableExtractor.extractPathVariables("/articles/edit/{articleId}",
                req.getRequestURI());
        Long articleId = Long.parseLong(pathVariables.get("articleId"));

        articleService.deleteArticle(sessionUser, articleId);
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
