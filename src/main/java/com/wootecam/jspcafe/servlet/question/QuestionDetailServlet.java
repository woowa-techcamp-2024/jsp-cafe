package com.wootecam.jspcafe.servlet.question;

import com.wootecam.jspcafe.model.Question;
import com.wootecam.jspcafe.service.QuestionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuestionDetailServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(QuestionDetailServlet.class);

    private final QuestionService questionService;

    public QuestionDetailServlet(final QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        Long id = parseSuffixPathVariable(req.getPathInfo());

        Question question = questionService.readById(id);

        log.info(question.toString());

        req.setAttribute("question", question);
        req.getRequestDispatcher("/WEB-INF/views/qna/show.jsp")
                .forward(req, resp);
    }

    private Long parseSuffixPathVariable(final String pathInfo) {
        String[] splitPaths = pathInfo.split("/");

        return Long.parseLong(splitPaths[splitPaths.length - 1].trim());
    }
}
