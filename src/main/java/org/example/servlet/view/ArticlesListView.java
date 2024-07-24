package org.example.servlet.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.data.ArticleDataHandler;
import org.example.data.ArticleDataHandlerInMemory;
import org.example.domain.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet("/articles")
public class ArticlesListView extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(ArticlesListView.class);
    private ArticleDataHandler articleDataHandler;

    @Override
    public void init() throws ServletException {
        super.init();
        articleDataHandler = (ArticleDataHandlerInMemory) getServletContext().getAttribute("articleDataHandlerInMemory");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        log.debug("[ArticlesListView] called");
        List<Article> articles = articleDataHandler.findAll();
        request.setAttribute("articles", articles);
        request.getRequestDispatcher("/article/list.jsp").forward(request, response);
    }
}