package cafe.filter;

import cafe.domain.db.UserDatabase;
import cafe.domain.entity.User;
import cafe.service.SessionService;
import cafe.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public class SignInFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Cookie[] cookies = ((HttpServletRequest) servletRequest).getCookies();
        String sessionid = getSessionId(cookies);
        SessionService sessionService = (SessionService) servletRequest.getServletContext().getAttribute("sessionService");
        UserService userService = (UserService) servletRequest.getServletContext().getAttribute("userService");

        String uri = ((HttpServletRequest) servletRequest).getRequestURI();
        validatePath(uri, sessionid, userService);

        if (sessionid == null || !sessionService.isSignIn(sessionid)) {
            servletRequest.setAttribute("sign-in", false);
        } else {
            servletRequest.setAttribute("uuid", userService.findBySession(sessionid).getId());
            servletRequest.setAttribute("sign-in", true);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void validatePath(String uri, String sessionid, UserService userService) {
        String[] paths = uri.split("/");
        if (paths.length <= 3) return;
        if (paths[1].equals("users") && paths[3].equals("edit")) {
            User sessionUser = userService.findBySession(sessionid).getUser();
            User user = userService.find(uri).getUser();
            if (!user.getUserid().equals(sessionUser.getUserid())) throw new IllegalArgumentException("You can't edit other user's information!");
        }
    }

    private String getSessionId(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("JSESSIONID")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
