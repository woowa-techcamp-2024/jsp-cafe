package cafe.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

public class AuthenticationFilter extends HttpFilter {
    private final Pattern[] authenticatedPatterns = {
            Pattern.compile("/users/.*/edit"),
            Pattern.compile("/users/sign-out"),
            Pattern.compile("/articles/.*"),
            Pattern.compile("/comments/*")
    };

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        String uri = ((HttpServletRequest) req).getRequestURI().toString();
        boolean signIn = (boolean) req.getAttribute("sign-in");
        boolean auth = Arrays.stream(authenticatedPatterns).anyMatch(pattern -> pattern.matcher(uri).matches());

        if (!signIn && auth) {
            ((HttpServletResponse) res).sendRedirect("/users/sign-in");
            return;
        }
        chain.doFilter(req, res);
    }
}
