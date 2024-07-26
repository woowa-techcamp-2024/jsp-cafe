package codesqaud.app.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("/*")
public class RemoveSessionAttributeFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpSession session = req.getSession();
        if(session != null) {
            if(!isInProgressCheckPassword(req)) {
                session.removeAttribute("checkPassword");
            }
        }
        
        super.doFilter(req, res, chain);
    }

    private boolean isInProgressCheckPassword(HttpServletRequest req) {
        return req.getRequestURI().equals("/users/profile");
    }
}
