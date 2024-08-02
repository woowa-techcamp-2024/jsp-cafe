package com.woowa.hyeonsik.application.servlet;

import com.woowa.hyeonsik.application.domain.Reply;
import com.woowa.hyeonsik.application.domain.User;
import com.woowa.hyeonsik.application.exception.LoginRequiredException;
import com.woowa.hyeonsik.application.service.CommentService;
import com.woowa.hyeonsik.application.util.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CommentServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(CommentServlet.class);
    private final CommentService commentService;

    public CommentServlet(CommentService commentService) {
        this.commentService = commentService;
    }

    // add comment (POST /comments)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("댓글을 작성합니다.");
        Map<String, Object> parse = JsonParser.parse(request.getReader());
        Long articleId = Long.valueOf((Integer) parse.get("articleId"));
        String contents = (String) parse.get("contents");

        // 세션이 존재하는지 확인
        final HttpSession session = request.getSession(false);
        final User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            throw new LoginRequiredException("로그인이 필요한 작업입니다.");
        }

        // 세션에서 글쓴이 정보를 가져온다.
        User user = (User) request.getSession(false).getAttribute("user");
        String writer = user.getUserId();

        // 댓글 작성
        Reply reply = new Reply(null, articleId, writer, contents);
        commentService.addComment(reply);
    }

    // read comments (GET /comments)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.debug("게시글에 댓글을 조회합니다.");
        Long articleId = Long.valueOf(request.getParameter("articleId"));
        List<Reply> comments = commentService.findAllByArticleId(articleId);

        // JSON 형식으로 댓글 목록 반환
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonComments = convertCommentsToJson(comments);
        response.getWriter().write(jsonComments);
    }

    // delete comment (DELETE /comments/{id})
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 게시글 ID 가져오기
        String requestURI = request.getRequestURI().substring(getServletContext().getContextPath().length() + "/comments/".length());
        String[] split = requestURI.split("/");
        long commentId = Long.parseLong(split[0]);
        logger.debug("댓글을 삭제합니다. commentID: {}", commentId);

        // 세션이 존재하는지 확인
        final HttpSession session = request.getSession(false);
        final User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            throw new LoginRequiredException("로그인이 필요한 작업입니다.");
        }

        // 세션에서 글쓴이 정보를 가져온다.
        User user = (User) request.getSession(false).getAttribute("user");
        String writer = user.getUserId();

        // 댓글 삭제
        commentService.deleteComment(commentId, writer);
    }

    // PUT comment (PUT /comments/{id})
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 게시글 ID 가져오기
        String requestURI = request.getRequestURI().substring(getServletContext().getContextPath().length() + "/comments/".length());
        String[] split = requestURI.split("/");
        long commentId = Long.parseLong(split[0]);

        // 게시글 정보 JSON 가져오기
        Map<String, Object> parse = JsonParser.parse(request.getReader());
        Long articleId = Long.valueOf((Integer) parse.get("articleId"));
        String contents = (String) parse.get("contents");
        logger.debug("댓글을 수정합니다. articleID: {}, commentId: {}, contents: {}", articleId, commentId, contents);

        // 세션이 존재하는지 확인
        final HttpSession session = request.getSession(false);
        final User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) {
            throw new LoginRequiredException("로그인이 필요한 작업입니다.");
        }

        // 세션에서 글쓴이 정보를 가져온다.
        User user = (User) request.getSession(false).getAttribute("user");
        String writer = user.getUserId();

        // 댓글 수정
        Reply reply = new Reply(commentId, articleId, writer, contents);
        commentService.updateComment(reply, writer);
    }

    private String convertCommentsToJson(List<Reply> comments) {
        // JSON 라이브러리를 사용하여 List<Reply>를 JSON 문자열로 변환
        // 예를 들어, Gson 라이브러리를 사용한다면:
        // Gson gson = new Gson();
        // return gson.toJson(comments);

        // 여기서는 간단한 예시로 직접 JSON 형식의 문자열을 만들어 반환합니다.
        StringBuilder jsonBuilder = new StringBuilder("[");
        for (int i = 0; i < comments.size(); i++) {
            Reply reply = comments.get(i);
            jsonBuilder.append("{");
            jsonBuilder.append("\"id\":").append(reply.getId()).append(",");
            jsonBuilder.append("\"articleId\":").append(reply.getArticleId()).append(",");
            jsonBuilder.append("\"writer\":\"").append(reply.getWriter()).append("\",");
            jsonBuilder.append("\"contents\":\"").append(reply.getContents().replace("\"", "\\\"")).append("\",");
            jsonBuilder.append("\"createdAt\":\"").append(reply.getCreatedAt()).append("\"");
            jsonBuilder.append("}");
            if (i < comments.size() - 1) {
                jsonBuilder.append(",");
            }
        }
        jsonBuilder.append("]");
        return jsonBuilder.toString();
    }
}
