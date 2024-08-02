package org.example.config.filter;

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
import java.io.IOException;
import java.util.Set;
import org.example.config.conatiner.ApplicationContext;
import org.example.util.session.SessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter("/*")
public class SessionCheckFilter implements Filter {

    private static final Set<String> STATIC_PATHS = Set.of("/static/", "/js/", "/images/");
    private static final Set<String> EXCLUDE_PATHS = Set.of("/", "/user/login", "/user/signup");

    private static final Logger logger = LoggerFactory.getLogger(SessionCheckFilter.class);
    private SessionManager sessionManager;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getRequestURI().substring(request.getContextPath().length());

        if (STATIC_PATHS.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (!EXCLUDE_PATHS.contains(path)) {
            logger.info("login check path : {}", path);
            HttpSession session = request.getSession(false);
            if (session == null) {
                response.sendRedirect("/user/login");
                return;
            }
            // 세션이 있더라도 세션매니저 내부에 존재하지 않으면 로그인 페이지로 보낸다.
            if (sessionManager.getSessionFromManager(session.getId()) == null) {
                response.sendRedirect("/user/login");
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        ServletContext servletContext = filterConfig.getServletContext();
        ApplicationContext applicationContext = (ApplicationContext) servletContext.getAttribute("applicationContext");
        Object bean = applicationContext.getBean(SessionManager.class);
        sessionManager = (SessionManager) bean;
    }
}
