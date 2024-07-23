package org.example.servlet.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.domain.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/articles")
public class ArticlesListView extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(ArticlesListView.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("[ArticlesListView] called");
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("title", "a", "hong", LocalDateTime.now()));
        articles.add(new Article("title2", "a","kim", LocalDateTime.now()));

        request.setAttribute("articles", articles);
        request.getRequestDispatcher("/article/list.jsp").forward(request, response);
    }
}