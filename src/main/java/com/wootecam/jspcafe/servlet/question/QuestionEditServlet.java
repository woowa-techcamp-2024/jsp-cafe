package com.wootecam.jspcafe.servlet.question;

import com.wootecam.jspcafe.domain.Question;
import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.service.QuestionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class QuestionEditServlet extends HttpServlet {

    private final QuestionService questionService;

    public QuestionEditServlet(final QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        User signInUser = (User) req.getSession().getAttribute("signInUser");

        if (Objects.isNull(signInUser)) {
            resp.sendRedirect("/users/sign-in");
            return;
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
}
