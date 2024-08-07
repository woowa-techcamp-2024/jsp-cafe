package org.example.jspcafe.question.controller;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.jspcafe.question.Question;
import org.example.jspcafe.question.QuestionPagination;
import org.example.jspcafe.question.repository.QuestionRepository;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "Main", value = "")
public class MainServlet extends HttpServlet {

    private QuestionRepository questionRepository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext context = config.getServletContext();
        questionRepository = (QuestionRepository) context.getAttribute("QuestionRepository");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 페이지네이션 메서드 호출
        QuestionPagination allWithPagination = questionRepository.getAllWithPagination(1, 15);
        req.setAttribute("pagination", allWithPagination);
        req.getRequestDispatcher("/qna/list.jsp").forward(req, resp);

    }
}
