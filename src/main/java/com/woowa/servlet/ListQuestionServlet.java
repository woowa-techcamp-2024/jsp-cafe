package com.woowa.servlet;

import com.woowa.framework.web.ResponseEntity;
import com.woowa.handler.QuestionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ListQuestionServlet extends HttpServlet {

    private final QuestionHandler questionHandler;

    public ListQuestionServlet(QuestionHandler questionHandler) {
        this.questionHandler = questionHandler;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String page = req.getParameter("page");
        String size = req.getParameter("size");
        if(page == null) {
            page = "0";
        }
        if(size == null) {
            size = "10";
        }
        ResponseEntity response = questionHandler.findQuestions(Integer.parseInt(page), Integer.parseInt(size));
        req.setAttribute("questions", response.getModel().get("questions"));
        req.getRequestDispatcher("/WEB-INF/classes/static/index.jsp").forward(req, resp);
    }
}
