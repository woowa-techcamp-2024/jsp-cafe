package codesquad.jspcafe.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * LoginAuthenticationFilter는 로그인을 한 사용자가 접근 불가능한 URI에 접근하는 경우를 처리하는 필터입니다 <br> 로그인 시 접근 불가능한 URI에
 * 접근하는 경우 메인 페이지로 리다이렉트합니다.
 */
@WebFilter({"/users/login", "/users/signup"})
public class LoginAuthenticationFilter extends HttpFilter {

    /**
     * 로그인 시 접근 불가능한 URI에 접근하는 경우 메인 페이지로 리다이렉트합니다.
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
        if (req.getSession().getAttribute("user") != null) {
            res.sendRedirect("/");
            return;
        }
        chain.doFilter(req, res);
    }
}
