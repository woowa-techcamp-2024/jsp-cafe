package servlet;

import domain.Article;
import dto.ArticleDao;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.ArticleService;

import java.io.IOException;

@WebServlet("/questions")
public class QuestionServlet extends HttpServlet {

    private ArticleService articleService;
    Logger log = LoggerFactory.getLogger(QuestionServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        this.articleService = (ArticleService) context.getAttribute("articleService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if(id == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Article article = articleService.findById(Long.parseLong(id));
        req.setAttribute("article", article);
        req.getRequestDispatcher("/qna/show.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String writer = req.getParameter("writer");
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        log.info("{} {} {}" , writer, title, content);
        articleService.saveArticle(new ArticleDao(writer, title, content));
        resp.sendRedirect("/");
    }
}
