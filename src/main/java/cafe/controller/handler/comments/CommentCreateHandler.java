package cafe.controller.handler.comments;

import cafe.controller.handler.Handler;
import cafe.service.CommentService;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommentCreateHandler implements Handler {
    private final CommentService commentService;

    public CommentCreateHandler(ServletContext context) {
        this.commentService = (CommentService) context.getAttribute("commentService");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        Handler.super.doPost(req, resp);
        //TODO: 댓글 추가
    }
}
