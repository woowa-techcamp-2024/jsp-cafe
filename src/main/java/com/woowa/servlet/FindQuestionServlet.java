package com.woowa.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowa.exception.AuthenticationException;
import com.woowa.framework.web.ResponseEntity;
import com.woowa.handler.QuestionHandler;
import com.woowa.handler.ReplyHandler;
import com.woowa.model.Reply;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class FindQuestionServlet extends HttpServlet {

    private static final String PREFIX = "/questions/";
    private static final String UPDATE_SUFFIX = "/update";
    private static final String REPLY_SUFFIX = "/replies";

    private final QuestionHandler questionHandler;
    private final ReplyHandler replyHandler;
    private final ObjectMapper objectMapper;

    public FindQuestionServlet(QuestionHandler questionHandler, ReplyHandler replyHandler, ObjectMapper objectMapper) {
        this.questionHandler = questionHandler;
        this.replyHandler = replyHandler;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String questionId = req.getRequestURI().replace(PREFIX, "");
        String userId = getUserIdFromSession(req);
        ResponseEntity response;
        if (questionId.endsWith(UPDATE_SUFFIX)) {
            questionId = questionId.replace(UPDATE_SUFFIX, "");
            response = questionHandler.updateQuestionForm(userId, questionId);
        } else {
            response = questionHandler.findQuestion(questionId);
        }
        req.setAttribute("question", response.getModel().get("question"));
        req.getRequestDispatcher("/WEB-INF/classes/static" + response.getViewName() + ".jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String questionId = req.getRequestURI().replace(PREFIX, "");
        String userId = getUserIdFromSession(req);
        if (questionId.endsWith(REPLY_SUFFIX)) {
            questionId = questionId.replace(REPLY_SUFFIX, "");
            String content = req.getParameter("content");

            ResponseEntity response = replyHandler.createReply(userId, questionId, content);
            Reply reply = (Reply) response.getModel().get("reply");
            String result = objectMapper.writeValueAsString(reply);
            PrintWriter writer = resp.getWriter();
            writer.print(result);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json; charset=utf-8");
            resp.setContentLength(result.getBytes().length);
            writer.flush();
            writer.close();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String questionId = req.getRequestURI().replace(PREFIX, "");
        String userId = getUserIdFromSession(req);
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        ResponseEntity response = questionHandler.updateQuestion(userId, questionId, title, content);
        resp.sendRedirect(response.getLocation());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String questionId = req.getRequestURI().replace(PREFIX, "");
        String userId = getUserIdFromSession(req);
        if (questionId.contains(REPLY_SUFFIX)) {
            String[] split = questionId
                    .replace(REPLY_SUFFIX, "")  // {questionId}/replies/{replyId}
                    .split("/");  // {questionId}/{replyId}
            questionId = split[0];
            String replyId = split[1];
            resp.setContentType("application/json; charset=utf-8");
            ResponseEntity response = replyHandler.deleteReply(userId, questionId, replyId);
            resp.setStatus(204);
        } else {
            ResponseEntity response = questionHandler.deleteQuestion(userId, questionId);
            resp.sendRedirect(response.getLocation());
        }
    }

    private String getUserIdFromSession(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) {
            throw new AuthenticationException("세션이 없습니다.");
        }
        Object userId = session.getAttribute("userId");
        if (userId == null) {
            throw new AuthenticationException("세션이 없습니다.");
        }
        return (String) userId;
    }
}
