package com.wootecam.jspcafe.servlet.question;

import com.wootecam.jspcafe.domain.Question;
import com.wootecam.jspcafe.domain.Reply;
import com.wootecam.jspcafe.domain.User;
import com.wootecam.jspcafe.service.QuestionService;
import com.wootecam.jspcafe.service.ReplyService;
import com.wootecam.jspcafe.servlet.AbstractHttpServlet;
import com.wootecam.jspcafe.servlet.dto.RepliesPageResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuestionDetailServlet extends AbstractHttpServlet {

    private static final Logger log = LoggerFactory.getLogger(QuestionDetailServlet.class);

    private final QuestionService questionService;
    private final ReplyService replyService;

    public QuestionDetailServlet(final QuestionService questionService, final ReplyService replyService) {
        this.questionService = questionService;
        this.replyService = replyService;
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
            throws ServletException, IOException {
        User signInUser = (User) req.getSession().getAttribute("signInUser");

        if (Objects.isNull(signInUser)) {
            resp.sendRedirect("/users/sign-in");
            return;
        }

        Long id = parseSuffixPathVariable(req.getPathInfo());

        Question question = questionService.read(id);
        int replyCount = replyService.countAll(question.getId());
        List<Reply> replies = replyService.readAll(question.getId(), 5);

        RepliesPageResponse response = RepliesPageResponse.of(replyCount, replies);

        log.info(question.toString());

        req.setAttribute("question", question);
        req.setAttribute("replyPageResponse", response);

        req.getRequestDispatcher("/WEB-INF/views/qna/show.jsp")
                .forward(req, resp);
    }

    private Long parseSuffixPathVariable(final String pathInfo) {
        String[] splitPaths = pathInfo.split("/");

        return Long.parseLong(splitPaths[splitPaths.length - 1].trim());
    }
}
