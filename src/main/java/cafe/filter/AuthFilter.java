package cafe.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class AuthFilter implements MappingFilter {
    private static final String[] AUTH_WHITELIST = {
            "/",
            "/questions",
            "/user/login",
            "/user/register",
    };

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (isAuthRequired(path) && req.getSession().getAttribute("user") == null) {
            res.sendRedirect("/user/login");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isAuthRequired(String path) {
        if (path.startsWith("/static")) {
            return false;
        }
        for (String pattern : AUTH_WHITELIST) {
            if (path.equals(pattern)) {
                return false;
            }
        }
        return true;
    }
}
