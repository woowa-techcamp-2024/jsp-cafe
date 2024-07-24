package org.example.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "postsServlet", urlPatterns = "/posts")
public class PostsServlet extends HttpServlet {
    // 게시글 작성
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Post post = new Post(
                null,
                request.getParameter("writer"),
                request.getParameter("title"),
                request.getParameter("contents"));
        post.setCreatedAt(LocalDateTime.now());

        PostDb.addPost(post);

        response.sendRedirect("/");
    }
}
