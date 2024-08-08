package org.example.cafe.ui;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.example.cafe.application.QuestionService;
import org.example.cafe.context.QuestionCountCache;
import org.example.cafe.domain.Question;
import org.slf4j.Logger;

@WebServlet("")
public class MainServlet extends BaseServlet {

    private static final Logger log = getLogger(MainServlet.class);

    private QuestionCountCache questionCountCache;
    private QuestionService questionService;

    @Override
    public void init() {
        questionService = (QuestionService) getServletContext().getAttribute("QuestionService");
        questionCountCache = (QuestionCountCache) getServletContext().getAttribute("QuestionCountCache");

        log.debug("Init servlet: {}", this.getClass().getSimpleName());
    }

    /**
     * 게시글 목록 페이지를 반환한다.
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        long page = 1L;
        if (request.getParameter("page") != null) {
            page = Long.parseLong(request.getParameter("page"));
        }

        List<Question> questions = questionService.findAll(page);

        request.setAttribute("totalPage", questionCountCache.getQuestionTotalPage());
        request.setAttribute("currentPage", page);
        request.setAttribute("questions", questions);
        request.getRequestDispatcher("/WEB-INF/qna/list.jsp").forward(request, response);
    }
}