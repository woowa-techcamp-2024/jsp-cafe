package org.example.servlet;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.dto.ArticleCreateReqDto;
import org.example.service.ArticleService;

@WebServlet("/questions")
public class ArticleServlet extends HttpServlet {

    private final ArticleService articleService = new ArticleService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        // 게시글 작성
        String author = request.getParameter("author");
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        ArticleCreateReqDto articleDto = new ArticleCreateReqDto(author, title, content);
        articleService.save(articleDto);

        response.sendRedirect("/");
    }
}
