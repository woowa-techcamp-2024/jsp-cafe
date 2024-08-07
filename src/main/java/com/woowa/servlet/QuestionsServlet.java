package com.woowa.servlet;

import com.woowa.framework.web.ResponseEntity;
import com.woowa.handler.QuestionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class QuestionsServlet extends HttpServlet {

    private final QuestionHandler questionHandler;

    public QuestionsServlet(QuestionHandler questionHandler) {
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
            size = "15";
        }
        ResponseEntity response = questionHandler.findQuestions(Integer.parseInt(page), Integer.parseInt(size));
        req.setAttribute("questions", response.getModel().get("questions"));
        req.getRequestDispatcher("/qna/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String userId = getUserIdFrom(session);
        if(userId == null) {
            resp.sendRedirect("/user/login.jsp");
            return;
        }
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        ResponseEntity response = questionHandler.createQuestion(userId, title, content);
        resp.sendRedirect(response.getLocation());
    }

    private String getUserIdFrom(HttpSession session) {
        if(session == null) {
            return null;
        }
        Object userId = session.getAttribute("userId");
        if(userId == null) {
            return null;
        }
        return (String) userId;
    }
}
