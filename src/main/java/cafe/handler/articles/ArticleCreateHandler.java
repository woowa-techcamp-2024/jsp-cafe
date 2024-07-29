package cafe.handler.articles;

import cafe.handler.Handler;
import cafe.service.ArticleService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ArticleCreateHandler implements Handler {
    private final ArticleService articleService;

    public ArticleCreateHandler(ServletContext context) {
        articleService = (ArticleService) context.getAttribute("articleService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/article/form.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String writer = req.getParameter("writer");
        String title = req.getParameter("title");
        String contents = req.getParameter("contents");

        articleService.save(writer, title, contents);
        resp.sendRedirect("/");
    }
}
