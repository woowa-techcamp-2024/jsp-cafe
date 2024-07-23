package codesquad.jspcafe.domain.article.controller;

import codesquad.jspcafe.domain.article.payload.response.ArticleCommonResponse;
import codesquad.jspcafe.domain.article.service.ArticleService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/questions/*")
public class ArticleServlet extends HttpServlet {

    private transient ArticleService articleService;

    @Override
    public void init() throws ServletException {
        articleService = (ArticleService) getServletContext().getAttribute("articleService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "아티클을 찾을 수 없습니다.");
            return;
        }
        String articleId = pathInfo.substring(1);
        ArticleCommonResponse articleCommonResponse = articleService.getArticleById(
            articleId);
        req.setAttribute("article", articleCommonResponse);
        req.getRequestDispatcher("/WEB-INF/jsp/question.jsp").forward(req, resp);
    }
}
