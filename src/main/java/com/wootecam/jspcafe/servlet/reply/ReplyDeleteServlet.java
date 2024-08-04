package com.wootecam.jspcafe.servlet.reply;

import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.exception.CommonException;
import com.wootecam.jspcafe.service.ReplyService;
import com.wootecam.jspcafe.servlet.AbstractHttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class ReplyDeleteServlet extends AbstractHttpServlet {

    private final ReplyService replyService;

    public ReplyDeleteServlet(final ReplyService replyService) {
        this.replyService = replyService;
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        User signInUser = (User) req.getSession().getAttribute("signInUser");

        if (Objects.isNull(signInUser)) {
            throw new CommonException("로그인을 해야 합니다.", HttpServletResponse.SC_UNAUTHORIZED);
        }

        Long replyId = parseSuffixPathVariable(req.getRequestURI());

        replyService.delete(replyId, signInUser.getId());
    }

    private Long parseSuffixPathVariable(final String pathInfo) {
        String[] splitPaths = pathInfo.split("/");

        return Long.parseLong(splitPaths[splitPaths.length - 1].trim());
    }

    @Override
    protected void responseError(final HttpServletRequest request,
                                 final HttpServletResponse response,
                                 final CommonException e) throws IOException {
        response.setStatus(e.getStatusCode());
        response.getWriter().print(e.getMessage());
    }
}
