package org.example.servlet.view;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.data.ArticleDataHandler;
import org.example.data.ArticleDataHandlerMySql;
import org.example.domain.Article;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet(urlPatterns = {"/articles/*"})
public class ArticleDetailView extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(ArticleDetailView.class);
    private ArticleDataHandler articleDataHandler;

    @Override
    public void init() throws ServletException {
        super.init();
        articleDataHandler = (ArticleDataHandlerMySql) getServletContext().getAttribute(ArticleDataHandlerMySql.class.getName());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("[ArticleDetailView] called");
        String pathInfo = request.getPathInfo();
        Long articleId = Long.valueOf(pathInfo.substring(1));
        log.debug("[ArticleDetailView] articleIdL: "+articleId);
        Article article = articleDataHandler.findByArticleId(articleId);
        if (article == null){
            request.setAttribute("status_code", HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("message", "게시글이 없습니다");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher("/error/error.jsp").forward(request, response);
            return;
        }
        request.setAttribute("article", article);
        request.getRequestDispatcher("/article/detail.jsp").forward(request, response);
    }
}
