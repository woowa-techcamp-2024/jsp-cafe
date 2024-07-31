package org.example.servlet;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.example.dto.ArticleCreateReqDto;
import org.example.service.ArticleService;
import org.example.util.SessionUtil;

@WebServlet("/questions")
public class ArticleServlet extends HttpServlet {

    private final ArticleService articleService = new ArticleService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        // 게시글 작성
        Optional<String> author = SessionUtil.extractUserId(request);
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        author.ifPresentOrElse(
            userId -> {
                ArticleCreateReqDto articleDto = new ArticleCreateReqDto(userId, title, content);
                // articleDto를 사용하여 추가 작업 수행
                articleService.save(articleDto);
                try {
                    response.sendRedirect("/");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            },
            () -> {
                try {
                    response.sendRedirect("/login");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        );
    }
}
