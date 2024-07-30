package org.example.jspcafe.question.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.question.repository.QuestionRepository;

import java.io.IOException;

import static org.example.jspcafe.common.RequestUtil.extractPathVariable;

@WebServlet(name = "QuestionServlet", value = "/questions/*")
public class QuestionsServlet extends HttpServlet {

    private QuestionRepository questionRepository;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        this.questionRepository = (QuestionRepository) context.getAttribute("QuestionRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = extractPathVariable(req);
        req.setAttribute("question", questionRepository.findById(id));
        req.getRequestDispatcher("/qna/show.jsp").forward(req, resp);
    }
}
