package woopaca.jspcafe.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import woopaca.jspcafe.service.AuthService;

import java.io.IOException;

@WebFilter("*")
public class AuthenticationFilter implements Filter {

    private final RequestMatchers includeMatchers = new RequestMatchers();

    private AuthService authService;

    @Override
    public void init(FilterConfig filterConfig) {
        includeMatchers.addMatcher("GET", "/users/*");
        includeMatchers.addMatcher("GET", "/users/profile/*");
        includeMatchers.addMatcher("POST", "/users/profile/*");

        ServletContext servletContext = filterConfig.getServletContext();
        this.authService = (AuthService) servletContext.getAttribute("authService");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        if (!includeMatchers.contains(method, requestURI)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession();
        Object authentication = session.getAttribute("authentication");
        if (authentication == null) {
            ((HttpServletResponse) response).sendRedirect("/login");
            return;
        }
        chain.doFilter(request, response);
    }
}
