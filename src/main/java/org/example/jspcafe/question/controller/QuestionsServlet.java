package org.example.jspcafe.question.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.question.Question;
import org.example.jspcafe.question.repository.QuestionRepository;
import org.example.jspcafe.question.service.QuestionService;

import java.io.IOException;
import java.util.List;

import static org.example.jspcafe.common.RequestUtil.extractPathVariable;
import static org.example.jspcafe.common.RequestUtil.getPathInfo;

@WebServlet(name = "QuestionServlet", value = "/questions/*")
public class QuestionsServlet extends HttpServlet {

    private QuestionRepository questionRepository;
    private QuestionService questionService;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        this.questionRepository = (QuestionRepository) context.getAttribute("QuestionRepository");
        this.questionService = (QuestionService) context.getAttribute("QuestionService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractPathVariable(req);
        req.setAttribute("question", questionRepository.findById(id));
        req.getRequestDispatcher("/qna/show.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String writer = req.getParameter("writer");
        String title = req.getParameter("title");
        String contents = req.getParameter("contents");
        Question question = new Question(writer, title, contents);
        Long id = questionService.saveQuestion(question);
        resp.sendRedirect("/questions/" + id);
    }
}
