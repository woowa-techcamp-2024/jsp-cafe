package codesquad.comment.handler;

import codesquad.comment.service.RegisterCommentService;
import codesquad.common.exception.NoSuchElementException;
import codesquad.common.handler.HttpServletRequestHandler;
import codesquad.common.handler.ReturnType;
import codesquad.common.handler.annotation.Response;
import codesquad.common.http.response.ApiResponse;
import codesquad.global.dao.ArticleQuery;
import codesquad.global.servlet.annotation.RequestMapping;
import codesquad.user.domain.User;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Response(returnType = ReturnType.JSON)
@RequestMapping("/questions/\\d+/answers")
public class CommentsAjaxHandler extends HttpServletRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(CommentsAjaxHandler.class);

    private final RegisterCommentService registerCommentService;

    public CommentsAjaxHandler(RegisterCommentService registerCommentService) {
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
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        RegisterCommentService.Command command = new RegisterCommentService.Command(articleId, loginUser.getUserId(), content);
        Long registeredId;
        try {
            registeredId = registerCommentService.register(command);
        } catch (NoSuchElementException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        ArticleQuery.CommentResponse data = new ArticleQuery.CommentResponse(registeredId, loginUser.getId(), loginUser.getUserId(), content);
        ApiResponse<ArticleQuery.CommentResponse> response = new ApiResponse<>(200, "OK", data);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String responseStr = objectMapper.writeValueAsString(response);
        objectMapper.writeValue(resp.getWriter(), responseStr);
    }

    private long extractIdFromPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || !pathInfo.startsWith("/")) {
            throw new NumberFormatException("Invalid path info");
        }
        if (pathInfo.endsWith("/answers")) {
            return Long.parseLong(pathInfo.substring(1, pathInfo.indexOf("/answers")));
        }
        return Long.parseLong(pathInfo.substring(1));
    }
}
