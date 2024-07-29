package org.example.cafe.ui;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.cafe.application.QuestionService;
import org.example.cafe.application.dto.QuestionCreateDto;
import org.slf4j.Logger;

@WebServlet(name = "QuestionServlet", urlPatterns = "/questions")
public class QuestionServlet extends BaseServlet {

    private static final Logger log = getLogger(QuestionServlet.class);

    private QuestionService questionService;

    @Override
    public void init() {
        questionService = (QuestionService) getServletContext().getAttribute("QuestionService");
        log.debug("Init servlet: {}", this.getClass().getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        req.getRequestDispatcher("/WEB-INF/qna/form.jsp").forward(req, resp);
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

        QuestionCreateDto questionCreateDto = new QuestionCreateDto(title, contents, writer);
        questionService.createQuestion(questionCreateDto);

        response.sendRedirect("/");
    }
}