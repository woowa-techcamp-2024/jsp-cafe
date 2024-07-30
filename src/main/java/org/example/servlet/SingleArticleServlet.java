package org.example.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.example.entity.Article;
import org.example.entity.User;
import org.example.service.ArticleService;
import org.example.service.NotSameAutherException;
import org.example.util.SessionUtil;

@WebServlet("/question/*")
public class SingleArticleServlet extends HttpServlet {

    private final ArticleService articleService = new ArticleService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        String articleId = getArticleIdFromPath(request.getRequestURI());

        Article article = articleService.findById(Integer.parseInt(articleId));

        if(request.getRequestURI().endsWith("/updateForm")){
            Optional<String> userId = SessionUtil.extractUserId(request);

            if (userId.isEmpty() || !article.isOwner(userId.get())) {
                request.getRequestDispatcher("/WEB-INF/error/not-same-author.jsp").forward(request, response);
                return;
            }
            request.setAttribute("article", article);
            request.getRequestDispatcher("/WEB-INF/updateForm.jsp").forward(request, response);
            return;
        }

        request.setAttribute("article", article);
        request.getRequestDispatcher("/WEB-INF/article.jsp").forward(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        String userId = SessionUtil.extractUserId(request)
            .orElseThrow(() -> new IllegalArgumentException("로그인이 필요합니다."));

        String articleId = getArticleIdFromPath(request.getRequestURI());
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        try {
            articleService.update(Integer.parseInt(articleId), title, content, userId);
        }catch (NotSameAutherException e){
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/error.jsp");
            dispatcher.forward(request, response);
        }

        response.sendRedirect("/question/" + articleId);
    }

    private String getArticleIdFromPath(String path) {
        String[] pathSplit = path.split("/");
        return pathSplit[pathSplit.length - 1];
    }

}
