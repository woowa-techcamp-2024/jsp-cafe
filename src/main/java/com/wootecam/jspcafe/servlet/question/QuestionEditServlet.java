package com.wootecam.jspcafe.servlet.question;

import com.wootecam.jspcafe.domain.Question;
import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.exception.CommonException;
import com.wootecam.jspcafe.service.QuestionService;
import com.wootecam.jspcafe.servlet.AbstractHttpServlet;
import com.wootecam.jspcafe.servlet.util.HttpBodyParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class QuestionEditServlet extends AbstractHttpServlet {

    private final QuestionService questionService;

    public QuestionEditServlet(final QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        User signInUser = (User) req.getSession().getAttribute("signInUser");

        if (Objects.isNull(signInUser)) {
            throw new CommonException("로그인을 해야 합니다.", HttpServletResponse.SC_UNAUTHORIZED);
        }
        Long questionId = parseSuffixPathVariable(req.getPathInfo());
        Question question = questionService.readQuestionToEdit(questionId, signInUser.getId());

        req.setAttribute("question", question);
        req.getRequestDispatcher("/WEB-INF/views/qna/edit_form.jsp")
                .forward(req, resp);
    }

    private Long parseSuffixPathVariable(final String pathInfo) {
        String[] splitPaths = pathInfo.split("/");

        return Long.parseLong(splitPaths[splitPaths.length - 1].trim());
    }

    @Override
    protected void doPut(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
        User signInUser = (User) req.getSession().getAttribute("signInUser");

        if (Objects.isNull(signInUser)) {
            throw new CommonException("로그인을 해야 합니다.", HttpServletResponse.SC_UNAUTHORIZED);
        }
        Long id = parseSuffixPathVariable(req.getPathInfo());
        Map<String, String> parameters = HttpBodyParser.parse(req.getReader().readLine());

        questionService.edit(
                id,
                parameters.get("title"),
                parameters.get("contents")
        );
    }

    @Override
    protected void responseError(final HttpServletRequest request,
                                 final HttpServletResponse response,
                                 final CommonException e) throws IOException {
        response.setStatus(e.getStatusCode());
        response.getWriter().print(e.getMessage());
    }
}
