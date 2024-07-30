package cafe.controller.handler.comments;

import cafe.controller.handler.Handler;
import cafe.service.CommentService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommentDeleteHandler implements Handler {
    private final CommentService commentService;

    public CommentDeleteHandler(ServletContext servletContext) {
        commentService = (CommentService) servletContext.getAttribute("commentService");
    }

    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        Handler.super.doDelete(req, resp);
        //TODO: 댓글 삭제
    }
}
