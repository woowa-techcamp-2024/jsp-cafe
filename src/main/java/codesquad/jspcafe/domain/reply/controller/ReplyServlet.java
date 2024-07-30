package codesquad.jspcafe.domain.reply.controller;

import codesquad.jspcafe.domain.reply.payload.request.ReplyCreateRequest;
import codesquad.jspcafe.domain.reply.service.ReplyService;
import codesquad.jspcafe.domain.user.payload.response.UserSessionResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * ReplyServlet은 댓글을 처리하는 서블릿입니다 <br>
 */
@WebServlet("/replies/*")
public class ReplyServlet extends HttpServlet {

    private transient ReplyService replyService;

    /**
     * 서블릿을 초기화하여 서블릿 컨텍스트에서 ReplyService를 가져옵니다. 이 메서드는 서블릿이 처음 로드될 때 서블릿 컨테이너에 의해 호출됩니다.
     *
     * @throws ServletException 초기화 중 오류가 발생할 경우
     */
    @Override
    public void init() throws ServletException {
        replyService = (ReplyService) getServletContext().getAttribute("replyService");
    }

    /**
     * POST 요청을 처리하여 새로운 댓글을 생성한 후 /questions/{articleId} 페이지로 리다이렉션합니다.<br> 클라이언트가 /replies로 POST
     * 요청을 보낼 때 이 메서드가 호출됩니다.
     *
     * @param req  an {@link HttpServletRequest} 클라이언트가 서블릿에 보낸 요청을 포함하는 HttpServletRequest 객체
     * @param resp an {@link HttpServletResponse} 서블릿이 클라이언트에게 보내는 응답을 포함하는 HttpServletResponse 객체
     * @throws ServletException 서블릿이 POST 요청을 처리하는 동안 입력 또는 출력 오류가 발생할 경우
     * @throws IOException      서블릿이 POST 요청을 처리하는 동안 입력 또는 출력 오류가 감지될 경우
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        UserSessionResponse userSessionResponse = (UserSessionResponse) req.getSession()
            .getAttribute("user");
        Map<String, String[]> map = req.getParameterMap();
        replyService.createReply(ReplyCreateRequest.of(map, userSessionResponse.getUserId()));
        resp.sendRedirect("/questions/" + map.get("article")[0]);
    }
}
