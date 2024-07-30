package cafe.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

public class ErrorHandlingFilter implements MappingFilter {
    private static final Logger log = Logger.getLogger(ErrorHandlingFilter.class.getName());

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            chain.doFilter(request, response);
            if (!httpResponse.isCommitted() &&
                    httpResponse.getStatus() >= 400 &&
                    httpRequest.getHeaders("Accept").nextElement().split(",")[0].trim().equals("text/html")
            ) {
                httpRequest.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(httpRequest, httpResponse);
            }
        } catch (Exception e) {
            if (!httpResponse.isCommitted() &&
                    httpResponse.getStatus() >= 400 &&
                    httpRequest.getHeaders("Accept").nextElement().split(",")[0].trim().equals("text/html")
            ) {
                httpRequest.setAttribute("errorMessage", e.getMessage());
                httpRequest.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(httpRequest, httpResponse);
            }
        }
    }
}
