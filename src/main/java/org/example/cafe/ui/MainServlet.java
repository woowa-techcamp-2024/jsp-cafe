package org.example.cafe.ui;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.example.cafe.application.QuestionService;
import org.example.cafe.domain.Question;
import org.slf4j.Logger;

@WebServlet("")
public class MainServlet extends HttpServlet {

    private static final Logger log = getLogger(MainServlet.class);

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
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        List<Question> questions = questionService.findAll();

        request.setAttribute("questions", questions);
        request.getRequestDispatcher("/qna/list.jsp").forward(request, response);
    }
}