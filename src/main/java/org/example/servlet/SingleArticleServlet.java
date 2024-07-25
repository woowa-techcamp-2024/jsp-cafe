package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.entity.Article;
import org.example.service.ArticleService;

@WebServlet("/question/*")
public class SingleArticleServlet extends HttpServlet {

    private final ArticleService articleService = new ArticleService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        String articleId = getArticleIdFromPath(request.getRequestURI());

        Article article = articleService.findById(Integer.parseInt(articleId));

        request.setAttribute("article", article);
        request.getRequestDispatcher("/WEB-INF/article.jsp").forward(request, response);
    }

    private String getArticleIdFromPath(String path) {
        String[] pathSplit = path.split("/");
        return pathSplit[pathSplit.length - 1];
    }

}
