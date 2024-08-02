package cafe.controller.handler.comments;

import cafe.controller.handler.Handler;
import cafe.domain.entity.Comment;
import cafe.service.CommentService;
import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CommentListHandler implements Handler {
    private final CommentService commentService;

    public CommentListHandler(ServletContext context) {
        this.commentService = (CommentService) context.getAttribute("commentService");
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        List<Comment> comments = commentService.findCommentsByArticleId(req.getRequestURI());
        Map<String, List<Comment>> commentMap = Map.of("comments", comments);
        String jsonComments = new Gson().toJson(commentMap);
        resp.getWriter().print(jsonComments);
    }
}
