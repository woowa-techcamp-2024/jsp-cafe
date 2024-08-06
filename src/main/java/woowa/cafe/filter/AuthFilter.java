package woowa.cafe.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class AuthFilter implements Filter {

    /**
     * 로그인이 필요한 API에 대해 로그인 여부를 확인합니다.
     * 로그인 상태를 확인하기 위해서 세션을 생성하고 세션에 로그인 유저의 정보가 입력되었는지를 확인합니다. 로그인 관련 처리는 {@link woowa.cafe.router.LoginRouter}에서 처리합니다.
     * @see woowa.cafe.router.LoginRouter#login
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (!(request instanceof HttpServletRequest)) {
            request.getRequestDispatcher("/template/error/404.jsp").forward(request, response);
            return;
        }

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        for (BlockAPI api : BlockAPI.values()) {
            if (httpServletRequest.getRequestURI().startsWith(api.url) &&
                httpServletRequest.getMethod().equals(api.method)) {
                HttpSession session = httpServletRequest.getSession(false);
                if (session == null || session.getAttribute("userInfo") == null) {
                    request.getRequestDispatcher("/login").forward(request, response);
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }

    private enum BlockAPI {
        // question
        QUESTION_CREATE("POST", "/question"),
        QUESTION_DETAIL_PAGE("GET", "/question/"),
        QUESTION_UPDATE("PATCH", "/question/"),
        QUESTION_DELETE("DELETE", "/question/"),
        // reply
        REPLY_CREATE("POST", "/reply"),
        REPLY_DELETE("DELETE", "/reply/"),
        ;
        private final String method;
        private final String url;

        BlockAPI(String method, String url) {
            this.method = method;
            this.url = url;
        }
    }

}
