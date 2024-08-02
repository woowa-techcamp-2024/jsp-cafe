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
import woowa.camp.jspcafe.domain.exception.ReplyException;
import woowa.camp.jspcafe.domain.exception.UnAuthorizationException;
import woowa.camp.jspcafe.service.ReplyService;
import woowa.camp.jspcafe.repository.dto.response.ReplyResponse;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            log.info("ReplyServlet doGet start");
            Long articleId = Long.parseLong(req.getParameter("articleId"));
            List<ReplyResponse> replies = replyService.findReplyList(articleId);

            ObjectMapper mapper = new ObjectMapper();
            String result = mapper.writeValueAsString(replies);
            log.info("result - {}", result);

            resp.setContentType("application/json");
            resp.getWriter().write(result);

            log.info("ReplyServlet doGet end");
        } catch (Exception e) {
            log.warn("exception", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            log.debug("ReplyServlet doPost start");
            HttpSession session = req.getSession();
            User sessionUser = (User) session.getAttribute("WOOWA_SESSIONID");

            Long articleId = Long.parseLong(req.getParameter("articleId"));
            String content = req.getParameter("content");

            log.info("sessionUser: " + sessionUser + ", articleId: " + articleId + ", content: " + content);

            ReplyWriteRequest replyWriteRequest = new ReplyWriteRequest(sessionUser.getId(), articleId, content);
            ReplyResponse replyResponse = replyService.writeReply(replyWriteRequest);
            log.info("댓글 작성 성공 = {}", replyResponse);

            ObjectMapper mapper = new ObjectMapper();
            String result = mapper.writeValueAsString(replyResponse);
            log.info("result - {}", result);

            resp.setContentType("application/json");
            resp.getWriter().write(result);
            log.debug("ReplyServlet doPost end");
        } catch (ReplyException e) {
            log.warn("[ReplyException]", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (UnAuthorizationException e) {
            log.warn("[UnAuthorizationException]", e);
            resp.sendRedirect(req.getContextPath() + "/");
        } catch (Exception e) {
            log.warn("[Exception]", e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            log.debug("ReplyServlet doDelete start");
            HttpSession session = req.getSession();
            User sessionUser = (User) session.getAttribute("WOOWA_SESSIONID");

            Long articleId = Long.parseLong(req.getParameter("articleId"));
            Long replyId = Long.parseLong(req.getParameter("replyId"));
            log.info("articleId - {}, replyId - {}", articleId, replyId);

            replyService.deleteReply(sessionUser, articleId, replyId);
            log.debug("ReplyServlet doDelete end");
        } catch (Exception e) {
            log.warn("댓글 삭제 실패 exception", e);
        }
    }

}
