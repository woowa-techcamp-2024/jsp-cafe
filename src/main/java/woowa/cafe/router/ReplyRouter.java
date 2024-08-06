package woowa.cafe.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import woowa.cafe.dto.ReplyInfo;
import woowa.cafe.dto.UserInfo;
import woowa.cafe.service.ReplyService;
import woowa.frame.web.annotation.HttpMapping;
import woowa.frame.web.annotation.Router;

import java.util.Map;

@Router(url = "/question/{questionId}/reply")
public class ReplyRouter {

    private final ReplyService replyService;

    public ReplyRouter(ReplyService replyService) {
        this.replyService = replyService;
    }

    @HttpMapping(method = "POST")
    public Map<String, Object> createReply(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getRequestURI();
        String questionId = url.substring(10, url.lastIndexOf("/reply"));
        String content = request.getParameter("content");
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");

        if (content == null || content.isEmpty()) {
            return null;
        }
        ReplyInfo replyInfo = replyService.createReply(questionId, content, userInfo.id(), userInfo.name());

        return Map.of(
                "message", "success : create reply",
                "data", Map.of(
                        "id", replyInfo.id() + "",
                        "content", replyInfo.content(),
                        "createdDate", replyInfo.createdDate().toLocalDate().toString(),
                        "status", replyInfo.status(),
                        "userId", replyInfo.userId(),
                        "authorName", replyInfo.authorName(),
                        "questionId", replyInfo.questionId()
                )
        );
    }

    @HttpMapping(method = "DELETE", urlTemplate = "/{replyId}")
    public Map<String, Object> deleteReply(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getRequestURI();
        String questionId = url.substring(10, url.lastIndexOf("/reply"));
        String replyId = url.substring(url.lastIndexOf("/reply/") + 7);
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");

        if (!replyService.deleteReply(userInfo, questionId, replyId)) {
            return null;
        }
        return Map.of(
                "message", "success : delete reply",
                "data", Map.of(
                        "questionId", questionId,
                        "replyId", replyId
                )
        );
    }
}
