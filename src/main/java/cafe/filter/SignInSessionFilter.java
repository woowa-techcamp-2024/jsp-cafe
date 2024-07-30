package cafe.filter;

import cafe.domain.entity.User;
import cafe.service.SessionService;
import cafe.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class SignInSessionFilter extends HttpFilter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        String sessionid = ((HttpServletRequest) req).getSession(true).getId();
        SessionService sessionService = (SessionService) req.getServletContext().getAttribute("sessionService");
        req.setAttribute("sign-in", false);

        if (sessionid != null && sessionService.isSignIn(sessionid)) {
            User user = sessionService.findUserBySession(sessionid);
            req.setAttribute("id", user.getUserId());
            req.setAttribute("sign-in", true);
        }
        chain.doFilter(req, res);
    }
}
