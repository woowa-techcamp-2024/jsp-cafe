package codesquad.global.filter;

import codesquad.common.handler.HandlerMapping;
import codesquad.common.handler.RequestHandler;
import codesquad.common.handler.annotation.Authorized;
import codesquad.user.domain.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <h3>인증을 위한 필터</h3>
 */
public class AuthenticationFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final Map<String, List<Pattern>> requestMap = new HashMap<>();
    private final Map<String, List<Pattern>> whiteMap = new HashMap<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();

        // Handler의 Authorize 확인
        logger.info("checking handler");
        List<HandlerMapping> handlerMappings = (List<HandlerMapping>) servletContext.getAttribute("HandlerMappings");
        for (HandlerMapping handlerMapping : handlerMappings) {
            Pattern pattern = handlerMapping.getPattern();
            RequestHandler handler = handlerMapping.getHandler();

            for (Method method : handler.getClass().getDeclaredMethods()) {
                String methodString = exchange(method);
                if (methodString == null) {
                    continue;
                }

                Authorized annotation = method.getAnnotation(Authorized.class);
                if (annotation == null) {
                    logger.info("Authorized not found for {} {}", method.getName(), pattern);
                    whiteMap.computeIfAbsent(methodString, (key) -> new ArrayList<>()).add(pattern);
                    logger.info("Filter registered exception: {} {}", methodString, pattern);
                } else {
                    logger.info("Authorized found for {} {}", method.getName(), pattern);
                    requestMap.computeIfAbsent(methodString, (key) -> new ArrayList<>()).add(pattern);
                    logger.info("Filter registered: {} {}", methodString, pattern);
                }
            }
        }
    }

    private String exchange(Method method) {
        switch (method.getName()) {
            case "doGet":
                return "GET";
            case "doPost":
                return "POST";
            case "doPut":
                return "PUT";
            case "doDelete":
                return "DELETE";
            default:
                return null;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String method = httpRequest.getMethod();
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        logger.info("checking request authentication");

        if (whiteMap.containsKey(method)) {
            List<Pattern> patterns = whiteMap.get(method);
            for (Pattern pattern : patterns) {
                Matcher matcher = pattern.matcher(path);
                if (matcher.matches()) {
                    logger.info("white request {}", path);
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        if (requestMap.containsKey(method)) {
            List<Pattern> patterns = requestMap.get(method);
            for (Pattern pattern : patterns) {
                Matcher matcher = pattern.matcher(path);
                if (matcher.matches() && !isAuthenticated(httpRequest)) {
                    logger.info("black request unauthenticated");
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isAuthenticated(HttpServletRequest httpRequest) throws IOException {
        // 세션에 User 객체가 있는지 확인
        HttpSession session = httpRequest.getSession(false);
        return session != null && session.getAttribute("loginUser") != null && User.class.isAssignableFrom(session.getAttribute("loginUser").getClass());
    }
}
