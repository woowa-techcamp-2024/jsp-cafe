package codesquad.comment.handler;

import codesquad.comment.domain.vo.Status;
import codesquad.comment.handler.dto.request.CommentQueryRequest;
import codesquad.comment.handler.dto.response.CommentResponse;
import codesquad.comment.handler.dto.response.PagedCommentResponse;
import codesquad.comment.service.QueryCommentService;
import codesquad.comment.service.RegisterCommentService;
import codesquad.common.exception.NoSuchElementException;
import codesquad.common.handler.HttpServletRequestHandler;
import codesquad.common.handler.ReturnType;
import codesquad.common.handler.annotation.RequestMapping;
import codesquad.common.handler.annotation.Response;
import codesquad.common.http.response.ApiResponse;
import codesquad.common.http.response.JsonWriter;
import codesquad.user.domain.User;
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

    private final QueryCommentService queryCommentService;
    private final RegisterCommentService registerCommentService;

    public CommentsAjaxHandler(QueryCommentService queryCommentService, RegisterCommentService registerCommentService) {
        this.queryCommentService = queryCommentService;
        this.registerCommentService = registerCommentService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("requesting comment list");
        long articleId;
        try {
            articleId = extractIdFromPath(req);
        } catch (NumberFormatException e) {
            req.setAttribute("errorMsg", "올바르지 않은 요청입니다.");
            req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
            return;
        }
        String pageNumber = req.getParameter("pageNumber");
        String pageSize = req.getParameter("pageSize");
        if (pageNumber == null || pageNumber.isEmpty() || pageSize == null || pageSize.isEmpty()) {
            pageNumber = "1";
            pageSize = "5";
        }
        CommentQueryRequest query = new CommentQueryRequest(Integer.parseInt(pageNumber), Integer.parseInt(pageSize), articleId, Status.COMMENTED);
        PagedCommentResponse<CommentResponse> data = queryCommentService.findByArticleId(query);
        ApiResponse<PagedCommentResponse<CommentResponse>> returnValue = new ApiResponse<>(200, "OK", data);
        JsonWriter.writeJson(resp, returnValue);
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
        CommentResponse data = new CommentResponse(registeredId, loginUser.getId(), loginUser.getUserId(), content);
        ApiResponse<CommentResponse> returnValue = new ApiResponse<>(200, "OK", data);
        JsonWriter.writeJson(resp, returnValue);
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
