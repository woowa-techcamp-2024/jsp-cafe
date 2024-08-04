package codesquad.comment.handler;

import codesquad.comment.service.RegisterCommentService;
import codesquad.common.exception.NoSuchElementException;
import codesquad.common.handler.HttpServletRequestHandler;
import codesquad.common.handler.annotation.Response;
import codesquad.common.handler.annotation.RequestMapping;
import codesquad.user.domain.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Response
@RequestMapping("/questions/\\d+/answers")
public class CommentsHandler extends HttpServletRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommentsHandler.class);

    private final RegisterCommentService registerCommentService;

    public CommentsHandler(RegisterCommentService registerCommentService) {
        this.registerCommentService = registerCommentService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("requesting comment register");
        long articleId;
        String content = req.getParameter("contents");
        User loginUser = (User) req.getSession().getAttribute("loginUser");
        try {
            articleId = extractIdFromPath(req);
        } catch (NumberFormatException e) {
            req.setAttribute("errorMsg", "올바르지 않은 요청입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        RegisterCommentService.Command command = new RegisterCommentService.Command(articleId, loginUser.getUserId(), content);
        try {
            registerCommentService.register(command);
        } catch (NoSuchElementException e) {
            req.setAttribute("errorMsg", "존재하지 않는 글입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/questions/" + articleId);
    }

    private long extractIdFromPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || !pathInfo.startsWith("/")) {
            throw new NumberFormatException("Invalid path info");
        }
        if (pathInfo.endsWith("/answers")) {
            return Long.parseLong(pathInfo.substring("/questions/".length(), pathInfo.indexOf("/answers")));
        }
        return Long.parseLong(pathInfo.substring("/questions/".length()));
    }
}
