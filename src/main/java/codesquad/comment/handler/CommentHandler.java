package codesquad.comment.handler;

import codesquad.comment.service.DeleteCommentService;
import codesquad.common.exception.NoSuchElementException;
import codesquad.common.exception.UnauthorizedRequestException;
import codesquad.common.handler.HttpServletRequestHandler;
import codesquad.common.handler.annotation.Response;
import codesquad.global.servlet.annotation.RequestMapping;
import codesquad.user.domain.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Response
@RequestMapping("/questions/\\d+/answers/\\d+")
public class CommentHandler extends HttpServletRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommentHandler.class);

    private final DeleteCommentService deleteCommentService;

    public CommentHandler(DeleteCommentService deleteCommentService) {
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
        } catch (NoSuchElementException e) {
            HttpServletRequestWrapper request = new HttpServletRequestWrapper(req) {
                @Override
                public String getMethod() {
                    return "GET";
                }
            };
            request.setAttribute("errorMsg", "존재하지 않는 댓글입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(request, resp);
            return;
        } catch (UnauthorizedRequestException e) {
            HttpServletRequestWrapper request = new HttpServletRequestWrapper(req) {
                @Override
                public String getMethod() {
                    return "GET";
                }
            };
            request.setAttribute("errorMsg", "다른 사람의 댓글을 삭제할 수 없습니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(request, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/questions/" + articleId);
    }

    private long extractArticleIdFromPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || !pathInfo.startsWith("/")) {
            throw new NumberFormatException("Invalid path info");
        }
        return Long.parseLong(pathInfo.substring("/".length(), pathInfo.indexOf("/answers")));
    }

    private long extractCommentIdFromPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || !pathInfo.startsWith("/")) {
            throw new NumberFormatException("Invalid path info");
        }
        int startIdx = pathInfo.indexOf("/answers");
        return Long.parseLong(pathInfo.substring(startIdx + "/answers/".length()));
    }
}
