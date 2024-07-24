package org.example.post.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.example.post.model.dto.PostResponse;
import org.example.post.repository.PostRepository;
import org.example.post.service.PostService;

@WebServlet("")
public class MainServlet extends HttpServlet {

    PostService postService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<PostResponse> postResponses = postService.getAll();
            req.setAttribute("posts", postResponses);
            req.getRequestDispatcher("/jsp/post/list.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        postService = new PostService(new PostRepository());
    }
}
