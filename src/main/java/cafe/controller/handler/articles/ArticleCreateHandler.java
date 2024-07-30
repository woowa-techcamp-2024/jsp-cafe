package cafe.controller.handler.articles;

import cafe.controller.handler.Handler;
import cafe.domain.entity.User;
import cafe.service.ArticleService;
import cafe.service.SessionService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ArticleCreateHandler implements Handler {
    private final ArticleService articleService;
    private final SessionService sessionService;

    public ArticleCreateHandler(ServletContext context) {
        articleService = (ArticleService) context.getAttribute("articleService");
        sessionService = (SessionService) context.getAttribute("sessionService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/article/form.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String session = req.getSession(true).getId();
        User writer = sessionService.findUserBySession(session);
        String title = req.getParameter("title");
        String contents = req.getParameter("contents");

        articleService.save(writer.getUserId(), title, contents);
        resp.sendRedirect("/");
    }
}
