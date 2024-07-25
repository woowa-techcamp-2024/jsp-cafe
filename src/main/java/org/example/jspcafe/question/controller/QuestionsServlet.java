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
import org.example.jspcafe.user.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "QuestionServlet", value = "/questions")
public class QuestionsServlet extends HttpServlet {

    private QuestionRepository questionRepository;

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        this.questionRepository = (QuestionRepository) context.getAttribute("QuestionRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Question> all = questionRepository.getAll();
        System.out.println(all.size());
        req.setAttribute("questions", all);
        req.getRequestDispatcher("/qna/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String writer = req.getParameter("writer");
        String title = req.getParameter("title");
        String contents = req.getParameter("contents");

        Question question = new Question(writer, title, contents);
        questionRepository.save(question);

        resp.sendRedirect("/");
    }
}
