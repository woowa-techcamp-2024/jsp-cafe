package org.example.cafe.ui;

import static org.example.cafe.utils.DateTimeFormatUtils.parseDateTime;
import static org.example.cafe.utils.LoggerFactory.getLogger;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import org.example.cafe.application.ReplyService;
import org.example.cafe.application.dto.ReplyCreateDto;
import org.example.cafe.application.dto.ReplyCreateResponse;
import org.example.cafe.application.dto.ReplyListResponse;
import org.example.cafe.application.dto.ReplyPageParam;
import org.example.cafe.common.exception.BadRequestException;
import org.example.cafe.domain.Reply;
import org.example.cafe.utils.JsonDataBinder;
import org.example.cafe.utils.PathTokenExtractUtils;
import org.slf4j.Logger;

@WebServlet(name = "ReplyServlet", urlPatterns = {"/replies/*", "/replies"})
public class ReplyServlet extends BaseServlet {

    private static final Logger log = getLogger(ReplyServlet.class);

    private ObjectMapper objectMapper;
    private ReplyService replyService;

    @Override
    public void init() {
        objectMapper = (ObjectMapper) getServletContext().getAttribute("ObjectMapper");
        replyService = (ReplyService) getServletContext().getAttribute("ReplyService");
        log.debug("Init servlet: {}", this.getClass().getSimpleName());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Long questionId = null;
        Long lastReplyId = null;
        LocalDateTime createdAt = null;
        if (request.getParameter("lastReplyId") != null) {
            lastReplyId = Long.parseLong(request.getParameter("lastReplyId"));
        }

        if (request.getParameter("createdAt") != null) {
            createdAt = parseDateTime(request.getParameter("createdAt"));
        }

        if (request.getParameter("questionId") != null) {
            questionId = Long.parseLong(request.getParameter("questionId"));
            if (questionId == 0) {
                throw new BadRequestException("questionId가 잘못되었습니다.");
            }
        }

        log.debug("lastReplyId: {}, createdAt: {}", lastReplyId, createdAt);

        List<Reply> replies = replyService.findReplyPageByQuestionId(
                new ReplyPageParam(questionId, lastReplyId, createdAt));

        String jsonResult = objectMapper.writeValueAsString(ReplyListResponse.create(replies));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(jsonResult);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ReplyCreateDto replyCreateDto = JsonDataBinder.bind(objectMapper, request.getInputStream(),
                ReplyCreateDto.class);

        assert request.getSession(false) != null;
        String userId = (String) request.getSession(false).getAttribute("userId");

        Reply newReply = replyService.createReply(replyCreateDto, userId);

        String jsonResult = objectMapper.writeValueAsString(new ReplyCreateResponse(newReply));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(jsonResult);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        Long replyId = PathTokenExtractUtils.extractValueByIndex(request.getRequestURI(), 2, Long.class);

        assert request.getSession(false) != null;
        String loginUserId = (String) request.getSession(false).getAttribute("userId");

        replyService.deleteReply(replyId, loginUserId);

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
