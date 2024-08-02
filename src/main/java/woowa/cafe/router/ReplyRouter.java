package woowa.cafe.router;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import woowa.cafe.dto.UserInfo;
import woowa.cafe.service.ReplyService;
import woowa.frame.web.annotation.HttpMapping;
import woowa.frame.web.annotation.Router;

@Router(url = "/question/{questionId}/reply")
public class ReplyRouter {

    private final ReplyService replyService;

    public ReplyRouter(ReplyService replyService) {
        this.replyService = replyService;
    }

    @HttpMapping(method = "POST")
    public Integer createReply(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getRequestURI();
        String questionId = url.substring(10, url.lastIndexOf("/reply"));
        String content = request.getParameter("content");
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");

        if (content == null || content.isEmpty()) {
            return 400;
        }
        replyService.createReply(questionId, content, userInfo.id(), userInfo.name());
        return 200;
    }

    @HttpMapping(method = "DELETE", urlTemplate = "/{replyId}")
    public Integer deleteReply(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getRequestURI();
        String questionId = url.substring(10, url.lastIndexOf("/reply"));
        String replyId = url.substring(url.lastIndexOf("/reply/") + 7);
        UserInfo userInfo = (UserInfo) request.getSession().getAttribute("userInfo");

        if (replyService.deleteReply(userInfo, questionId, replyId)) {
            return 200;
        } else {
            return 400;
        }
    }
}
