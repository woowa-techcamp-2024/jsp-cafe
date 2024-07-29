package codesquad.jspcafe.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/users", "/questions", "/questions/*"})
public class UserAuthenticationFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
        throws IOException, ServletException {
        if (req.getSession().getAttribute("user") == null) {
            res.sendRedirect("/users/login");
            return;
        }
        chain.doFilter(req, res);
    }
}
