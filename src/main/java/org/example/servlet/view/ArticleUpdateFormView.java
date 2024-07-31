package org.example.servlet.view;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.example.constance.DataHandler;
import org.example.constance.SessionName;
import org.example.data.ArticleDataHandler;
import org.example.domain.Article;
import org.example.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/articles/update-form/*")
public class ArticleUpdateFormView extends HttpServlet {
    private final Logger log = (Logger) LoggerFactory.getLogger(ArticleUpdateFormView.class);
    private ArticleDataHandler articleDataHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        articleDataHandler = (ArticleDataHandler) config.getServletContext()
                .getAttribute(DataHandler.ARTICLE.getValue());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        Long articleId = Long.valueOf(pathInfo.substring(1));
        Article article = articleDataHandler.findByArticleId(articleId);
        if (isArticleNull(req, resp, article)) {
            return;
        }
        if (isCorrectAuthor(req, resp, article)) {
            return;
        }
        req.setAttribute("article", article);
        req.getRequestDispatcher("/article/update-form.jsp").forward(req, resp);
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
        if (!user.getUserId().equals(article.getUserId())) {
            request.setAttribute("status_code", HttpServletResponse.SC_NOT_FOUND);
            request.setAttribute("message", "Article 작성자가 아닙니다");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            request.getRequestDispatcher("/error/error.jsp").forward(request, response);
            return true;
        }
        return false;
    }
}
