package org.example.servlet.api;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import org.example.constance.AliveStatus;
import org.example.constance.DataHandler;
import org.example.constance.SessionName;
import org.example.data.ArticleDataHandler;
import org.example.domain.Article;
import org.example.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/api/articles")
public class ArticleRegisterApi extends HttpServlet {
    private final Logger log = LoggerFactory.getLogger(ArticleRegisterApi.class);
    private ArticleDataHandler articleDataHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        articleDataHandler = (ArticleDataHandler) config.getServletContext()
                .getAttribute(DataHandler.ARTICLE.getValue());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(SessionName.USER.getName());
        String author = user.getNickname();
        Article article = new Article(title, content, author, LocalDateTime.now(), AliveStatus.ALIVE, user.getUserId());
        Article savedArticle = articleDataHandler.insert(article);
        log.debug("[ArticleRegisterApi]" + savedArticle.toString());
        response.sendRedirect("/");
    }
}
