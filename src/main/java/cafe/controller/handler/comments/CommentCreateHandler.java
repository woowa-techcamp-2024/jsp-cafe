package cafe.controller.handler.comments;

import cafe.controller.handler.Handler;
import cafe.domain.entity.User;
import cafe.dto.CommentDto;
import cafe.service.CommentService;
import cafe.service.SessionService;
import cafe.util.JsonUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CommentCreateHandler implements Handler {
    private final CommentService commentService;
    private final SessionService sessionService;

    public CommentCreateHandler(ServletContext context) {
        this.commentService = (CommentService) context.getAttribute("commentService");
        this.sessionService = (SessionService) context.getAttribute("sessionService");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        String jsonData = JsonUtil.readJson(req.getReader());
        CommentDto comment = new Gson().fromJson(jsonData, CommentDto.class);
        User user = sessionService.findUserBySession(req.getSession(true).getId());
        commentService.save(req.getRequestURI(), comment.getComments(), user.getUserId());
    }
}
