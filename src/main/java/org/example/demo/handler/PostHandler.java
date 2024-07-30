package org.example.demo.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.domain.Post;
import org.example.demo.exception.NotFoundExceptoin;
import org.example.demo.model.PostCreateDao;
import org.example.demo.repository.PostRepository;
import org.example.demo.validator.AuthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class PostHandler {
    private static final Logger logger = LoggerFactory.getLogger(PostHandler.class);
    private final PostRepository postRepository;
    private final AuthValidator authValidator;

    public PostHandler(PostRepository postRepository, AuthValidator authValidator) {
        this.postRepository = postRepository;
        this.authValidator = authValidator;
    }

    public void handleGetPost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        authValidator.checkLoggedIn(request, response);
        Long postId = Long.parseLong(pathVariables.get(0));
        Post post = checkPostExistence(postId);

        request.setAttribute("post", post);
        request.getRequestDispatcher("/WEB-INF/post/show.jsp").forward(request, response);
    }

    public void handleCreatePost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException, ServletException {
        authValidator.checkLoggedIn(request, response);

        Long writer = getUserIdFromSession(request);
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");

        postRepository.addPost(new PostCreateDao(writer, title, contents));

        response.sendRedirect("/");
    }

    public void handleEditPost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        authValidator.checkLoggedIn(request, response);
        Long postId = Long.parseLong(pathVariables.get(0));
        Post post = checkPostExistence(postId);
        Long userId = getUserIdFromSession(request);

        authValidator.checkIdenticalUser(request, userId, post);

        request.setAttribute("post", post);
        request.getRequestDispatcher("/WEB-INF/post/edit.jsp").forward(request, response);
    }

    public void handleUpdatePost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException, ServletException {
        authValidator.checkLoggedIn(request, response);
        Long postId = Long.parseLong(pathVariables.get(0));
        Post post = checkPostExistence(postId);
        Long userId = getUserIdFromSession(request);

        authValidator.checkIdenticalUser(request, userId, post);

        String title = request.getParameter("title");
        String contents = request.getParameter("contents");
        postRepository.updatePost(postId, title, contents);

        response.sendRedirect("/posts/" + postId);
    }

    public void handleDeletePost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException, ServletException {
        authValidator.checkLoggedIn(request, response);
        Long postId = Long.parseLong(pathVariables.get(0));
        Post post = checkPostExistence(postId);
        Long userId = getUserIdFromSession(request);

        authValidator.checkIdenticalUser(request, userId, post);

        postRepository.deletePost(postId);

        response.sendRedirect("/");
    }

    public void handlePostForm(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/post/form.jsp").forward(request, response);
    }

    private Post checkPostExistence(Long postId) {
        return postRepository.getPost(postId).orElseThrow(() -> new NotFoundExceptoin("Post not found"));
    }

    private Long getUserIdFromSession(HttpServletRequest request) {
        return (Long) request.getSession().getAttribute("user");
    }
}
