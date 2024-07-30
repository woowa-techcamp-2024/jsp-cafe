package com.wootecam.jspcafe.servlet;

import com.wootecam.jspcafe.domain.Question;
import com.wootecam.jspcafe.service.QuestionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HomeServlet extends AbstractHttpServlet {

    private static final Logger log = LoggerFactory.getLogger(HomeServlet.class);

    private final QuestionService questionService;

    public HomeServlet(final QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        List<Question> questions = questionService.readAll();
        req.setAttribute("questions", questions);

        log.debug("forward to home");

        req.getRequestDispatcher("/WEB-INF/views/index.jsp")
                .forward(req, resp);
    }
}
