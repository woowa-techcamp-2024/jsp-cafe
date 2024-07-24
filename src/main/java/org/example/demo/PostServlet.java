package org.example.demo;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "postServlet", urlPatterns = "/posts/*")
public class PostServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String postId = request.getRequestURI().substring(request.getContextPath().length() + "/posts/".length());

        Post post = PostDb.getPost(Long.parseLong(postId)).orElseThrow(() -> new RuntimeException("Post not found"));

        request.setAttribute("post", post);
        request.getRequestDispatcher("/post/show.jsp").forward(request, response);
    }
}
