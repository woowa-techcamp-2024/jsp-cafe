package org.example.demo.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.repository.PostRepository;

import java.io.IOException;

@WebServlet(name = "homeServlet", urlPatterns = "/")
public class HomeServlet extends HttpServlet {
    private PostRepository postRepository;

    @Override
    public void init() throws ServletException {
        this.postRepository = PostRepository.getInstance();
    }

    // 홈 화면
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("posts", postRepository.getPosts());

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
