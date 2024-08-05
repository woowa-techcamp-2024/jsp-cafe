package codesqaud.app.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/qna/*")
public class HttpMethodOverrideFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String overridingMethod = req.getParameter("_method");

        if (overridingMethod != null) {
            req = new HttpServletRequestWrapper(req) {
                @Override
                public String getMethod() {
                    return overridingMethod.toUpperCase();
                }
            };
        }

        super.doFilter(req, res, chain);
    }
}
