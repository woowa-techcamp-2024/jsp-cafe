package org.example.cafe.ui;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import org.example.cafe.application.QuestionService;
import org.example.cafe.domain.Question;
import org.slf4j.Logger;

@WebServlet("/questions/*")
public class QuestionDetailServlet extends HttpServlet {

    private static final Logger log = getLogger(QuestionDetailServlet.class);

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

        String path = request.getRequestURI();
        String[] pathParts = path.split("/");
        String questionId = URLDecoder.decode(pathParts[2], StandardCharsets.UTF_8);

        Question question = questionService.findById(questionId);
        if (question == null) {
            response.sendError(404);
            return;
        }

        request.setAttribute("question", question);
        request.getRequestDispatcher("/qna/detail.jsp").forward(request, response);
    }
}