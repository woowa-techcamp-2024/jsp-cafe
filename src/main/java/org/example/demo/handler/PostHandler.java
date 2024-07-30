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
import java.util.Optional;

public class PostHandler {
    private static final Logger logger = LoggerFactory.getLogger(PostHandler.class);
    private static PostHandler instance;
    private final PostRepository postRepository;
    private final AuthValidator authValidator;

    private PostHandler() {
        postRepository = PostRepository.getInstance();
        authValidator = AuthValidator.getInstance();
    }

    public static PostHandler getInstance() {
        if (instance == null) {
            instance = new PostHandler();
        }
        return instance;
    }

    public void handleGetPost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        Long postId = Long.parseLong(pathVariables.get(0));
        Post post = postRepository.getPost(postId).orElseThrow(() -> new NotFoundExceptoin("Post not found"));

        authValidator.checkLoggedIn(request, response);

        request.setAttribute("post", post);
        request.getRequestDispatcher("/WEB-INF/post/show.jsp").forward(request, response);
    }

    public void handleCreatePost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException, ServletException {
        authValidator.checkLoggedIn(request, response);

        String writer = (String) request.getSession().getAttribute("user");
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");

        postRepository.addPost(new PostCreateDao(writer, title, contents));

        response.sendRedirect("/");
    }

    public void handleEditPost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        authValidator.checkLoggedIn(request, response);

        Long id = Long.parseLong(pathVariables.get(0));
        Post post = postRepository.getPost(id).orElseThrow(() -> new NotFoundExceptoin("Post not found"));
        Long userId = (Long) request.getSession().getAttribute("user");

        authValidator.checkIdenticalUser(request, userId, post);

        request.setAttribute("post", post);
        request.getRequestDispatcher("/WEB-INF/post/edit.jsp").forward(request, response);
    }

    public void handleUpdatePost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException {
        Long id = Long.parseLong(pathVariables.get(0));
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");

        Post post = postRepository.getPost(id).orElseThrow(() -> new NotFoundExceptoin("Post not found"));
        Long userId = (Long) request.getSession().getAttribute("user");

        authValidator.checkIdenticalUser(request, userId, post);

        postRepository.updatePost(id, title, contents);

        response.sendRedirect("/posts/" + id);
    }

    public void handleDeletePost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException, ServletException {
        authValidator.checkLoggedIn(request, response);

        Long id = Long.parseLong(pathVariables.get(0));
        Post post = postRepository.getPost(id).orElseThrow(() -> new NotFoundExceptoin("Post not found"));
        Long userId = (Long) request.getSession().getAttribute("user");

        authValidator.checkIdenticalUser(request, userId, post);

        postRepository.deletePost(id);

        response.sendRedirect("/");
    }

    public void handlePostForm(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/post/form.jsp").forward(request, response);
    }
}
