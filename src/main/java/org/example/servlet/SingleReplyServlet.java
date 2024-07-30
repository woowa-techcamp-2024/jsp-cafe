package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.service.ReplyService;
import org.example.util.BodyParser;
import org.example.util.LoggerUtil;
import org.example.util.SessionUtil;
import org.json.JSONObject;
import org.slf4j.Logger;

@WebServlet("/reply/*")
public class SingleReplyServlet extends HttpServlet {

     private final Logger logger =  LoggerUtil.getLogger();
     private final ReplyService replyService = new ReplyService();

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 댓글 삭제
        String userId = SessionUtil.extractUserId(request)
            .orElseThrow(() -> new IllegalArgumentException("로그인이 필요합니다."));
        logger.info("userId: {}", userId);
        int replyId = Integer.parseInt(getArticleIdFromPath(request.getRequestURI()));

        replyService.deleteReply(replyId, userId);
    }

    private String getArticleIdFromPath(String path) {
        if (path == null || path.isBlank()) {
            throw new IllegalArgumentException("잘못된 경로입니다.");
        }

        String[] pathSplit = path.split("/");
        return pathSplit[pathSplit.length - 1];
    }
}
