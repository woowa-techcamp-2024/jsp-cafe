package org.example.cafe.ui;

import static org.example.cafe.utils.LoggerFactory.getLogger;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.example.cafe.application.QuestionService;
import org.example.cafe.application.ReplyService;
import org.example.cafe.application.dto.QuestionUpdateDto;
import org.example.cafe.common.exception.BadAuthenticationException;
import org.example.cafe.domain.Question;
import org.example.cafe.domain.Reply;
import org.example.cafe.utils.JsonDataBinder;
import org.example.cafe.utils.PathTokenExtractUtils;
import org.slf4j.Logger;

@WebServlet("/questions/*")
public class QuestionDetailServlet extends BaseServlet {

    private static final Logger log = getLogger(QuestionDetailServlet.class);

    private QuestionService questionService;
    private ReplyService replyService;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        this.questionService = (QuestionService) getServletContext().getAttribute("QuestionService");
        this.replyService = (ReplyService) getServletContext().getAttribute("ReplyService");
        this.objectMapper = (ObjectMapper) getServletContext().getAttribute("ObjectMapper");
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
        Long questionId = PathTokenExtractUtils.extractValueByIndex(request.getRequestURI(), 2, Long.class);

        String edit = request.getParameter("edit");
        if ("true".equals(edit)) {
            forwardEditQuestionForm(request, response, questionId);
            return;
        }

        forwardQuestionDetail(request, response, questionId);
    }

    private void forwardQuestionDetail(HttpServletRequest request, HttpServletResponse response, Long questionId)
            throws ServletException, IOException {
        Question question = questionService.findById(questionId);
        List<Reply> replies = replyService.findRepliesByQuestionId(questionId);

        request.setAttribute("question", question);
        request.setAttribute("replies", replies);
        request.getRequestDispatcher("/WEB-INF/qna/detail.jsp").forward(request, response);
    }

    private void forwardEditQuestionForm(HttpServletRequest request, HttpServletResponse response, Long questionId)
            throws ServletException, IOException {
        Question question = questionService.findById(questionId);
        if (question.isValidWriter((String) request.getSession().getAttribute("userId"))) {
            throw new BadAuthenticationException("작성자만 수정, 삭제할 수 있습니다.");
        }

        request.setAttribute("question", question);
        request.getRequestDispatcher("/WEB-INF/qna/form.jsp").forward(request, response);
    }

    /**
     * 게시글을 삭제한다
     *
     * @param request  the {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param response the {@link HttpServletResponse} object that contains the response the servlet returns to the
     *                 client
     * @throws IOException
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long questionId = PathTokenExtractUtils.extractValueByIndex(request.getRequestURI(), 2, Long.class);

        assert request.getSession().getAttribute("userId") != null;
        String loginUserId = (String) request.getSession().getAttribute("userId");

        questionService.deleteQuestion(loginUserId, questionId);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long questionId = PathTokenExtractUtils.extractValueByIndex(request.getRequestURI(), 2, Long.class);

        assert request.getSession().getAttribute("userId") != null;
        String loginUserId = (String) request.getSession().getAttribute("userId");

        QuestionUpdateDto questionUpdateDto = JsonDataBinder.bind(objectMapper, request.getInputStream(),
                QuestionUpdateDto.class);
        questionService.updateQuestion(loginUserId, questionUpdateDto);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}