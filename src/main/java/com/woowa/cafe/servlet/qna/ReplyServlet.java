package com.woowa.cafe.servlet.qna;

import com.woowa.cafe.dto.article.ReplyDto;
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
import java.util.List;
import java.util.Map;

import static com.woowa.cafe.config.GsonConfig.gson;

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
    public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        log.debug("ReplyServlet doGet");
        String queryString = req.getQueryString();
        String[] split = queryString.split("&");
        if (split.length == 1) {
            throw new HttpException(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다.");
        }
        Long articleId = Long.parseLong(split[0].split("=")[1]);
        if (split.length != 3) {
            List<ReplyDto> byArticleIdWithPage = replyService.findByArticleIdWithPage(articleId, 1, 5);
            return;
        }
        int index = Integer.parseInt(split[1].split("=")[1]);
        int size = Integer.parseInt(split[2].split("=")[1]);
        List<ReplyDto> byArticleIdWithPage = replyService.findByArticleIdWithPage(articleId, index, size);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(byArticleIdWithPage));
    }

    @Override
    public void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        Map<String, String> bodyFormData = HttpMessageUtils.getBodyFormData(req);

        for (String string : bodyFormData.keySet()) {
            log.debug("key: {}, value: {}", string, bodyFormData.get(string));
        }

        ReplyDto reply = replyService.save(SaveReplyDto.from(bodyFormData), (String) req.getSession().getAttribute("memberId"));

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(gson.toJson(reply));
    }

    @Override
    public void doDelete(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        log.debug("ReplyServlet doDelete");
        String pathInfo = req.getPathInfo();

        String[] split = pathInfo.split("/");
        if (split.length != 2) {
            throw new HttpException(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "잘못된 요청입니다.");
        }

        Long replyId = Long.parseLong(split[1]);
        HttpSession session = req.getSession();
        String memberId = (String) session.getAttribute("memberId");

        replyService.delete(replyId, memberId);
    }
}
