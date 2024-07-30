package org.example.demo.validator;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.domain.Comment;
import org.example.demo.domain.Post;
import org.example.demo.exception.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AuthValidator {
    private static final Logger logger = LoggerFactory.getLogger(AuthValidator.class);

    public void checkLoggedIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!isLoggedIn(request)) {
            logger.warn("User not logged in");
            request.setAttribute("error", "User not logged in");
            request.getRequestDispatcher("/WEB-INF/user/login.jsp").forward(request, response);

            throw new UnauthorizedException("User not logged in");
        }
    }

    public void checkIdenticalUser(HttpServletRequest request, Long userId, Post post) {
        if (!isIdenticalUser(userId, post)) {
            logger.warn("User not authorized");
            request.setAttribute("error", "User not authorized");

            throw new UnauthorizedException("User not authorized");
        }
    }

    public void checkIdenticalUser(HttpServletRequest request, Long userId, Comment comment) {
        if (!isIdenticalUser(userId, comment)) {
            logger.warn("User not authorized");
            request.setAttribute("error", "User not authorized");

            throw new UnauthorizedException("User not authorized");
        }
    }

    private boolean isLoggedIn(HttpServletRequest request) {
        return request.getSession().getAttribute("user") != null;
    }

    private boolean isIdenticalUser(Long userId, Post post) {
        return userId.equals(post.getWriter().getId());
    }

    private boolean isIdenticalUser(Long userId, Comment comment) {
        return userId.equals(comment.getWriter().getId());
    }
}
