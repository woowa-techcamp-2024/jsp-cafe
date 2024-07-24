package codesqaud.app.servlet;

import codesqaud.app.dao.ArticleDao;
import codesqaud.app.model.Article;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "/qna")
public class QnaServlet extends HttpServlet {
    private ArticleDao articleDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.articleDao = (ArticleDao) config.getServletContext().getAttribute("articleDao");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authorId = req.getParameter("authorId");
        String title = req.getParameter("title");
        String content = req.getParameter("contents");

        Article article = new Article(title, content, authorId);
        articleDao.save(article);

        resp.sendRedirect("/");
        throw new IllegalArgumentException("hi");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}
