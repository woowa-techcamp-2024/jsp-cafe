package org.example.demo.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.domain.Post;
import org.example.demo.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class HomeHandler {
    private static final Logger logger = LoggerFactory.getLogger(HomeHandler.class);
    private final PostRepository postRepository;

    public HomeHandler(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void handleGetPosts(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        request.setAttribute("posts", postRepository.getPosts());
        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
    }
}
