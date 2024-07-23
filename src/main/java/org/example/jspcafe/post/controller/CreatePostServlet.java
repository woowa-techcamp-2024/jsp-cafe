//package org.example.jspcafe.post.controller;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.example.jspcafe.di.ApplicationContext;
//import org.example.jspcafe.post.request.PostCreateRequest;
//import org.example.jspcafe.post.service.PostService;
//
//import java.io.IOException;
//
//
//@WebServlet(name = "createPostServlet", value = "/api/posts")
//public class CreatePostServlet extends HttpServlet {
//
//    private PostService postService;
//
//    @Override
//    public void init() throws ServletException {
//        super.init();
//        this.postService = ApplicationContext.getContainer().resolve(PostService.class);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Long userId = (Long) req.getSession().getAttribute("userId");
//
//        String title = req.getParameter("title");
//        String content = req.getParameter("content");
//
//        PostCreateRequest request = new PostCreateRequest(userId, title, content);
//
//        try {
//            postService.createPost(request);
//            resp.sendRedirect("/");
//        } catch (Exception e) {
//            req.setAttribute("errorMessage", e.getMessage());
//            req.getRequestDispatcher("/create-post.jsp").forward(req, resp);
//        }
//    }
//}
