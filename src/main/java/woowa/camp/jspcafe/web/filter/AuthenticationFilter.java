package woowa.camp.jspcafe.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.domain.User;
import woowa.camp.jspcafe.web.exception.AuthenticationException;

@WebFilter(filterName = "authenticationFilter", value = "/*")
public class AuthenticationFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationFilter.class);
    private static final String AUTHENTICATION_COOKIE_NAME = "WOOWA_SESSIONID";
    private static final List<Pattern> WHITE_LIST_PATTERNS = Arrays.asList(
            Pattern.compile("^/?$"),  // 루트 경로
            Pattern.compile("^/static(/.*)?$"),  // static 리소스
            Pattern.compile("^/articles\\?page=\\d*$"),  // 게시글 페이지네이션
            Pattern.compile("^/users/registration$"),
            Pattern.compile("^/users/login$"),
            Pattern.compile("^/users/login/fail$")
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.info("AuthenticationFilter doFilter start");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (isUnAuthenticateAPI(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        validateCookieExist(cookies);
        Cookie authCookie = validateAuthCookieExist(cookies);
        HttpSession session = httpRequest.getSession(false);
        validateSessionKeyExist(session, authCookie);
        validateSessionValue(session, authCookie);

        chain.doFilter(request, response);
    }

    private boolean isUnAuthenticateAPI(HttpServletRequest httpRequest) {
        String requestURI = httpRequest.getRequestURI();
        String queryString = httpRequest.getQueryString();
        String fullPath = isQueryStringExist(queryString) ? requestURI + "?" + queryString : requestURI;
        return WHITE_LIST_PATTERNS.stream()
                .anyMatch(pattern -> pattern.matcher(fullPath).matches());
    }

    private boolean isQueryStringExist(String queryString) {
        return queryString != null && !queryString.isEmpty();
    }

    private void validateCookieExist(Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            throw new AuthenticationException("Cookie not found");
        }
    }

    private Cookie validateAuthCookieExist(Cookie[] cookies) {
        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(AUTHENTICATION_COOKIE_NAME))
                .findFirst()
                .orElseThrow(() -> new AuthenticationException(AUTHENTICATION_COOKIE_NAME + " Cookie not found"));
    }

    private void validateSessionKeyExist(HttpSession httpSession, Cookie cookie) {
        if (httpSession == null) {
            throw new AuthenticationException("Session not found");
        }
        Object attribute = httpSession.getAttribute(cookie.getName());
        if (attribute == null) {
            throw new AuthenticationException("Session key not found");
        }
    }

    private void validateSessionValue(HttpSession httpSession, Cookie cookie) {
        Object attribute = httpSession.getAttribute(cookie.getName());
        if (!(attribute instanceof User sessionUser)) {
            throw new AuthenticationException("Invalid session type");
        }
        long cookieUserId;
        try {
            cookieUserId = Long.parseLong(cookie.getValue());
        } catch (NumberFormatException e) {
            throw new AuthenticationException("Invalid session value", e);
        }
        if (!Objects.equals(sessionUser.getId(), cookieUserId)) {
            throw new AuthenticationException("User ID mismatch");
        }
    }

}
