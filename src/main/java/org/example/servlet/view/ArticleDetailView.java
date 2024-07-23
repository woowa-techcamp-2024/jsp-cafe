package org.example.servlet.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.domain.Article;
import org.example.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(urlPatterns = {"/articles/*"})
public class ArticleDetailView extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(ArticleDetailView.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("[ArticleDetailView] called");
        String pathInfo = request.getPathInfo();
        Long articleId = Long.valueOf(pathInfo.substring(1));
        log.debug("[ArticleDetailView] articleIdL: "+articleId);
        Article article = new Article(articleId, "title", "a", "hong", LocalDateTime.now());
        request.setAttribute("article", article);
        request.getRequestDispatcher("/article/detail.jsp").forward(request, response);
    }
}
