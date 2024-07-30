package codesquad.jspcafe.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ExceptionHandlingFilter는 서블릿에서 발생하는 예외를 처리하는 필터입니다. <br> 서블릿에서 발생하는 예외를 처리하여 에러 페이지로 포워딩합니다.
 */
@WebFilter("/*")
public class ExceptionHandlingFilter extends HttpFilter {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlingFilter.class);

    /**
     * 서블릿에서 발생하는 예외를 처리하여 에러 페이지로 포워딩합니다.
     *
     * @param req   클라이언트가 필터에 요청한 내용을 포함하는 {@link HttpServletRequest} 객체
     * @param res   필터가 클라이언트에게 보내는 응답을 포함하는 {@link HttpServletResponse} 객체
     * @param chain 다음 필터 또는 리소스를 호출하는 <code>FilterChain</code>
     * @throws IOException      이 필터의 처리 중 I/O 오류가 발생한 경우
     * @throws ServletException 처리 중 다른 이유로 실패한 경우
     */
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
        throws IOException, ServletException {
        try {
            chain.doFilter(req, res);
        } catch (Exception e) {
            handleException(req, res, e);
        }
    }

    /**
     * 예외를 처리하여 에러 페이지로 포워딩합니다. <br> GET 또는 POST 요청인 경우 에러 페이지로 포워딩하고, 그 외의 경우 상태 코드와 메시지를 반환합니다.
     * <br> 이 외의 경우에 대해서는 상태 코드와 응답 바디로 메시지를 반환합니다.
     *
     * @param req 클라이언트가 필터에 요청한 내용을 포함하는 {@link HttpServletRequest} 객체
     * @param res 필터가 클라이언트에게 보내는 응답을 포함하는 {@link HttpServletResponse} 객체
     * @param e   발생한 예외 <code>Exception</code>
     * @throws IOException      이 필터의 처리 중 I/O 오류가 발생한 경우
     * @throws ServletException 처리 중 다른 이유로 실패한 경우
     */
    private void handleException(HttpServletRequest req, HttpServletResponse res, Exception e)
        throws ServletException, IOException {
        log.error(e.getMessage());
        if (req.getMethod().equals("GET") || req.getMethod().equals("POST")) {
            req.setAttribute("message", e.getMessage());
            req.setAttribute("statusCode", verifyStatusCode(e));
            req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, res);
            return;
        }
        res.setStatus(verifyStatusCode(e));
        res.getWriter().write(e.getMessage());
    }

    /**
     * 예외에 따라 상태 코드를 반환합니다.
     * <ul>
     *     <li>IllegalArgumentException: 400 Bad Request</li>
     *     <li>SecurityException: 401 Unauthorized</li>
     *     <li>AccessDeniedException: 403 Forbidden</li>
     *     <li>NoSuchElementException: 404 Not Found</li>
     *     <li>기타 예외: 500 Internal Server Error</li>
     * </ul>
     *
     * @param e 발생한 예외 <code>Exception</code>
     * @return 상태 코드
     */
    private int verifyStatusCode(Exception e) {
        if (e instanceof IllegalArgumentException) {
            return HttpServletResponse.SC_BAD_REQUEST;
        } else if (e instanceof SecurityException) {
            return HttpServletResponse.SC_UNAUTHORIZED;
        } else if (e instanceof AccessDeniedException) {
            return HttpServletResponse.SC_FORBIDDEN;
        } else if (e instanceof NoSuchElementException) {
            return HttpServletResponse.SC_NOT_FOUND;
        } else {
            return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
    }
}
