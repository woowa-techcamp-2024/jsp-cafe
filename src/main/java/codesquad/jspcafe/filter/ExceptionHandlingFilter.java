package codesquad.jspcafe.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebFilter("/*")
public class ExceptionHandlingFilter extends HttpFilter {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlingFilter.class);

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
        throws IOException, ServletException {
        try {
            chain.doFilter(req, res);
        } catch (Exception e) {
            handleException(req, res, e);
        }
    }

    private void handleException(HttpServletRequest req, HttpServletResponse res, Exception e)
        throws ServletException, IOException {
        log.error(e.getMessage());
        req.setAttribute("message", e.getMessage());
        req.setAttribute("statusCode", verifyStatusCode(e));
        req.getRequestDispatcher("/WEB-INF/jsp/error.jsp").forward(req, res);
    }

    private int verifyStatusCode(Exception e) {
        if (e instanceof IllegalArgumentException) {
            return HttpServletResponse.SC_BAD_REQUEST;
        } else {
            return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
    }
}
