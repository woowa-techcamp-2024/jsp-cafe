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

@WebServlet(name = "PostEditPageServlet", value = "/post/edit/*")
public class PostEditPageServlet extends HttpServlet {

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
            req.getSession().setAttribute("errorMessage", "수정 권한이 없습니다.");
            resp.sendRedirect("/posts/" + postId);
            return;
        }

        req.setAttribute("post", post);
        req.getRequestDispatcher("/WEB-INF/jsp/post/edit.jsp").forward(req, resp);
    }

//
}
