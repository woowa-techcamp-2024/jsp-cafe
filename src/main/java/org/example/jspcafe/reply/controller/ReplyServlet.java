package org.example.jspcafe.reply.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.common.DateTimeUtil;
import org.example.jspcafe.question.repository.QuestionRepository;
import org.example.jspcafe.reply.Reply;
import org.example.jspcafe.reply.repository.ReplyRepository;
import org.example.jspcafe.user.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.example.jspcafe.common.RequestUtil.*;

@WebServlet(value = "/reply")
public class ReplyServlet extends HttpServlet {

    private ReplyRepository replyRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        replyRepository = (ReplyRepository) context.getAttribute("ReplyRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        User user = getUserFromReqSession(req);
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            Long questionId = Long.valueOf(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");

        User user = getUserFromReqSession(req);
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Long questionId = Long.valueOf(req.getParameter("questionId"));
        String contents = req.getParameter("contents");

        Reply newReply = Reply.builder()
                .userId(user.getId())
                .questionId(questionId)
                .contents(contents)
                .lastModifiedDate(LocalDateTime.now())
                .build();

        Long savedId = replyRepository.save(newReply);

        // JSON 응답으로 새로 생성된 댓글 정보 반환
        try (PrintWriter out = resp.getWriter()) {
            out.print("{\"id\":" + savedId + ",\"userId\":" + user.getId() + ",\"userNickName\":\"" + user.getNickname() +"\",\"questionId\":" + newReply.getQuestionId() + ",\"contents\":\"" + newReply.getContents() + "\",\"lastModifiedDate\":\"" + DateTimeUtil.dateTimeToString(newReply.getLastModifiedDate()) + "\"}");
            out.flush();
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
        }catch (Exception e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
            return;
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        User user = getUserFromReqSession(req);
        if (user == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Long replyId = Long.valueOf(req.getParameter("replyId"));
        Reply reply = replyRepository.findById(replyId).orElseThrow(() -> new IllegalArgumentException("Invalid reply ID : "+replyId));

        if (reply.getUserId() != user.getId()) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        replyRepository.deleteById(replyId);

        // JSON 응답으로 삭제 완료 메시지 반환
        try (PrintWriter out = resp.getWriter()) {
            out.print("{\"message\":\"Reply deleted successfully\"}");
            out.flush();
        }
    }
}
