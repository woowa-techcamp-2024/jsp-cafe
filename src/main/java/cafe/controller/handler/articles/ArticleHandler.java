package cafe.controller.handler.articles;

import cafe.controller.handler.Handler;
import cafe.service.ArticleService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ArticleHandler implements Handler {
    private final ArticleService articleService;

    public ArticleHandler(ServletContext context) {
        articleService = (ArticleService) context.getAttribute("articleService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        req.setAttribute("article", articleService.find(req.getRequestURI()));
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/article/show.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }
}
