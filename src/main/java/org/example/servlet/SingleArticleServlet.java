package org.example.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.example.entity.Article;
import org.example.entity.Reply;
import org.example.service.ArticleService;
import org.example.exception.NotSameAuthorException;
import org.example.service.ReplyService;
import org.example.util.BodyParser;
import org.example.util.LoggerUtil;
import org.example.util.SessionUtil;
import org.json.JSONObject;
import org.slf4j.Logger;

@WebServlet("/question/*")
public class SingleArticleServlet extends HttpServlet {

    private final ArticleService articleService = new ArticleService();
    private final ReplyService replyService = new ReplyService();
    private final Logger logger = LoggerUtil.getLogger();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        String articleId = getArticleIdFromPath(request.getRequestURI());
        logger.info("articleId: {}", articleId);
        Article article = articleService.findById(Integer.parseInt(articleId));

        if(request.getRequestURI().endsWith("/updateForm")){
            logger.info("updateForm: {}", articleId);
            Optional<String> userId = SessionUtil.extractUserId(request);
            if (userId.isEmpty() || !article.isOwner(userId.get())) {
                request.getRequestDispatcher("/WEB-INF/error/not-same-author.jsp").forward(request, response);
                return;
            }
            request.setAttribute("article", article);
            request.getRequestDispatcher("/WEB-INF/updateForm.jsp").forward(request, response);
            return;
        }

        List<Reply> replyList = replyService.findRepliesByArticleId(article.getArticleId());
        logger.info("uri: {}", request.getRequestURI());
        if(request.getRequestURI().endsWith("/replies")){
            logger.info("replies: {}", articleId);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            String json = new JSONObject().put("replies", replyList).toString();
            response.getWriter().write(json);
            return;
        }

        request.setAttribute("article", article);
        request.setAttribute("replies", replyList);
        request.getRequestDispatcher("/WEB-INF/article.jsp").forward(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        String userId = SessionUtil.extractUserId(request)
            .orElseThrow(() -> new IllegalArgumentException("로그인이 필요합니다."));
        logger.info("userId by delete: {}", userId);
        String articleId = getArticleIdFromPath(request.getRequestURI());
        logger.info("articleId by delete: {}", articleId);

        try {
            articleService.delete(Integer.parseInt(articleId), userId);
            response.setHeader("Location", "/");
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NotSameAuthorException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            request.getRequestDispatcher("/WEB-INF/error/not-same-author.jsp").forward(request, response);
            return;
        }catch (Exception e){
            e.printStackTrace();
        }
        logger.info("게시글 삭제 완료");
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        String userId = SessionUtil.extractUserId(request)
            .orElseThrow(() -> new IllegalArgumentException("로그인이 필요합니다."));
        logger.info("userId: {}", userId);
        String articleId = getArticleIdFromPath(request.getRequestURI());
        String bodyString = BodyParser.stringBody(request);
        JSONObject body = new JSONObject(bodyString);

        String title = body.getString("title");
        String content = body.getString("content");
        logger.info(request.toString());
        logger.info("title: {}, content: {}", title, content);

        articleService.update(Integer.parseInt(articleId), title, content, userId);
        response.setHeader("Location", "/question/" + articleId);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    private String getArticleIdFromPath(String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("잘못된 경로입니다.");
        }

        if (path.endsWith("/updateForm") || path.endsWith("/replies")) {
            path = path.replace("/updateForm", "");
            path = path.replace("/replies", "");
        }

        String[] pathSplit = path.split("/");
        return pathSplit[pathSplit.length - 1];
    }

}
