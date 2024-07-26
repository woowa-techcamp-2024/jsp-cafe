package woowa.camp.jspcafe.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.service.ArticleService;
import woowa.camp.jspcafe.service.dto.ArticleWriteRequest;

@WebServlet(name = "articleWriteServlet", value = "/articles/write")
public class ArticleWriteServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ArticleWriteServlet.class);
    private ArticleService articleService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        this.articleService = (ArticleService) context.getAttribute("articleService");
        if (this.articleService == null) {
            String errorMessage = "[ServletException] ArticleWriteServlet -> ArticleService not initialized";
            log.error(errorMessage);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("ArticleWriteServlet doGet start");
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/views/article/form.jsp");
        requestDispatcher.forward(req, resp);
        log.debug("ArticleWriteServlet doGet end");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("ArticleWriteServlet doPost start");

        String title = req.getParameter("title");
        String content = req.getParameter("content");
        log.info("title: " + title + ", content: " + content);

        // TODO: 로그인 기능 도입 후, authorId 수정 필요
        ArticleWriteRequest articleWriteRequest = new ArticleWriteRequest(null, title, content);
        articleService.writeArticle(articleWriteRequest);

        String contextPath = req.getContextPath();
        resp.sendRedirect(contextPath + "/");
        log.debug("ArticleWriteServlet doPost end");
    }
}
