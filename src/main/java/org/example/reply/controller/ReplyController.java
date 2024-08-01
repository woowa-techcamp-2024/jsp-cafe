package org.example.reply.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.example.config.HttpMethod;
import org.example.config.annotation.Autowired;
import org.example.config.annotation.Controller;
import org.example.config.annotation.PathVariable;
import org.example.config.annotation.RequestMapping;
import org.example.config.annotation.RequestParam;
import org.example.member.model.dto.UserDto;
import org.example.post.service.PostService;
import org.example.reply.model.dto.ReplyDto;
import org.example.reply.service.ReplyService;
import org.example.util.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping(path = "/reply")
public class ReplyController {

    private static final Logger logger = LoggerFactory.getLogger(ReplyController.class);
    private final ReplyService replyService;
    private final PostService postService;
    private final SessionManager sessionManager;

    @Autowired
    public ReplyController(ReplyService replyService, PostService postService, SessionManager sessionManager) {
        this.replyService = replyService;
        this.postService = postService;
        this.sessionManager = sessionManager;
    }

    @RequestMapping(path = "/{postId}", method = HttpMethod.GET)
    public void getAllReplyList(@PathVariable("postId") Long postId, HttpServletResponse response, HttpSession session)
            throws SQLException, IOException {
        List<ReplyDto> allReplies = replyService.getAllReplies(postId);
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");
        logger.info("all Replies : {}", allReplies.toString());
        StringBuilder htmlBuilder = new StringBuilder();
        UserDto currentUser = sessionManager.getUserDetails(session.getId());
        for (ReplyDto reply : allReplies) {
            htmlBuilder.append(String.format(
                    "<li id='reply-%d'>" +
                            "<p>%s</p>" +
                            "<span>작성자: %s</span>",
                    reply.getId(), reply.getContents(), reply.getWriter()));

            if (currentUser != null && currentUser.getName().equals(reply.getWriter())) {
                htmlBuilder.append(String.format(
                        "<button onclick='editReply(%d)'>수정</button>" +
                                "<button onclick='deleteReply(%d)'>삭제</button>",
                        reply.getId(), reply.getId()));
            }

            htmlBuilder.append("</li>");
        }
        response.getWriter().write(htmlBuilder.toString());
    }

    @RequestMapping(path = "/{postId}", method = HttpMethod.POST)
    public void addReply(@PathVariable("postId") Long postId,
                         @RequestParam("contents") String contents,
                         HttpSession session,
                         HttpServletResponse response) throws SQLException, IOException {
        UserDto userDetails = sessionManager.getUserDetails(session.getId());
        if (userDetails == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("로그인이 필요합니다.");
            return;
        }

        ReplyDto newReply = replyService.saveReply(userDetails, postId, contents);
        ReplyDto replyById = replyService.getReplyById(newReply.getId());
        if (replyById != null) {
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            logger.info("newReply: {}", replyById);
            response.getWriter().write(String.format(
                    "<li id='reply-%d'>" +
                            "<p>%s</p>" +
                            "<span>작성자: %s</span>" +
                            "<c:if test=\"${isAuthor}\">" +
                            "<button onclick='editReply(%d)'>수정</button>" +
                            "<button onclick='deleteReply(%d)'>삭제</button>" +
                            "</c:if>" +
                            "</li>"
                    ,
                    replyById.getId(), replyById.getContents(), replyById.getWriter(), replyById.getId(), replyById.getId()
            ));
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("댓글 추가에 실패했습니다.");
        }
    }

    @RequestMapping(path = "/{replyId}", method = HttpMethod.PUT)
    public void updateReply(@PathVariable("replyId") Long replyId,
                            @RequestParam("contents") String contents,
                            HttpSession session,
                            HttpServletResponse response) throws SQLException, IOException {
        UserDto userDetails = sessionManager.getUserDetails(session.getId());
        if (userDetails == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("로그인이 필요합니다.");
            return;
        }

        ReplyDto updatedReply = replyService.updateReply(replyId, userDetails, contents);
        if (updatedReply != null) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(updatedReply.getContents());
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("댓글 수정에 실패했습니다.");
        }
    }

    @RequestMapping(path = "/{replyId}", method = HttpMethod.DELETE)
    public void deleteReply(@PathVariable("replyId") Long replyId,
                            HttpSession session,
                            HttpServletResponse response) throws SQLException, IOException {
        UserDto userDetails = sessionManager.getUserDetails(session.getId());
        if (userDetails == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("로그인이 필요합니다.");
            return;
        }

        boolean isDeleted = replyService.deleteReply(replyId, userDetails);
        if (isDeleted) {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("success");
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("댓글 삭제에 실패했습니다.");
        }
    }
}