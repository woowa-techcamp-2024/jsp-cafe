package org.example.jspcafe.post.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.jspcafe.comment.request.CommentCreateRequest;
import org.example.jspcafe.comment.request.CommentDeleteRequest;
import org.example.jspcafe.comment.request.CommentModifyRequest;
import org.example.jspcafe.comment.service.CommentService;
import org.example.jspcafe.di.ApplicationContext;
import org.example.jspcafe.post.request.PostModifyRequest;
import org.example.jspcafe.post.response.CommentResponse;
import org.example.jspcafe.post.service.PostService;

import java.io.BufferedReader;
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] split = req.getRequestURI().split("/");

        /**
         * 게시글 삭제 API
         * /api/posts/{postId}
         */
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

            try {
                postService.deletePost(userId, postId);

                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (Exception e) {
                session.setAttribute("errorMessage", e.getMessage());
                resp.sendRedirect(req.getContextPath() + "/posts/" + postId);
            }
            return;
        }

        /**
         * 댓글 삭제 API
         * /api/posts/{postId}/comments/{commentId}
         */
        if (split.length == 6 && "comments".equals(split[4])) {
            Long commentId = Long.parseLong(split[split.length - 1]);
            Long userId = (Long) req.getSession().getAttribute("userId");

            if (userId == null) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            try {
                CommentDeleteRequest request = new CommentDeleteRequest(commentId, userId);
                commentService.deleteComment(request);
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } catch (Exception e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }

            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] split = req.getRequestURI().split("/");

        /**
         * 댓글 조회 API
         * post에 달린 댓글을 조회합니다.
         * /api/posts/{postId}/comments
         */
        if (split.length == 5 && split[4].equals("comments")) {
            Long postId = Long.parseLong(split[split.length - 2]);
            final CommentResponse[] comments = commentService.findCommentsJoinUser(postId).toArray(CommentResponse[]::new);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            PrintWriter out = resp.getWriter();
            String jsonResponse = objectMapper.writeValueAsString(comments);
            out.print(jsonResponse);
            out.flush();
            return;
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] split = req.getRequestURI().split("/");

        /**
         * 게시글 수정 API
         * /api/posts/{postId}
         */
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

            StringBuilder jsonBuffer = new StringBuilder();
            String line;
            try (BufferedReader reader = req.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBuffer.append(line);
                }
            }

            // JSON 데이터를 파싱하여 title, content 필드를 추출합니다.
            String jsonString = jsonBuffer.toString();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            String title = jsonNode.get("title").asText();
            String content = jsonNode.get("content").asText();

            PostModifyRequest request = new PostModifyRequest(userId, postId, title, content);
            try {
                postService.modifyPost(request);
            } catch (Exception e) {
                session.setAttribute("errorMessage", e.getMessage());
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
            return;
        }

        /**
         * 댓글 수정 API
         * /api/posts/{postId}/comments/{commentId}
         */
        if (split.length == 6 && split[4].equals("comments")) {
            Long commentId = Long.parseLong(split[split.length - 1]);
            Long userId = (Long) req.getSession().getAttribute("userId");

            StringBuilder jsonBuffer = new StringBuilder();
            String line;
            try (BufferedReader reader = req.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBuffer.append(line);
                }
            }
            // JSON 데이터를 파싱하여 content 필드를 추출합니다.
            String jsonString = jsonBuffer.toString();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            String content = jsonNode.get("content").asText();

            if (userId == null) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }


            CommentModifyRequest request = new CommentModifyRequest(commentId, userId, content);
            commentService.modifyComment(request);

            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] split = req.getRequestURI().split("/");

        /**
         * 댓글 생성 API
         * post에 댓글을 생성합니다.
         * /api/posts/{postId}/comments
         */
        if (split.length == 5 && split[4].equals("comments")) {
            Long postId = Long.parseLong(split[split.length - 2]);
            Long userId = (Long) req.getSession().getAttribute("userId");

            if (userId == null) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            StringBuilder jsonBuffer = new StringBuilder();
            String line;
            try (BufferedReader reader = req.getReader()) {
                while ((line = reader.readLine()) != null) {
                    jsonBuffer.append(line);
                }
            }
            // JSON 데이터를 파싱하여 content 필드를 추출합니다.
            String jsonString = jsonBuffer.toString();
            JsonNode jsonNode = objectMapper.readTree(jsonString);

            String content = jsonNode.get("content").asText();
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

    }
}
