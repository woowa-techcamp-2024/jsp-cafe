package cafe.controller.handler.comments;

import cafe.controller.handler.Handler;
import cafe.domain.entity.User;
import cafe.service.CommentService;
import cafe.service.SessionService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommentDeleteHandler implements Handler {
    private final CommentService commentService;
    private final SessionService sessionService;

    public CommentDeleteHandler(ServletContext servletContext) {
        commentService = (CommentService) servletContext.getAttribute("commentService");
        sessionService = (SessionService) servletContext.getAttribute("sessionService");
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        String session = req.getSession(true).getId();
        User user = sessionService.findUserBySession(session);
        commentService.verifyCommentId(user, req.getRequestURI());
        commentService.deleteById(req.getRequestURI());
    }
}
