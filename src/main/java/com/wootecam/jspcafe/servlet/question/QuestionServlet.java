package com.wootecam.jspcafe.servlet.question;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuestionServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(QuestionServlet.class);

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        log.debug("forward to question form");

        req.getRequestDispatcher("/WEB-INF/views/qna/form.jsp")
                .forward(req, resp);
    }
}
