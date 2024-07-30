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
        System.out.println("init home servlet!");
        this.postRepository = PostRepository.getInstance();
    }

    // 홈 화면
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("home servlet!!!");
        System.out.println("postRepository = " + postRepository.getPosts());

        request.setAttribute("posts", postRepository.getPosts());

        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
