package org.example.servlet.view;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.config.DataHandler;
import org.example.data.ArticleDataHandler;
import org.example.domain.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

@WebServlet("")
public class ArticlesListView extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(ArticlesListView.class);
    private ArticleDataHandler articleDataHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        articleDataHandler = (ArticleDataHandler) config.getServletContext()
                .getAttribute(DataHandler.ARTICLE.getValue());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        log.debug("[ArticlesListView] called");
        List<Article> articles = articleDataHandler.findAll();
        request.setAttribute("articles", articles);
        request.getRequestDispatcher("/article/list.jsp").forward(request, response);
    }
}