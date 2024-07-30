package com.wootecam.jspcafe.servlet.question;

import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.exception.CommonException;
import com.wootecam.jspcafe.service.QuestionService;
import com.wootecam.jspcafe.servlet.AbstractHttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class QuestionDeleteServlet extends AbstractHttpServlet {

    private final QuestionService questionService;

    public QuestionDeleteServlet(final QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    protected void doDelete(final HttpServletRequest req, final HttpServletResponse resp) {
        User signInUser = (User) req.getSession().getAttribute("signInUser");

        if (Objects.isNull(signInUser)) {
            throw new CommonException("로그인을 해야 합니다.", HttpServletResponse.SC_UNAUTHORIZED);
        }
        Long questionId = parseSuffixPathVariable(req.getPathInfo());

        questionService.delete(questionId, signInUser.getId());
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
