package org.example.demo.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.demo.model.CommentCreateDao;
import org.example.demo.repository.CommentRepository;
import org.example.demo.validator.AuthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommentHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommentHandler.class);
    private CommentRepository commentRepository;
    private AuthValidator authValidator;

    public CommentHandler(CommentRepository commentRepository, AuthValidator authValidator) {
        this.commentRepository = commentRepository;
        this.authValidator = authValidator;
    }

    public void createComment(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) {
        Long postId = Long.parseLong(request.getParameter("postId"));
        Long writerId = Long.parseLong(request.getParameter("writerId"));
        String contents = request.getParameter("contents");

        commentRepository.saveComment(new CommentCreateDao(
                postId,
                writerId,
                contents
        ));
    }

    public void deleteComment(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) {
        Long commentId = Long.parseLong(pathVariables.get(1));

        authValidator.checkIdenticalUser(request, getUser(request), commentRepository.getComment(commentId).get());
        commentRepository.deleteComment(commentId);
    }

    public void updateComment(HttpServletRequest request, HttpServletResponse response, List<String> pathVariables) {
        Long commentId = Long.parseLong(pathVariables.get(1));
        String contents = request.getParameter("contents");

        authValidator.checkIdenticalUser(request, getUser(request), commentRepository.getComment(commentId).get());
        commentRepository.updateComment(commentId, contents);
    }

    private static Long getUser(HttpServletRequest request) {
        return (Long) request.getSession().getAttribute("user");
    }
}
