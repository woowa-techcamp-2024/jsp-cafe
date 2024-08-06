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
import org.example.jspcafe.question.service.QuestionService;
import org.example.jspcafe.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

import static org.example.jspcafe.common.RequestUtil.getUserFromReqSession;

@WebServlet("/questions")
public class QuestionServlet extends HttpServlet {
    private QuestionRepository questionRepository;
    private QuestionService questionService;
    private Logger logger = LoggerFactory.getLogger(QuestionServlet.class);

    @Override
    public void init(ServletConfig config) {
        ServletContext context = config.getServletContext();
        this.questionRepository = (QuestionRepository) context.getAttribute("QuestionRepository");
        this.questionService = (QuestionService) context.getAttribute("QuestionService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1; // 기본값 설정
        int size = 15; // 기본값 설정

        try {
            String pageParam = req.getParameter("page");
            String sizeParam = req.getParameter("size");

            if (pageParam != null) {
                page = Integer.parseInt(pageParam);
            }

            if (sizeParam != null) {
                size = Integer.parseInt(sizeParam);
            }

        } catch (NumberFormatException | NullPointerException e) {
            logger.error("Invalid page or size parameter. Using default values.");
            System.err.println("Invalid page or size parameter. Using default values.");
        }
        // 페이지네이션 메서드 호출
        QuestionPagination allWithPagination = questionRepository.getAllWithPagination(page, size);
        req.setAttribute("pagination", allWithPagination);
        req.getRequestDispatcher("/qna/list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = getUserFromReqSession(req);

        if (user == null) {
            resp.sendRedirect("/login");
        }

        String title = req.getParameter("title");
        String contents = req.getParameter("contents");
        Question question = Question.builder()
                .userId(user.getId())
                .title(title)
                .contents(contents)
                .build();
        Long id = questionService.saveQuestion(question);
        resp.sendRedirect("/questions/" + id);
    }
}
