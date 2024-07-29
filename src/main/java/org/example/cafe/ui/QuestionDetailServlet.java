package org.example.cafe.ui;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.example.cafe.application.QuestionService;
import org.example.cafe.domain.Question;
import org.slf4j.Logger;

@WebServlet("/questions/*")
public class QuestionDetailServlet extends BaseServlet {

    private static final Logger log = getLogger(QuestionDetailServlet.class);

    private QuestionService questionService;

    @Override
    public void init() {
        questionService = (QuestionService) getServletContext().getAttribute("QuestionService");
        log.debug("Init servlet: {}", this.getClass().getSimpleName());
    }

    /**
     * 특정 게시글의 상세 페이지를 반환한다
     *
     * @param request  an {@link HttpServletRequest} object that contains the request the client has made of the
     *                 servlet
     * @param response an {@link HttpServletResponse} object that contains the response the servlet sends to the client
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        String path = request.getRequestURI();
        String[] pathParts = path.split("/");
        Long questionId = Long.valueOf(pathParts[2]);

        Question question = questionService.findById(questionId);

        request.setAttribute("question", question);
        request.getRequestDispatcher("/WEB-INF/qna/detail.jsp").forward(request, response);
    }
}