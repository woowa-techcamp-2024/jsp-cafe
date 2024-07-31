package com.wootecam.jspcafe.servlet.question;

import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.service.QuestionService;
import com.wootecam.jspcafe.servlet.AbstractHttpServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuestionServlet extends AbstractHttpServlet {

    private static final Logger log = LoggerFactory.getLogger(QuestionServlet.class);

    private final QuestionService questionService;

    public QuestionServlet(final QuestionService questionService) {
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

        log.debug("forward to question form");
        req.getRequestDispatcher("/WEB-INF/views/qna/form.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        User signInUser = (User) req.getSession().getAttribute("signInUser");

        if (Objects.isNull(signInUser)) {
            resp.sendRedirect("/users/sign-in");
            return;
        }

        questionService.append(
                req.getParameter("writer"),
                req.getParameter("title"),
                req.getParameter("contents"),
                signInUser.getId()
        );

        resp.sendRedirect("/");
    }
}
