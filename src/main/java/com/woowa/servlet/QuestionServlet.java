package com.woowa.servlet;

import com.woowa.database.QuestionDatabase;
import com.woowa.framework.web.ResponseEntity;
import com.woowa.handler.QuestionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

public class QuestionServlet extends HttpServlet {

    private final QuestionHandler questionHandler;

    public QuestionServlet(QuestionHandler questionHandler) {
        this.questionHandler = questionHandler;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        String userId = getUserIdFrom(session);
        if(userId == null) {
            resp.sendRedirect("/WEB-INF/classes/static/user/login.jsp");
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
