package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.dto.ReplyCreateDto;
import org.example.service.ReplyService;
import org.example.util.BodyParser;
import org.example.util.LoggerUtil;
import org.example.util.SessionUtil;
import org.json.JSONObject;
import org.slf4j.Logger;

@WebServlet("/reply")
public class ReplyServlet extends HttpServlet {

    private final Logger logger = LoggerUtil.getLogger();
    private final ReplyService replyService = new ReplyService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 댓글 생성
        String userId = SessionUtil.extractUserId(request)
            .orElseThrow(() -> new IllegalArgumentException("로그인이 필요합니다."));
        logger.info("userId: {}", userId);

        String bodyString = BodyParser.stringBody(request);
        JSONObject body = new JSONObject(bodyString);

        int articleId = body.getInt("articleId");
        String content = body.getString("content");
        logger.info("articleId: {}, content: {}", articleId, content);
        replyService.createReply(new ReplyCreateDto(content, userId, articleId));
        response.setStatus(HttpServletResponse.SC_CREATED);
    }
}
