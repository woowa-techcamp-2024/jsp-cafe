package codesquad.servlet;

import codesquad.article.Article;
import codesquad.article.ArticleDao;
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

@WebServlet(urlPatterns = "/questions")
public class QnasServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(QnasServlet.class);

    private ArticleDao articleDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        articleDao = (ArticleDao) servletContext.getAttribute("articleDao");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("uploading article");
        String title = req.getParameter("title");
        String writer = req.getParameter("writer");
        String content = req.getParameter("contents");

        articleDao.save(new Article(title, writer, content));

        resp.sendRedirect(req.getContextPath() + "/");
    }
}
