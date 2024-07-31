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

    public static boolean isLoggedIn(HttpServletRequest request) {
        return request.getSession(false).getAttribute("user") != null;
    }

    public static boolean isIdenticalUser(Long userId, Post post) {
        return userId.equals(post.getWriter().getId());
    }

    public static boolean isIdenticalUser(Long userId, Comment comment) {
        return userId.equals(comment.getWriter().getId());
    }
}
