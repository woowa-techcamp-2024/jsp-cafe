package cafe.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class ErrorHandlingFilter implements MappingFilter {

    @Override
    public List<String> mappings() {
        return List.of("/*");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {

            httpRequest.setAttribute("errorMessage", e.getMessage());
            httpRequest.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(httpRequest, httpResponse);
        }
    }

    @Override
    public void destroy() {
        // 필터 종료 코드 (필요한 경우)
    }
}
