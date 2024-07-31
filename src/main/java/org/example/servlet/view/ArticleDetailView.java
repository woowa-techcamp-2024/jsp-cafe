package org.example.servlet.view;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.example.constance.DataHandler;
import org.example.data.ArticleDataHandler;
import org.example.data.ReplyDataHandler;
import org.example.domain.Article;
import org.example.domain.Reply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(urlPatterns = {"/articles/*"})
public class ArticleDetailView extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(ArticleDetailView.class);
    private ArticleDataHandler articleDataHandler;
    private ReplyDataHandler replyDataHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        articleDataHandler = (ArticleDataHandler) config.getServletContext()
                .getAttribute(DataHandler.ARTICLE.getValue());
        replyDataHandler = (ReplyDataHandler) config.getServletContext()
                .getAttribute(DataHandler.REPLY.getValue());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("[ArticleDetailView] called");
        String pathInfo = request.getPathInfo();
        Long articleId = Long.valueOf(pathInfo.substring(1));
        log.debug("[ArticleDetailView] articleIdL: " + articleId);
        Article article = articleDataHandler.findByArticleId(articleId);
        if (article == null) {
            request.setAttribute("status_code", HttpServletResponse.SC_BAD_REQUEST);
            request.setAttribute("message", "게시글이 없습니다");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher("/error/error.jsp").forward(request, response);
            return;
        }
        List<Reply> replies = replyDataHandler.findAllByArticleId(articleId);
        request.setAttribute("article", article);
        request.setAttribute("replies", replies);
        request.getRequestDispatcher("/article/detail.jsp").forward(request, response);
    }
}
