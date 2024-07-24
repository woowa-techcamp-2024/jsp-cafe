package org.example.servlet.api;

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
import java.time.LocalDateTime;

@WebServlet("/api/articles")
public class ArticleRegisterApi extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(ArticleRegisterApi.class);
    private ArticleDataHandler articleDataHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        articleDataHandler = (ArticleDataHandler) config.getServletContext().getAttribute(DataHandler.ARTICLE.getValue());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String author = "";
        Article article = new Article(title, content, author, LocalDateTime.now());
        Article savedArticle = articleDataHandler.save(article);
        log.debug("[ArticleRegisterApi]" + savedArticle.toString());
        response.sendRedirect("/articles");
    }
}
