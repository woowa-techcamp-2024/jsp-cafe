package com.woowa.cafe.servlet.qna;

import com.woowa.cafe.dto.article.SaveReplyDto;
import com.woowa.cafe.exception.HttpException;
import com.woowa.cafe.service.ReplyService;
import com.woowa.cafe.utils.HttpMessageUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "replyServlet", urlPatterns = {"/reply", "/reply/*"})
public class ReplyServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(ReplyServlet.class);
    private ReplyService replyService;

    @Override
    public void init() throws ServletException {
        this.replyService = (ReplyService) getServletContext().getAttribute("replyService");

        log.info("ReplyServlet init");
    }

    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        log.info("ReplyServlet doPost");
        Map<String, String> bodyFormData = HttpMessageUtils.getBodyFormData(req);

        Long articleId = replyService.save(SaveReplyDto.from(bodyFormData), (String) req.getSession().getAttribute("memberId"));

        resp.sendRedirect("/question/" + articleId);
    }

    @Override
    public void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    public void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        String[] split = pathInfo.split("/");
        if (split.length != 5) {
            throw new HttpException(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "잘못된 요청입니다.");
        }
        Long replyId = Long.parseLong(split[4]);
        HttpSession session = req.getSession();
        String memberId = (String) session.getAttribute("memberId");

        replyService.delete(replyId, memberId);
    }
}
