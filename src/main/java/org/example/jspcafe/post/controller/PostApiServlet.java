package org.example.jspcafe.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.jspcafe.comment.request.CommentCreateRequest;
import org.example.jspcafe.comment.service.CommentService;
import org.example.jspcafe.di.ApplicationContext;
import org.example.jspcafe.post.request.PostModifyRequest;
import org.example.jspcafe.post.response.CommentResponse;
import org.example.jspcafe.post.service.PostService;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "PostApiServlet", value = "/api/posts/*")
public class PostApiServlet extends HttpServlet {

    private PostService postService;
    private CommentService commentService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        super.init();
        this.postService = ApplicationContext.getContainer().resolve(PostService.class);
        this.commentService = ApplicationContext.getContainer().resolve(CommentService.class);
        this.objectMapper = ApplicationContext.getContainer().resolve(ObjectMapper.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] split = req.getRequestURI().split("/");

        // /api/posts/{postId}/comments
        if (split.length == 5 && split[4].equals("comments")) {
            Long postId = Long.parseLong(split[split.length - 2]);
            String content = req.getParameter("content");
            Long userId = (Long) req.getSession().getAttribute("userId");

            if (userId == null) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            CommentCreateRequest request = new CommentCreateRequest(postId, userId, content);
            final CommentResponse comment = commentService.createComment(request);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            String jsonResponse = objectMapper.writeValueAsString(comment);
            out.print(jsonResponse);
            out.flush();
            return;
        }

        // /api/posts/{postId}
        if (split.length == 4) {
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
}
