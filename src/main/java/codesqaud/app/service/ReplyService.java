package codesqaud.app.service;

import codesqaud.app.AuthenticationManager;
import codesqaud.app.dao.reply.ReplyDao;
import codesqaud.app.dto.ReplyDto;
import codesqaud.app.exception.HttpException;
import codesqaud.app.model.Reply;
import codesqaud.app.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;

import static jakarta.servlet.http.HttpServletResponse.*;

public class ReplyService {
    private final ReplyDao replyDao;

    public ReplyService(ReplyDao replyDao) {
        this.replyDao = replyDao;
    }

    /*
       POST Handlers
     */
    public void handleReplyCreation(HttpServletRequest req, HttpServletResponse resp, Long articleId) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = req.getInputStream();
        JsonNode jsonNode = objectMapper.readTree(inputStream);

        String contents = jsonNode.get("contents").asText();

        User loginUser = AuthenticationManager.getLoginUserOrElseThrow(req);
        Reply reply = new Reply(contents, articleId, loginUser.getId());
        replyDao.save(reply);

        ReplyDto replyDto = replyDao.findByIdAsDto(reply.getId()).orElseThrow(() -> new HttpException(SC_INTERNAL_SERVER_ERROR));

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_CREATED);

        String jsonResponse = objectMapper.writeValueAsString(replyDto);
        resp.getWriter().write(jsonResponse);
    }

    /*
        DELETE Handlers
     */
    public void handle(HttpServletRequest req, HttpServletResponse resp, Long articleId, Long replyId) throws IOException {
        Reply reply = replyDao.findById(replyId).orElseThrow(
                () -> new HttpException(SC_NOT_FOUND)
        );

        if (!AuthenticationManager.isMe(req, reply.getAuthorId())) {
            throw new HttpException(SC_FORBIDDEN, "본인이 작성한 댓글만 삭제할 수 있습니다.");
        }

        replyDao.delete(reply);
        resp.sendRedirect("/qna/" + articleId);
    }
}
