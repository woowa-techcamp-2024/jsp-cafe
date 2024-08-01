package org.example.demo.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.domain.Comment;
import org.example.demo.exception.UnauthorizedException;
import org.example.demo.model.CommentCreateDao;
import org.example.demo.repository.CommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.example.demo.validator.AuthValidator.isIdenticalUser;

public class CommentHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommentHandler.class);
    private CommentRepository commentRepository;
    private ObjectMapper objectMapper;

    public CommentHandler(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public void createComment(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) {
        Long postId = Long.valueOf(pathVariables.get(0));
        String contents = request.getParameter("contents");

        Comment comment = commentRepository.saveComment(new CommentCreateDao(
                postId,
                (Long) request.getSession(false).getAttribute("user"),
                contents
        ));

        // 업데이트된 comment 반환
        List<Comment> updatedComments = new ArrayList<>();
        updatedComments.add(comment);
        updatedComments.addAll(getUpdatedComments(postId, comment.getId()));

        logger.info("Comment created: {}", comment);
        logger.info("Updated comments: {}", updatedComments);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().write(objectMapper.writeValueAsString(updatedComments));
        } catch (Exception e) {
            logger.error("Error writing response", e);
        }
    }

    public void deleteComment(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) {
        Long commentId = Long.parseLong(pathVariables.get(1));
        Comment comment = commentRepository.getComment(commentId).orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!isIdenticalUser(getUserId(request), comment)) {
            request.setAttribute("error", "User not authorized");
            throw new UnauthorizedException("User not authorized");
        }

        commentRepository.deleteComment(commentId);

        response.setContentType("application/json");
        logger.info("Comment deleted: {}", commentId);
    }

    public void updateComment(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) {
        Long commentId = Long.parseLong(pathVariables.get(1));
        String contents = request.getParameter("contents");
        Comment comment = commentRepository.getComment(commentId).orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        if (!isIdenticalUser(getUserId(request), comment)) {
            request.setAttribute("error", "User not authorized");
            throw new UnauthorizedException("User not authorized");
        }

        commentRepository.updateComment(commentId, contents);
    }

    private static Long getUserId(HttpServletRequest request) {
        return (Long) request.getSession(false).getAttribute("user");
    }

    private List<Comment> getUpdatedComments(Long postId, Long lastCommentId) {
        return commentRepository.getMoreComments(postId, lastCommentId);
    }
}
