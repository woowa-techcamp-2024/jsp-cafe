package org.example.servlet.api;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.example.constance.DataHandler;
import org.example.constance.SessionName;
import org.example.data.ReplyDataHandler;
import org.example.domain.Reply;
import org.example.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/api/replies/*")
public class ReplyUpdateApi extends HttpServlet {
    private final Logger log = (Logger) LoggerFactory.getLogger(ReplyUpdateApi.class);
    private ReplyDataHandler replyDataHandler;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        replyDataHandler = (ReplyDataHandler) config.getServletContext()
                .getAttribute(DataHandler.REPLY.getValue());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("[ReplyAi] doDelete called");
        req.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        Long replyId = Long.parseLong(pathParts[1]);
        Reply reply = replyDataHandler.findByReplyId(replyId);
        //
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute(SessionName.USER.getName());
        if (!user.getUserId().equals(reply.getUserId())) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"댓글 작성자가 아닙니다.\"}");
            return;
        }
        //
        reply.delete();
        Reply updateReply = replyDataHandler.update(reply);
        if (updateReply != null) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"댓글이 성공적으로 삭제되었습니다.\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"댓글 삭제 중 오류가 발생했습니다.\"}");
        }
    }
}
