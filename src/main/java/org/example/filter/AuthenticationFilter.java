package org.example.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.example.constance.SessionName;


/*
 * 등록된 url 에 GET 요청시
 * 로그인 상태가 아니면 로그인 페이지로 redirect 합니다
 * */
@WebFilter(
        urlPatterns = {"/articles/register", "/articles/*", "/articles/update-form/*", "/users/update-form/*"}
)
public class AuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        if ("GET".equalsIgnoreCase(httpServletRequest.getMethod())) {
            HttpSession session = httpServletRequest.getSession(false);
            if (session == null || session.getAttribute(SessionName.USER.getName()) == null) {
                httpServletResponse.sendRedirect("/login");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
