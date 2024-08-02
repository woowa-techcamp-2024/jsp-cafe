package cafe.controller.handler.articles;

import cafe.controller.handler.Handler;
import cafe.domain.entity.User;
import cafe.dto.ArticleDto;
import cafe.service.ArticleService;
import cafe.service.SessionService;
import cafe.util.JsonUtil;
import com.google.gson.Gson;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ArticleUpdateHandler implements Handler {
    private final ArticleService articleService;
    private final SessionService sessionService;

    public ArticleUpdateHandler(ServletContext context) {
        this.articleService = (ArticleService) context.getAttribute("articleService");
        this.sessionService = (SessionService) context.getAttribute("sessionService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String session = req.getSession(true).getId();
        User user = sessionService.findUserBySession(session);
        articleService.verifyArticleId(user, req.getRequestURI());

        req.setAttribute("article", articleService.find(req.getRequestURI()));
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/article/update_form.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String session = req.getSession(true).getId();
        User user = sessionService.findUserBySession(session);
        articleService.verifyArticleId(user, req.getRequestURI());

        String jsonData = JsonUtil.readJson(req.getReader());
        ArticleDto article = new Gson().fromJson(jsonData, ArticleDto.class);
        articleService.update(req.getRequestURI(), article.getTitle(), article.getContents());
    }


}
