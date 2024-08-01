package woowa.camp.jspcafe.web.servlet.reply;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.domain.Reply;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.service.ReplyService;
import woowa.camp.jspcafe.service.dto.ReplyWriteRequest;

@WebServlet(name = "replyServlet", value = "/comments/*")
public class ReplyServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ReplyServlet.class);
    private ReplyService replyService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        this.replyService = (ReplyService) context.getAttribute("replyService");
        if (this.replyService == null) {
            String errorMessage = "[ServletException] ReplyServlet -> ReplyService not initialized";
            log.error(errorMessage);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("ReplyServlet doPost start");
        HttpSession session = req.getSession();
        User sessionUser = (User) session.getAttribute("WOOWA_SESSIONID");

        Long articleId = Long.parseLong(req.getParameter("articleId"));
        String content = req.getParameter("content");

        log.info("sessionUser: " + sessionUser + ", articleId: " + articleId + ", content: " + content);

        ReplyWriteRequest replyWriteRequest = new ReplyWriteRequest(sessionUser.getId(), articleId, content);
        Reply reply = replyService.writeReply(replyWriteRequest);

        log.info("댓글 작성 성공 = {}", reply);

        resp.sendRedirect(req.getContextPath() + "/articles/" + articleId);
        log.debug("ReplyServlet doPost end");
    }
}
