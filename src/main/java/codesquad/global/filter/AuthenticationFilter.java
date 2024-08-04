package codesquad.global.filter;

import codesquad.common.handler.HandlerMapping;
import codesquad.common.handler.RequestHandler;
import codesquad.common.handler.annotation.Authorized;
import codesquad.common.handler.annotation.RequestMapping;
import codesquad.user.domain.User;
import jakarta.servlet.*;
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
 * <h4>설명</h4>
 * 인증이 된 사용자만 접근할 수 있는 요청들을 선처리하는 필터입니다.
 * <h4>사용법</h4>
 * ReqeustHandler 구현체의 메서드에 {@link Authorized}를 붙이면 {@link RequestMapping}의 path를 필터에 등록하게 됩니다.
 * 하단은 `POST /questions`를 인증이 필요한 요청으로 등록하는 예시입니다.
 * <pre>
 *     {@code
 * @RequestMapping("/questions")
 * public class QnasHandler extends HttpServletRequestHandler {
 *     private final RegisterArticleService registerArticleService;
 *
 *     public QnasHandler(RegisterArticleService registerArticleService) {
 *         this.registerArticleService = registerArticleService;
 *     }
 *
 *     @Authorized
 *     @Override
 *     protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
 *         String title = req.getParameter("title");
 *         String writer = ((User) req.getSession().getAttribute("loginUser")).getUserId();
 *         String content = req.getParameter("contents");
 *
 *         Command command = new Command(title, writer, content);
 *         registerArticleService.register(command);
 *         resp.sendRedirect(req.getContextPath() + "/");
 *     }
 * }
 *     }
 * </pre>
 * 또한 현재로써는 Handler를 수동으로 ServletContext에 등록해주어야 합니다.
 * AuthenticationFilter 또한 ServletContext에 등록된 Handler를 통해 초기화를 진행하기 때문에 해당 필터에 등록되기 위해서는 ServletContext에 등록되어야 합니다.
 *
 * <p>(추후에는 `@RequestMapping`을 붙인 클래스를 수집해서 서블릿에 자동으로 등록해주는 기능을 개발하고자 합니다.)</p>
 *
 * @see codesquad.global.container.listener.HandlerRegister
 * @see #init(FilterConfig)
 */

public class AuthenticationFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final Map<String, List<Pattern>> requestMap = new HashMap<>();

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
                if (annotation != null) {
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

        logger.info("checking request authentication {} {}", method, path);
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

    private boolean isAuthenticated(HttpServletRequest httpRequest) {
        // 세션에 User 객체가 있는지 확인
        HttpSession session = httpRequest.getSession(false);
        return session != null && session.getAttribute("loginUser") != null && User.class.isAssignableFrom(session.getAttribute("loginUser").getClass());
    }
}
