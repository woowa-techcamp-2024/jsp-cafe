package woowa.camp.jspcafe.web.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import woowa.camp.jspcafe.domain.exception.ArticleException;
import woowa.camp.jspcafe.domain.exception.ReplyException;
import woowa.camp.jspcafe.domain.exception.UnAuthorizationException;
import woowa.camp.jspcafe.domain.exception.UserException;
import woowa.camp.jspcafe.web.exception.AuthenticationException;

@WebFilter(filterName = "exceptionHandlerFilter", value = "/*")
public class ExceptionHandlerFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            chain.doFilter(httpRequest, httpResponse);
        } catch (Exception e) {
            handleException(httpRequest, httpResponse, e);
        }
    }

    private void handleException(HttpServletRequest req, HttpServletResponse resp, Exception e) throws IOException {
        if (e instanceof AuthenticationException) {
            log.warn("[AuthenticationException]", e);
            resp.sendRedirect(req.getContextPath() + "/users/login");

        } else if (e instanceof UnAuthorizationException) {
            log.warn("[UnAuthorizationException]", e);
            resp.sendRedirect(req.getContextPath() + "/");

        } else if (e instanceof ArticleException) {
            log.warn("[ArticleException]", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);

        } else if (e instanceof ReplyException) {
            log.warn("[ReplyException]", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);

        } else if (e instanceof UserException) {
            log.warn("[UserException]", e);
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);

        } else {
            log.warn("[Exception] - unexpected exception", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
