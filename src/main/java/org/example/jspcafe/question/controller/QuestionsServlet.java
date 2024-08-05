package org.example.jspcafe.question.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.common.StringUtils;
import org.example.jspcafe.question.Question;
import org.example.jspcafe.question.repository.QuestionRepository;
import org.example.jspcafe.reply.repository.ReplyRepository;
import org.example.jspcafe.user.User;

import java.io.IOException;

import static org.example.jspcafe.common.RequestUtil.*;
import static org.example.jspcafe.question.Question.updateQuestion;

@WebServlet(name = "QuestionServlet", value = "/questions/*")
public class QuestionsServlet extends HttpServlet {

    private QuestionRepository questionRepository;
    private ReplyRepository replyRepository;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        this.questionRepository = (QuestionRepository) context.getAttribute("QuestionRepository");
        this.replyRepository = (ReplyRepository) context.getAttribute("ReplyRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        User user = getUserFromReqSession(req);

        if(user == null) {
            res.sendRedirect("/auth/login"); // 로그인 페이지로 리다이렉트
            return;
        }

        String pathVar = extractStringPathVariable(req);

        if(StringUtils.isNumeric(pathVar)){
            Long id = extractLongPathVariable(req);
            req.setAttribute("question", questionRepository.findById(id).orElseThrow(IllegalArgumentException::new));
            req.setAttribute("replies",replyRepository.getAllByQuestionId(id));
            req.getRequestDispatcher("/qna/show.jsp").forward(req, res);
            return;
        }

        if("form".equals(pathVar)){
            req.getRequestDispatcher("/qna/form.jsp").forward(req, res);
            return;
        }

        if("edit".equals(pathVar.split("\\/")[1])){
            Long id = Long.valueOf(pathVar.split("\\/")[0]);
            Question question = questionRepository.findById(id).orElseThrow(IllegalArgumentException::new);

            if(question.getUserId()!=user.getId()){
                res.sendRedirect("/questions/"+pathVar);
                return;
            }

            req.setAttribute("question", question);
            req.getRequestDispatcher("/qna/edit.jsp").forward(req, res);
            return;
        }

        res.sendRedirect("/");
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = extractLongPathVariable(req);
        String title = req.getParameter("title");
        String contents = req.getParameter("contents");
        Question question = questionRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        updateQuestion(question, title, contents);
        questionRepository.update(question);

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setHeader("Location", "/questions/" + id);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = extractLongPathVariable(req);
        Question question = questionRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        if(question.getUserId() != getUserFromReqSession(req).getId()){
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.setHeader("Location", "/questions/" + id);
            return;
        }

        System.out.println(" = " +questionRepository.delete(id));

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setHeader("Location", "/");
    }
}
