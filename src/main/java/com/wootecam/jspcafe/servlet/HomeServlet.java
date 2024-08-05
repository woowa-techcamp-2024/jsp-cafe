package com.wootecam.jspcafe.servlet;

import com.wootecam.jspcafe.domain.Question;
import com.wootecam.jspcafe.service.QuestionService;
import com.wootecam.jspcafe.servlet.dto.QuestionsPageResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class HomeServlet extends AbstractHttpServlet {

    private final QuestionService questionService;

    public HomeServlet(final QuestionService questionService) {
        this.questionService = questionService;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        int questionCount = questionService.countAll();
        int page = parsePage(req.getParameter("page"));
        int size = parseSize(req.getParameter("size"));

        List<Question> questions;
        questions = questionService.readAll(page, size);

        QuestionsPageResponse response = QuestionsPageResponse.of(questionCount, page, questions);
        req.setAttribute("questions", response);

        req.getRequestDispatcher("/WEB-INF/views/index.jsp")
                .forward(req, resp);
    }

    private int parseSize(final String size) {
        if (Objects.isNull(size)) {
            return 15;
        }
        return Integer.parseInt(size);
    }

    private int parsePage(final String page) {
        if (Objects.isNull(page)) {
            return 1;
        }
        return Integer.parseInt(page);
    }
}
