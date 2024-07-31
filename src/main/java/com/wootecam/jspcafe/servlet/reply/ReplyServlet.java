package com.wootecam.jspcafe.servlet.reply;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wootecam.jspcafe.domain.Reply;
import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.exception.CommonException;
import com.wootecam.jspcafe.service.ReplyService;
import com.wootecam.jspcafe.servlet.AbstractHttpServlet;
import com.wootecam.jspcafe.servlet.util.HttpBodyParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        User signInUser = (User) req.getSession().getAttribute("signInUser");

        if (Objects.isNull(signInUser)) {
            throw new CommonException("로그인을 해야 합니다.", HttpServletResponse.SC_UNAUTHORIZED);
        }

        Map<String, String> parameters = HttpBodyParser.parse(req.getReader().readLine());
        Reply reply = replyService.append(
                Long.parseLong(parameters.get("questionId")),
                signInUser.getId(),
                signInUser.getUserId(),
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
