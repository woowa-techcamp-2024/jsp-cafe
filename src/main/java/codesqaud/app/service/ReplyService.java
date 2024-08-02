package codesqaud.app.service;

import codesqaud.app.AuthenticationManager;
import codesqaud.app.dao.reply.ReplyDao;
import codesqaud.app.exception.HttpException;
import codesqaud.app.model.Reply;
import codesqaud.app.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class ReplyService {
    private final ReplyDao replyDao;

    public ReplyService(ReplyDao replyDao) {
        this.replyDao = replyDao;
    }

    /*
       POST Handlers
     */
    public void handleReplyCreation(HttpServletRequest req, HttpServletResponse resp, Long articleId) throws IOException {
        String contents = req.getParameter("contents");

        User loginUser = AuthenticationManager.getLoginUserOrElseThrow(req);
        Reply reply = new Reply(contents, articleId, loginUser.getId());
        replyDao.save(reply);

        resp.sendRedirect("/qna/" + articleId);
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
