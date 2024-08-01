package org.example.demo.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.domain.Comment;
import org.example.demo.domain.Post;
import org.example.demo.exception.NotFoundExceptoin;
import org.example.demo.exception.UnauthorizedException;
import org.example.demo.model.PostCreateDao;
import org.example.demo.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static org.example.demo.validator.AuthValidator.isIdenticalUser;
import static org.example.demo.validator.AuthValidator.isLoggedIn;

public class PostHandler {
    private static final Logger logger = LoggerFactory.getLogger(PostHandler.class);
    private final PostRepository postRepository;

    public PostHandler(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    private static boolean isIdentical(Comment comment, Long userId) {
        return comment.getWriter().getId().equals(userId);
    }

    public void handleGetPost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        if (!isLoggedIn(request)) {
            request.setAttribute("error", "User not logged in");
            request.getRequestDispatcher("/WEB-INF/user/login.jsp").forward(request, response);
            return;
        }

        Long postId = Long.parseLong(pathVariables.get(0));
        Post post = checkPostExistence(postId);

        request.setAttribute("post", post);
        request.setAttribute("comments", post.getComments());
        request.getRequestDispatcher("/WEB-INF/post/show.jsp").forward(request, response);
    }

    public void handleCreatePost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException, ServletException {
        if (!isLoggedIn(request)) {
            request.setAttribute("error", "User not logged in");
            request.getRequestDispatcher("/WEB-INF/user/login.jsp").forward(request, response);
            return;
        }

        Long writer = getUserIdFromSession(request);
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");

        postRepository.addPost(new PostCreateDao(writer, title, contents));

        response.sendRedirect("/");
    }

    public void handleEditPost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws ServletException, IOException {
        if (!isLoggedIn(request)) {
            request.setAttribute("error", "User not logged in");
            request.getRequestDispatcher("/WEB-INF/user/login.jsp").forward(request, response);
            return;
        }

        Long postId = Long.parseLong(pathVariables.get(0));
        Post post = checkPostExistence(postId);
        Long userId = getUserIdFromSession(request);

        if (!isIdenticalUser(userId, post)) {
            request.setAttribute("error", "User not authorized");
            throw new UnauthorizedException("User not authorized");
        }

        request.setAttribute("post", post);
        request.getRequestDispatcher("/WEB-INF/post/edit.jsp").forward(request, response);
    }

    public void handleUpdatePost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException, ServletException {
        if (!isLoggedIn(request)) {
            request.setAttribute("error", "User not logged in");
            request.getRequestDispatcher("/WEB-INF/user/login.jsp").forward(request, response);
            return;
        }

        Long postId = Long.parseLong(pathVariables.get(0));
        Post post = checkPostExistence(postId);
        Long userId = getUserIdFromSession(request);

        if (!isIdenticalUser(userId, post)) {
            request.setAttribute("error", "User not authorized");
            throw new UnauthorizedException("User not authorized");
        }

        String title = request.getParameter("title");
        String contents = request.getParameter("contents");
        postRepository.updatePost(postId, title, contents);

        response.sendRedirect("/posts/" + postId);
    }

    public void handleDeletePost(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) throws IOException, ServletException {
        if (!isLoggedIn(request)) {
            request.setAttribute("error", "User not logged in");
            request.getRequestDispatcher("/WEB-INF/user/login.jsp").forward(request, response);
            return;
        }

        Long postId = Long.parseLong(pathVariables.get(0));
        Post post = checkPostExistence(postId);
        Long userId = getUserIdFromSession(request);

        if (!isIdenticalUser(userId, post)) {
            request.setAttribute("error", "User not authorized");
            throw new UnauthorizedException("User not authorized");
        }

        List<Comment> comments = postRepository.getPost(postId).get().getComments();

        // 모든 댓글이 작성자 본인이 쓴 댓글인 경우에만 삭제 가능
        if (comments.stream().anyMatch(comment -> !isIdentical(comment, userId))) {
            request.setAttribute("error", "다른 사람이 쓴 댓글이 존재합니다.");
            throw new IllegalArgumentException("다른 사람이 쓴 댓글이 존재");
        }

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
        return (Long) request.getSession(false).getAttribute("user");
    }
}
