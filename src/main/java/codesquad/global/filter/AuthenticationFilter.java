package codesquad.global.filter;

import codesquad.domain.user.User;
import codesquad.servlet.annotation.authentication.Authorized;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
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
        Map<String, ? extends ServletRegistration> servletRegistrations = servletContext.getServletRegistrations();

        for (ServletRegistration registration : servletRegistrations.values()) {
            String className = registration.getClassName();
            try {
                // 서블릿 클래스 로드
                Class<?> servletClass = Class.forName(className);
                // 서블릿 클래스의 메서드 정보 출력
                Method[] methods = servletClass.getDeclaredMethods();
                // Authorized annotation 유무 확인
                for (Method method : methods) {
                    // doGet/doPost/doPut/doDelete만 분리
                    String methodString = exchange(method);
                    if (methodString == null) {
                        continue;
                    }
                    // 있으면 requestMap, 없으면 whiteMap에 등록
                    Authorized annotation = method.getAnnotation(Authorized.class);
                    if (annotation == null) {
                        Collection<String> mappings = registration.getMappings();
                        List<Pattern> patterns = whiteMap.getOrDefault(methodString, new ArrayList<>());
                        for (String mapping : mappings) {
                            patterns.add(Pattern.compile(mapping.replaceAll("\\*", ".*")));
                        }
                        whiteMap.put(methodString, patterns);
                        logger.info("Filter Registered except: {} {}", methodString, mappings);
                        continue;
                    }
                    Collection<String> mappings = registration.getMappings();
                    List<Pattern> patterns = requestMap.getOrDefault(methodString, new ArrayList<>());
                    for (String mapping : mappings) {
                        patterns.add(Pattern.compile(mapping.replaceAll("\\*", ".*")));
                    }
                    requestMap.put(methodString, patterns);
                    logger.info("Filter Registered : {} {}", methodString, mappings);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
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

        if (whiteMap.containsKey(method)) {
            List<Pattern> patterns = whiteMap.get(method);
            for (Pattern pattern : patterns) {
                Matcher matcher = pattern.matcher(path);
                if (matcher.matches()) {
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
