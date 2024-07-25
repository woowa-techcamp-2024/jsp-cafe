package codesquad.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.IOException;

public class MethodOverrideFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest) {
            if (httpRequest.getDispatcherType() == DispatcherType.REQUEST) {
                String method = httpRequest.getParameter("_method");
                if (method != null && (method.equalsIgnoreCase("PUT") || method.equalsIgnoreCase("DELETE"))) {
                    chain.doFilter(new HttpMethodRequestWrapper(httpRequest, method), response);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    private static class HttpMethodRequestWrapper extends HttpServletRequestWrapper {
        private final String method;

        public HttpMethodRequestWrapper(HttpServletRequest request, String method) {
            super(request);
            this.method = method;
        }

        @Override
        public String getMethod() {
            return this.method;
        }
    }
}

