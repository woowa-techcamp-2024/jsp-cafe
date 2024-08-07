package codesquad.jspcafe.domain.reply.controller;

import codesquad.jspcafe.domain.reply.payload.request.ReplyCreateRequest;
import codesquad.jspcafe.domain.reply.payload.respose.ReplyCommonResponse;
import codesquad.jspcafe.domain.reply.service.ReplyService;
import codesquad.jspcafe.domain.user.payload.response.UserSessionResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * ReplyServlet은 댓글을 처리하는 서블릿입니다 <br> GET 요청을 처리하여 댓글을 조회한 후 JSON 바디를 반환합니다. <br> POST 요청을 처리하여 새로운
 * 댓글을 생성한 후 /questions/{articleId} 페이지로 리다이렉션합니다. <br> DELETE 요청을 처리하여 댓글을 삭제합니다.
 * <br> 이 서블릿은 /replies/* 경로로 매핑되어 있습니다.
 */
@WebServlet("/replies/*")
public class ReplyServlet extends HttpServlet {

    private transient ReplyService replyService;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
     * GET 요청을 처리하여 댓글을 조회한 후 JSON 바디를 반환합니다. <br> 클라이언트가 /replies로 GET 요청을 보낼 때 이 메서드가 호출됩니다.
     * <br> 클라이언트가 /replies/{replyId}로 GET 요청을 보낼 때 이 메서드가 호출됩니다.
     *
     * @param req  an {@link HttpServletRequest} 클라이언트가 서블릿에 보낸 요청을 포함하는 HttpServletRequest 객체
     * @param resp an {@link HttpServletResponse} 서블릿이 클라이언트에게 보내는 응답을 포함하는 HttpServletResponse 객체
     * @throws ServletException 서블릿이 GET 요청을 처리하는 동안 입력 또는 출력 오류가 발생할 경우
     * @throws IOException      서블릿이 GET 요청을 처리하는 동안 입력 또는 출력 오류가 발생할 경우
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        verifyPathInfo(pathInfo);
        Long articleId = Long.parseLong(pathInfo.substring(1));
        String replyId = req.getParameter("id");
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().write(replyId == null ? objectMapper.writeValueAsString(
            replyService.getRepliesByArticleId(articleId)) : objectMapper.writeValueAsString(
            replyService.getRepliesByArticleId(articleId, Long.parseLong(replyId))));
    }

    /**
     * POST 요청을 처리하여 새로운 댓글을 생성한 후 CREATED 응답과 함께 JSON 바디를 반환합니다. <br> 클라이언트가 /replies로 POST 요청을 보낼
     * 때 이 메서드가 호출됩니다.
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
        ReplyCommonResponse commonResponse = replyService.createReply(
            ReplyCreateRequest.of(map, userSessionResponse.getUserId()));
        resp.setStatus(HttpServletResponse.SC_CREATED);
        resp.setContentType("application/json");
        resp.getWriter().write(objectMapper.writeValueAsString(commonResponse));
    }

    /**
     * DELETE 요청을 처리하여 댓글을 삭제합니다. <br> 클라이언트가 /replies/{replyId}로 DELETE 요청을 보낼 때 이 메서드가 호출됩니다.
     * <br> 요청 URI가 잘못된 경우 NoSuchElementException{@link NoSuchElementException}을 던집니다.
     * <br> 요청한 사용자가 댓글 작성자가 아닌 경우
     * AccessDeniedException{@link java.nio.file.AccessDeniedException}을 던집니다.
     *
     * @param req  an {@link HttpServletRequest} 클라이언트가 서블릿에 보낸 요청을 포함하는 HttpServletRequest 객체
     * @param resp an {@link HttpServletResponse} 서블릿이 클라이언트에게 보내는 응답을 포함하는 HttpServletResponse 객체
     * @throws ServletException 서블릿이 DELETE 요청을 처리하는 동안 입력 또는 출력 오류가 발생할 경우
     * @throws IOException      서블릿이 DELETE 요청을 처리하는 동안 입력 또는 출력 오류가 감지될 경우
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        verifyPathInfo(pathInfo);
        UserSessionResponse sessionResponse = (UserSessionResponse) req.getSession()
            .getAttribute("user");
        replyService.deleteReply(Long.parseLong(pathInfo.substring(1)),
            sessionResponse.getUserId());
    }

    private void verifyPathInfo(String pathInfo) {
        if (pathInfo == null || pathInfo.isBlank()) {
            throw new NoSuchElementException("아티클을 찾을 수 없습니다.");
        }
    }
}
