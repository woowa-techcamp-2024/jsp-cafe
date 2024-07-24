package org.example.cafe.ui;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.cafe.application.QuestionService;
import org.example.cafe.application.dto.QuestionCreateDto;
import org.slf4j.Logger;

@WebServlet("/questions")
public class QuestionServlet extends HttpServlet {

    private static final Logger log = getLogger(QuestionServlet.class);

    private QuestionService questionService;

    @Override
    public void init() {
        questionService = (QuestionService) getServletContext().getAttribute("QuestionService");
        log.debug("Init servlet: {}", this.getClass().getSimpleName());
    }

    /**
     * 게시글을 작성한다.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");

        String writer = request.getParameter("writer");
        String title = request.getParameter("title");
        String contents = request.getParameter("contents");

        QuestionCreateDto questionCreateDto = new QuestionCreateDto(writer, title, contents);
        questionService.createQuestion(questionCreateDto);

        response.sendRedirect("/");
    }
}