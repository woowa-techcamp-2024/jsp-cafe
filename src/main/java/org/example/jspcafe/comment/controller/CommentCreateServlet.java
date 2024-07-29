//package org.example.jspcafe.comment.controller;
//
//import com.google.gson.Gson;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.example.jspcafe.comment.request.CommentCreateRequest;
//import org.example.jspcafe.comment.service.CommentService;
//import org.example.jspcafe.di.ApplicationContext;
//import org.example.jspcafe.post.response.CommentResponse;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//
//@WebServlet("/api/posts/*/comments")
//public class CommentCreateServlet extends HttpServlet {
//
//
//
//    @Override
//    public void init() throws ServletException {
//        this.commentService = ApplicationContext.getContainer().resolve(CommentService.class);
//        this.gson = ApplicationContext.getContainer().resolve(Gson.class);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String[] split = req.getRequestURI().split("/");
//        Long postId = Long.parseLong(split[split.length - 2]);
//        String content = req.getParameter("content");
//        Long userId = (Long) req.getAttribute("userId");
//
//        CommentCreateRequest request = new CommentCreateRequest(postId, userId, content);
//        final CommentResponse comment = commentService.createComment(request);
//
//        resp.setContentType("application/json");
//        resp.setCharacterEncoding("UTF-8");
//        PrintWriter out = resp.getWriter();
//        String jsonResponse = gson.toJson(comment);
//        out.print(jsonResponse);
//        out.flush();
//
//    }
//}
