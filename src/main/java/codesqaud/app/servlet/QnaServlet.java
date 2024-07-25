package codesqaud.app.servlet;

import codesqaud.app.dao.article.ArticleDao;
import codesqaud.app.exception.HttpException;
import codesqaud.app.model.Article;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(urlPatterns = {"/qna/*", ""})
public class QnaServlet extends HttpServlet {
    private static final Pattern pattern = Pattern.compile("^/qna/([1-9][\\d]{0,9})$");
    private ArticleDao articleDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.articleDao = (ArticleDao) config.getServletContext().getAttribute("articleDao");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getRequestURI().equals("/")) {
            handleArticleList(req, resp);
            return;
        }

        if(req.getRequestURI().equals("/qna/form")){
            req.getRequestDispatcher("/WEB-INF/qna/form.jsp").forward(req, resp);
            return;
        }

        Matcher matcher = pattern.matcher(req.getRequestURI());
        if (matcher.matches()) {
            Long id = Long.parseLong(matcher.group(1));
            handleArticleDetails(req, resp, id);
            return;
        }

        throw new HttpException(HttpServletResponse.SC_NOT_FOUND);
    }

    private void handleArticleList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Article> articles = articleDao.findAll();

        req.setAttribute("articles", articles);
        req.setAttribute("articleSize", articles.size());

        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/index.jsp");
        requestDispatcher.forward(req, resp);
    }

    private void handleArticleDetails(HttpServletRequest req, HttpServletResponse resp, Long id) throws ServletException, IOException {
        Article article = articleDao.findById(id).orElseThrow(
                () -> new HttpException(HttpServletResponse.SC_NOT_FOUND)
        );
        req.setAttribute("article", article);
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/WEB-INF/qna/show.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String authorId = req.getParameter("authorId");
        String title = req.getParameter("title");
        String content = req.getParameter("contents");

        Article article = new Article(title, content, authorId);
        articleDao.save(article);

        resp.sendRedirect("/");
    }
}
