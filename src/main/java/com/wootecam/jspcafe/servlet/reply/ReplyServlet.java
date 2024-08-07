package com.wootecam.jspcafe.servlet.reply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wootecam.jspcafe.domain.Reply;
import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.exception.CommonException;
import com.wootecam.jspcafe.exception.NotFoundException;
import com.wootecam.jspcafe.service.ReplyService;
import com.wootecam.jspcafe.servlet.AbstractHttpServlet;
import com.wootecam.jspcafe.servlet.dto.RepliesAppendPageResponse;
import com.wootecam.jspcafe.servlet.util.HttpBodyParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReplyServlet extends AbstractHttpServlet {

    private final ReplyService replyService;
    private final ObjectMapper mapper;

    public ReplyServlet(final ReplyService replyService, final ObjectMapper mapper) {
        this.replyService = replyService;
        this.mapper = mapper;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        User signInUser = (User) req.getSession().getAttribute("signInUser");

        if (Objects.isNull(signInUser)) {
            throw new CommonException("로그인을 해야 합니다.", HttpServletResponse.SC_UNAUTHORIZED);
        }

        String lastReplyId = req.getParameter("lastReplyId");
        String questionId = req.getParameter("questionId");

        if (Objects.isNull(lastReplyId) || Objects.isNull(questionId)) {
            throw new NotFoundException("질문을 찾을 수 없습니다.");
        }
        List<Reply> replies = replyService.readAllStartsWith(Long.parseLong(questionId), Long.parseLong(lastReplyId),
                5);
        RepliesAppendPageResponse response = RepliesAppendPageResponse.of(replies);

        resp.getWriter()
                .write(mapper.writeValueAsString(response));
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        User signInUser = (User) req.getSession().getAttribute("signInUser");

        if (Objects.isNull(signInUser)) {
            throw new CommonException("로그인을 해야 합니다.", HttpServletResponse.SC_UNAUTHORIZED);
        }

        Map<String, String> parameters = HttpBodyParser.parse(req.getReader().readLine());
        Reply reply = replyService.append(
                Long.parseLong(parameters.get("questionId")),
                signInUser.getId(),
                signInUser.getName(),
                parameters.get("contents")
        );

        resp.getWriter()
                .write(mapper.writeValueAsString(reply));
    }

    @Override
    protected void responseError(final HttpServletRequest request,
                                 final HttpServletResponse response,
                                 final CommonException e) throws IOException {
        response.setStatus(e.getStatusCode());
        response.getWriter().print(e.getMessage());
    }
}
