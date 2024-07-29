package codesquad.jspcafe.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter({"/users/login", "/users/signup"})
public class LoginAuthenticationFilter extends HttpFilter {

    /*
    공통 접근 가능 URI: /resources/*, /, /index.html
    로그인 시 접근 가능 URI: /logout, etc...
    로그인 시 접근 불가능 URI: /users/login, /users/signup
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
