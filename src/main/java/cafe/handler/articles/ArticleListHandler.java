package cafe.handler.articles;

import cafe.handler.Handler;
import cafe.service.ArticleService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ArticleListHandler implements Handler {
    private ArticleService articleService;

    public ArticleListHandler(ServletContext context) {
        articleService = (ArticleService) context.getAttribute("articleService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setAttribute("articles", articleService.findAll());
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/article/list.jsp");
        dispatcher.forward(req, resp);
    }
}
