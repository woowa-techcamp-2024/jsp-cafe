package com.wootecam.jspcafe.servlet.question;

import com.wootecam.jspcafe.domain.Question;
import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.service.QuestionService;
import com.wootecam.jspcafe.servlet.AbstractHttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class QuestionEditHttpServlet extends AbstractHttpServlet {

    private final QuestionService questionService;

    public QuestionEditHttpServlet(final QuestionService questionService) {
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

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        User signInUser = (User) req.getSession().getAttribute("signInUser");

        if (Objects.isNull(signInUser)) {
            resp.sendRedirect("/users/sign-in");
            return;
        }
        Long id = parseSuffixPathVariable(req.getPathInfo());
        questionService.edit(
                id,
                req.getParameter("title"),
                req.getParameter("contents")
        );

        resp.sendRedirect("/questions/" + id);
    }
}
