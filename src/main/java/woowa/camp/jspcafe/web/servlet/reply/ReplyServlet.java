package woowa.camp.jspcafe.web.servlet.reply;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.repository.dto.response.ReplyResponse;
import woowa.camp.jspcafe.service.ReplyService;
import woowa.camp.jspcafe.service.dto.request.ReplyWriteRequest;

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("ReplyServlet doGet start");
        Long articleId = Long.parseLong(req.getParameter("articleId"));
        Long lastReplyId = null;
        String lastReplyIdParam = req.getParameter("lastReplyId");
        if (lastReplyIdParam != null && !lastReplyIdParam.isEmpty()) {
            lastReplyId = Long.parseLong(lastReplyIdParam);
        }
        log.info("lastReplyId: {}", lastReplyId);

        List<ReplyResponse> replies = replyService.findReplyList(articleId, lastReplyId);

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(replies);

        resp.setContentType("application/json");
        resp.getWriter().write(result);
        log.debug("ReplyServlet doGet end");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("ReplyServlet doPost start");
        HttpSession session = req.getSession();
        User sessionUser = (User) session.getAttribute("WOOWA_SESSIONID");

        Long articleId = Long.parseLong(req.getParameter("articleId"));
        String content = req.getParameter("content");

        ReplyWriteRequest replyWriteRequest = new ReplyWriteRequest(sessionUser.getId(), articleId, content);
        ReplyResponse replyResponse = replyService.writeReply(replyWriteRequest);

        ObjectMapper mapper = new ObjectMapper();
        String result = mapper.writeValueAsString(replyResponse);

        resp.setContentType("application/json");
        resp.getWriter().write(result);
        log.debug("ReplyServlet doPost end");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("ReplyServlet doDelete start");
        HttpSession session = req.getSession();
        User sessionUser = (User) session.getAttribute("WOOWA_SESSIONID");

        Long articleId = Long.parseLong(req.getParameter("articleId"));
        Long replyId = Long.parseLong(req.getParameter("replyId"));
        log.info("articleId - {}, replyId - {}", articleId, replyId);

        replyService.deleteReply(sessionUser, articleId, replyId);
        log.debug("ReplyServlet doDelete end");
    }

}
