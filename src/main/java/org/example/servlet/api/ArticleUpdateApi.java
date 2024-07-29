package org.example.servlet.api;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;
import org.example.constance.DataHandler;
import org.example.constance.SessionName;
import org.example.data.ArticleDataHandler;
import org.example.domain.Article;
import org.example.domain.User;
import org.slf4j.LoggerFactory;

@WebServlet("/api/articles/*")
public class ArticleUpdateApi extends HttpServlet {
    private final Logger log = (Logger) LoggerFactory.getLogger(ArticleUpdateApi.class);
    private ArticleDataHandler articleDataHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        articleDataHandler = (ArticleDataHandler) config.getServletContext()
                .getAttribute(DataHandler.ARTICLE.getValue());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Long articleId = Long.valueOf(req.getParameter("articleId"));
        Article article = articleDataHandler.findByArticleId(articleId);
        if (isArticleNull(req, resp, article)) {
            return;
        }
        if (isCorrectAuthor(req, resp, article)) {
            return;
        }
        // ToDo article update

        //
        resp.sendRedirect("/articles/" + articleId);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Long articleId = Long.valueOf(req.getParameter("articleId"));
        Article article = articleDataHandler.findByArticleId(articleId);
        if (isArticleNull(req, resp, article)) {
            return;
        }
        if (isCorrectAuthor(req, resp, article)) {
            return;
        }
        // ToDo Article Delete

        //
        resp.sendRedirect("/articles/" + articleId);
    }

    private boolean isArticleNull(HttpServletRequest request, HttpServletResponse response, Article article)
            throws ServletException, IOException {
        if (article == null) {
            request.setAttribute("status_code", HttpServletResponse.SC_NOT_FOUND);
            request.setAttribute("message", "Article 이 없습니다.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.getRequestDispatcher("/error/error.jsp").forward(request, response);
            return true;
        }
        return false;
    }

    private boolean isCorrectAuthor(HttpServletRequest request, HttpServletResponse response, Article article)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(SessionName.USER.getName());
        if (!user.getUserId().equals(article.getArticleId())) {
            request.setAttribute("status_code", HttpServletResponse.SC_NOT_FOUND);
            request.setAttribute("message", "Article 작성자가 아닙니다");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.getRequestDispatcher("/error/error.jsp").forward(request, response);
            return true;
        }
        return false;
    }
}
