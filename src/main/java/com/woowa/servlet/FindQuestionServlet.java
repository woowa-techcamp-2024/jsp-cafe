package com.woowa.servlet;

import com.woowa.framework.web.ResponseEntity;
import com.woowa.handler.QuestionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FindQuestionServlet extends HttpServlet {

    private final QuestionHandler questionHandler;

    public FindQuestionServlet(QuestionHandler questionHandler) {
        this.questionHandler = questionHandler;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String questionId = req.getRequestURI().replace("/questions/", "");
        ResponseEntity response = questionHandler.findQuestion(questionId);
        req.setAttribute("question", response.getModel().get("question"));
        req.getRequestDispatcher("/WEB-INF/classes/static/qna/show.jsp").forward(req, resp);
    }
}
