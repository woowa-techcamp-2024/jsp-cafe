package org.example.jspcafe.post.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.di.ApplicationContext;
import org.example.jspcafe.post.request.PostCreateRequest;
import org.example.jspcafe.post.response.PostListResponse;
import org.example.jspcafe.post.service.PostService;

import java.io.IOException;

@WebServlet(name = "PostsServlet", urlPatterns = "/api/posts")
public class PostServlet extends HttpServlet {

    private PostService postService;
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;

    @Override
    public void init() throws ServletException {
        super.init();
        this.postService = ApplicationContext.getContainer().resolve(PostService.class);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long userId = (Long) req.getSession().getAttribute("userId");

        String title = req.getParameter("title");
        String content = req.getParameter("content");

        PostCreateRequest request = new PostCreateRequest(userId, title, content);

        try {
            postService.createPost(request);
            resp.sendRedirect("/");
        } catch (Exception e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/create-post.jsp").forward(req, resp);
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer page = DEFAULT_PAGE;
        Integer size = DEFAULT_SIZE;

        String pageParam = req.getParameter("page");
        if (pageParam != null) {
            page = Integer.parseInt(pageParam);
        }

        String sizeParam = req.getParameter("size");
        if (sizeParam != null) {
            size = Integer.parseInt(sizeParam);
        }

        PostListResponse postListResponse = postService.getPosts(page, size);

        req.setAttribute("posts", postListResponse);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", (int) Math.ceil((double) postListResponse.totalElements() / size));

        req.getRequestDispatcher("/post/posts.jsp").include(req, resp);
    }
}
