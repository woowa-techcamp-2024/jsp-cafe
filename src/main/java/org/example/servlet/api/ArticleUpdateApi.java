package org.example.servlet.api;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import org.example.constance.DataHandler;
import org.example.constance.SessionName;
import org.example.data.ArticleDataHandler;
import org.example.domain.Article;
import org.example.domain.User;
import org.example.util.JsonParser;
import org.slf4j.Logger;
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
        log.info("[ArticleUpdateApi] called");
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        Long articleId = Long.parseLong(pathParts[1]);

        // JSON 요청 본문 읽기
        BufferedReader reader = req.getReader();
        StringBuilder jsonBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBody.append(line);
        }

        // JSON 파싱
        Map<String, Object> jsonObject = JsonParser.parse(jsonBody.toString());
        String title = (String) jsonObject.get("title");
        String content = (String) jsonObject.get("content");

        Article article = articleDataHandler.findByArticleId(articleId);
        if (isArticleNull(req, resp, article)) {
            return;
        }
        if (isCorrectAuthor(req, resp, article)) {
            return;
        }
        if (article != null) {
            article.update(title, content);
            // 글 업데이트
            Article upateArticle = articleDataHandler.update(article);
            if (upateArticle != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"message\": \"글이 성공적으로 수정되었습니다.\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\": \"글 수정 중 오류가 발생했습니다.\"}");
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("[ArticleUpdateApi] doDelete called");
        req.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        Long articleId = Long.parseLong(pathParts[1]);

        Article article = articleDataHandler.findByArticleId(articleId);
        if (isArticleNull(req, resp, article)) {
            return;
        }
        if (isCorrectAuthor(req, resp, article)) {
            return;
        }
        if (article != null) {
            article.delete();
            Article upateArticle = articleDataHandler.update(article);
            if (upateArticle != null) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("{\"message\": \"글이 성공적으로 삭제되었습니다.\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\": \"글 삭제 중 오류가 발생했습니다.\"}");
            }
        }
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
