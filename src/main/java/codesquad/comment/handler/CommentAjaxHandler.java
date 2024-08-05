package codesquad.comment.handler;

import codesquad.comment.service.DeleteCommentService;
import codesquad.common.exception.NoSuchElementException;
import codesquad.common.exception.UnauthorizedRequestException;
import codesquad.common.handler.HttpServletRequestHandler;
import codesquad.common.handler.ReturnType;
import codesquad.common.handler.annotation.RequestMapping;
import codesquad.common.handler.annotation.Response;
import codesquad.user.domain.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Response(returnType = ReturnType.JSON)
@RequestMapping("/questions/\\d+/answers/\\d+")
public class CommentAjaxHandler extends HttpServletRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommentAjaxHandler.class);

    private final DeleteCommentService deleteCommentService;

    public CommentAjaxHandler(DeleteCommentService deleteCommentService) {
        this.deleteCommentService = deleteCommentService;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("pathInfo {}", req.getPathInfo());
        long articleId = extractArticleIdFromPath(req);
        long commentId = extractCommentIdFromPath(req);
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        DeleteCommentService.Command command = new DeleteCommentService.Command(commentId, loginUser.getUserId());
        try {
            deleteCommentService.delete(command);
        } catch (NoSuchElementException | UnauthorizedRequestException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    private long extractArticleIdFromPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || !pathInfo.startsWith("/")) {
            throw new NumberFormatException("Invalid path info");
        }
        return Long.parseLong(pathInfo.substring("/questions/".length(), pathInfo.indexOf("/answers")));
    }

    private long extractCommentIdFromPath(HttpServletRequest req) throws NumberFormatException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || !pathInfo.startsWith("/")) {
            throw new NumberFormatException("Invalid path info");
        }
        int startIdx = pathInfo.indexOf("/answers");
        return Long.parseLong(pathInfo.substring(startIdx + "/answers/".length()));
    }
}
