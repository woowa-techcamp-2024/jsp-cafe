package org.example.post.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import org.example.post.model.dao.Post;
import org.example.post.model.dto.PostResponse;
import org.example.post.service.PostService;

@WebServlet("/questions")
public class PostServlet extends HttpServlet {

    private PostService postService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");

        try {
            PostResponse post = postService.getPostById(Long.parseLong(id));
            req.setAttribute("post", post);
            req.getRequestDispatcher("/jsp/post/show.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String writer = req.getParameter("writer");
            String title = req.getParameter("title");
            String contents = req.getParameter("contents");
            Post post = Post.create(writer, title, contents);
            postService.create(post);
            resp.sendRedirect("/");
        } catch (SQLException e) {

        }
    }

    @Override
    public void init() throws ServletException {
        super.init();
        postService = new PostService();
    }
}

