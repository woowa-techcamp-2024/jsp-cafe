package org.example.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.model.PostCreateDao;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "postsServlet", urlPatterns = "/posts")
public class PostsServlet extends HttpServlet {
    // 게시글 작성
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String writer = request.getParameter("writer");
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");
        PostDb.addPost(new PostCreateDao(writer, title, contents));

        response.sendRedirect("/");
    }
}
