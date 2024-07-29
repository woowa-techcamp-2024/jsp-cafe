package org.example.jspcafe.post.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.jspcafe.di.ApplicationContext;
import org.example.jspcafe.post.request.PostModifyRequest;
import org.example.jspcafe.post.service.PostService;

import java.io.IOException;

@WebServlet(name = "PostEditServlet", value = "/api/posts/*")
public class PostEditServlet extends HttpServlet {

    private PostService postService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.postService = ApplicationContext.getContainer().resolve(PostService.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Boolean isLogined = (Boolean) session.getAttribute("isLogined");
        Long userId = (Long) session.getAttribute("userId");

        if (isLogined == null || !isLogined) {
            resp.sendRedirect("/login");
            return;
        }

        String pathInfo = req.getPathInfo();
        Long postId = Long.parseLong(pathInfo.substring(1));

        String title = req.getParameter("title");
        String content = req.getParameter("content");

        PostModifyRequest request = new PostModifyRequest(userId, postId, title, content);
        try {
            postService.modifyPost(request);
        } catch (Exception e) {
            session.setAttribute("errorMessage", e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/post/edit/" + postId);
            return;
        }

        resp.sendRedirect("/posts/" + postId);
    }
}
