package org.example.jspcafe.post.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.jspcafe.di.ApplicationContext;
import org.example.jspcafe.post.model.Post;
import org.example.jspcafe.post.service.PostService;

import java.io.IOException;

@WebServlet(name = "postEditServlet", value = "/post/edit/*")
public class PostEditServlet extends HttpServlet {

    private PostService postService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.postService = ApplicationContext.getContainer().resolve(PostService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Boolean isLogined = (Boolean) session.getAttribute("isLogined");
        Long userId = (Long) session.getAttribute("userId");

        if (isLogined == null || !isLogined) {
            resp.sendRedirect("/login");
            return;
        }

        String pathInfo = req.getPathInfo();
        Long postId = Long.parseLong(pathInfo.substring(1)); // "/{postId}"에서 postId 추출

        Post post = postService.getPostById(postId);


        if (!post.canModifyBy(userId)) {
            req.setAttribute("errorMessage", "수정 권한이 없습니다.");
            resp.sendRedirect("/post/post.jsp");
            return;
        }

        req.setAttribute("post", post);
        req.getRequestDispatcher("/WEB-INF/jsp/post/edit.jsp").forward(req, resp);
    }

//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        HttpSession session = req.getSession();
//        Boolean isLogined = (Boolean) session.getAttribute("isLogined");
//        Long userId = (Long) session.getAttribute("userId");
//
//        if (isLogined == null || !isLogined) {
//            resp.sendRedirect("/login");
//            return;
//        }
//
//        Long postId = Long.parseLong(req.getParameter("postId"));
//        String title = req.getParameter("title");
//        String content = req.getParameter("content");
//
//        PostModifyRequest request = new PostModifyRequest(userId, postId, title, content);
//        postService.updatePost(request);
//
//        resp.sendRedirect("/posts/" + postId);
//    }
}
